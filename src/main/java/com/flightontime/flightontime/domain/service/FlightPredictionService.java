package com.flightontime.flightontime.domain.service;

import com.flightontime.flightontime.api.dto.request.DataScienceRequest;
import com.flightontime.flightontime.api.dto.request.PredictionRequest;
import com.flightontime.flightontime.api.dto.response.DataScienceResponse;
import com.flightontime.flightontime.api.dto.response.PredictionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

@Service
public class FlightPredictionService {

    private final RestTemplate restTemplate;

    // Pega a URL do application.properties
    @Value("${ml.api.base-url:http://127.0.0.1:8000}")
    private String dsApiUrl;

    public FlightPredictionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PredictionResponse predict(PredictionRequest request) {
        DataScienceRequest dsRequest = convertToDsRequest(request);
        String url = dsApiUrl + "/predict";

        try {
            // 1. Pega a resposta como String Pura para não falhar no parse automático
            ResponseEntity<String> rawResponse = restTemplate.postForEntity(url, dsRequest, String.class);

            // 2. LOGA O QUE VEIO (Isso vai revelar o erro se persistir)
            System.out.println("🔍 STATUS PYTHON: " + rawResponse.getStatusCode());
            System.out.println("📦 BODY PYTHON: " + rawResponse.getBody());

            // 3. Tenta converter manualmente com suporte a NaN
            ObjectMapper mapper = JsonMapper.builder()
                    .enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS)
                    .build();

            DataScienceResponse dsResponse = mapper.readValue(rawResponse.getBody(), DataScienceResponse.class);

            return mapToPredictionResponse(dsResponse);

        } catch (Exception e) {
            // Agora teremos o log acima para saber o que aconteceu
            System.err.println("❌ ERRO NO PARSE: " + e.getMessage());
            throw new RuntimeException("Falha ao processar resposta do Python. Ver logs.", e);
        }
    }

    private DataScienceRequest convertToDsRequest(PredictionRequest req) {
        DataScienceRequest ds = new DataScienceRequest();

        // Mapeia Companhia (Lógica simplificada para MVP)
        ds.setAirline(mapAirlineCode(req.getCompanhia()));
        ds.setOrigin(req.getOrigemAeroporto());
        ds.setDest(req.getDestinoAeroporto());

        // Extrai a parte da DATA (yyyy-MM-dd) do LocalDateTime
        ds.setFlightDate(req.getDataPartida().toLocalDate().toString());

        // Conversão de Data e Hora
        ds.setDayOfWeek(req.getDataPartida().getDayOfWeek().getValue());
        int hour = req.getDataPartida().getHour();
        int minute = req.getDataPartida().getMinute();
        ds.setCrsDepTime(hour * 100 + minute); // Ex: 14:30 -> 1430

        // CRÍTICO: Conversão KM -> Milhas (O modelo foi treinado em milhas)
        ds.setDistance(req.getDistanciaKm() * 0.621371);

        return ds;
    }

    private String mapAirlineCode(String companhiaInput) {
        if (companhiaInput == null)
            return "XX";
        String input = companhiaInput.toUpperCase();

        // Mapeamento das principais cias brasileiras e americanas
        if (input.contains("LATAM"))
            return "LA";
        if (input.contains("GOL"))
            return "G3";
        if (input.contains("AZUL"))
            return "AD";
        if (input.contains("AMERICAN"))
            return "AA";
        if (input.contains("UNITED"))
            return "UA";
        if (input.contains("DELTA"))
            return "DL";

        // Fallback: tenta pegar os 2 primeiros chars
        return input.length() > 2 ? input.substring(0, 2) : input;
    }

    private PredictionResponse mapToPredictionResponse(DataScienceResponse dsResponse) {
        if (dsResponse == null)
            return null;

        PredictionResponse response = new PredictionResponse();

        // Converte a String do Python para o Integer esperado pelo Front-End (0 ou 1)
        String predStr = dsResponse.getPrediction() != null ? dsResponse.getPrediction() : "";
        Integer predInt = predStr.equalsIgnoreCase("Pontual") ? 0 : 1;
        response.setPredicao(predInt);

        response.setProbabilidade(dsResponse.getProbability() != null ? dsResponse.getProbability() : 0.0);
        response.setMensagem(
                dsResponse.getMessage() != null ? dsResponse.getMessage() : dsResponse.getRecommendation());

        if (dsResponse.getInternalMetrics() != null) {
            PredictionResponse.InternalMetrics metrics = new PredictionResponse.InternalMetrics();

            metrics.setRiscoHistoricoOrigem(dsResponse.getInternalMetrics().getHistoricalOriginRisk());
            metrics.setRiscoHistoricoCompanhia(dsResponse.getInternalMetrics().getHistoricalCarrierRisk());
            metrics.setFonte(dsResponse.getInternalMetrics().getSource());

            response.setMetricasInternas(metrics);
        }

        return response;
    }
}
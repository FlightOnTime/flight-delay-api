package com.flightontime.flightontime.domain.service;

import com.flightontime.flightontime.api.dto.request.DataScienceRequest;
import com.flightontime.flightontime.api.dto.request.PredictionRequest;
import com.flightontime.flightontime.api.dto.response.DataScienceResponse;
import com.flightontime.flightontime.api.dto.response.PredictionResponse;
import com.flightontime.flightontime.api.exception.InvalidCarrierException;
import com.flightontime.flightontime.api.exception.MlServiceUnavailableException;
import com.flightontime.flightontime.api.exception.ModelNotLoadedException;

import com.flightontime.flightontime.domain.model.PredictionHistory;
import com.flightontime.flightontime.domain.repository.PredictionHistoryRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.time.LocalDateTime;

@Service
public class FlightPredictionService {

    private final RestTemplate restTemplate;
    private final PredictionHistoryRepository historyRepository;

    // Pega a URL do application.properties
    @Value("${ml.api.base-url:http://127.0.0.1:8000}")
    private String dsApiUrl;

    public FlightPredictionService(
            RestTemplate restTemplate,
            PredictionHistoryRepository historyRepository
    ) {
        this.restTemplate = restTemplate;
        this.historyRepository = historyRepository;
    }

    public PredictionResponse predict(PredictionRequest request) {

        long startTime = System.currentTimeMillis(); // ⏱️ mede tempo de resposta

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

            DataScienceResponse dsResponse =
                    mapper.readValue(rawResponse.getBody(), DataScienceResponse.class);

            // Se o Python respondeu algo inválido ou vazio
            if (dsResponse == null) {
                throw new ModelNotLoadedException("Resposta vazia do modelo de ML");
            }

            PredictionResponse response = mapToPredictionResponse(dsResponse);

            long endTime = System.currentTimeMillis();
            double responseTimeMs = endTime - startTime;

            // 🔹 SALVA HISTÓRICO DE SUCESSO
            PredictionHistory history = new PredictionHistory();
            history.setCompanhiaAerea(request.getCompanhia());
            history.setOrigemAeroporto(request.getOrigemAeroporto());
            history.setDestinoAeroporto(request.getDestinoAeroporto());
            history.setDataPartida(request.getDataPartida());
            history.setDiaDaSemana(request.getDataPartida().getDayOfWeek().name());

            history.setAtrasoPrevisto(response.getPredicao() == 1);
            history.setProbabilidadeAtraso(response.getProbabilidade());

            history.setModeloVersao("v1"); // pode ser dinâmico no futuro
            history.setTempoRespostaMs(responseTimeMs);

            history.setStatus(true);
            history.setRequestAt(LocalDateTime.now());

            historyRepository.save(history);

            return response;

        } catch (ResourceAccessException e) {

            long endTime = System.currentTimeMillis();

            // ML fora do ar, timeout, conexão recusada
            // 🔹 SALVA HISTÓRICO DE FALHA
            PredictionHistory history = new PredictionHistory();
            history.setCompanhiaAerea(request.getCompanhia());
            history.setOrigemAeroporto(request.getOrigemAeroporto());
            history.setDestinoAeroporto(request.getDestinoAeroporto());
            history.setDataPartida(request.getDataPartida());
            history.setDiaDaSemana(request.getDataPartida().getDayOfWeek().name());

            history.setStatus(false);
            history.setTempoRespostaMs((double) (endTime - startTime));
            history.setRequestAt(LocalDateTime.now());

            historyRepository.save(history);

            throw new MlServiceUnavailableException("Serviço de ML indisponível");

        } catch (InvalidCarrierException e) {
            // Companhia inválida: deixa o handler global tratar
            throw e;

        } catch (Exception e) {
            // Erro de parse, resposta inválida, NaN quebrado, etc
            throw new ModelNotLoadedException(
                    "Modelo de ML não carregado ou resposta inválida"
            );
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

        if (companhiaInput == null || companhiaInput.isBlank()) {
            throw new InvalidCarrierException("Companhia inválida");
        }

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

        // Nenhuma regra bateu → companhia inválida
        throw new InvalidCarrierException("Companhia inválida: " + companhiaInput);
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

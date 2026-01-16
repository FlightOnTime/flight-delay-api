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
import java.util.ArrayList;
import java.util.List;

@Service
public class FlightPredictionService {

    private final RestTemplate restTemplate;
    private final PredictionHistoryRepository historyRepository;

    // Pega a URL do application.properties
    @Value("${ml.api.base-url:http://127.0.0.1:8000}")
    private String dsApiUrl;

    public FlightPredictionService(
            RestTemplate restTemplate,
            PredictionHistoryRepository historyRepository) {
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

            // 3. Tenta converter manualmente com suporte a NaN
            ObjectMapper mapper = JsonMapper.builder()
                    .enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS)
                    .build();

            DataScienceResponse dsResponse = mapper.readValue(rawResponse.getBody(), DataScienceResponse.class);

            if (dsResponse == null) {
                throw new ModelNotLoadedException("Resposta vazia do modelo de ML");
            }

            PredictionResponse response = mapToPredictionResponse(dsResponse, request);

            long endTime = System.currentTimeMillis();

            // 🔹 SALVA HISTÓRICO DE SUCESSO
            PredictionHistory history = new PredictionHistory();
            history.setCompanhiaAerea(request.getCompanhia());
            history.setOrigemAeroporto(request.getOrigemAeroporto());
            history.setDestinoAeroporto(request.getDestinoAeroporto());
            history.setDataPartida(request.getDataPartida());

            history.setDistanciaKm(request.getDistanciaKm());
            history.setPrevisao(dsResponse.getPrediction());
            history.setConfianca(dsResponse.getMessage());
            history.setDiaDaSemana(request.getDataPartida().getDayOfWeek().name());
            history.setAtrasoPrevisto(response.getPredicao() == 1);
            history.setProbabilidadeAtraso(response.getProbabilidade());
            history.setModeloVersao("v1");
            history.setTempoRespostaMs((double) (endTime - startTime));
            history.setStatus(true);
            history.setRequestAt(LocalDateTime.now());

            historyRepository.save(history);

            return response;

        } catch (ResourceAccessException e) {
            // ML fora do ar, timeout, conexão recusada
            // 🔹 SALVA HISTÓRICO DE FALHA
            PredictionHistory history = new PredictionHistory();
            history.setCompanhiaAerea(request.getCompanhia());
            history.setOrigemAeroporto(request.getOrigemAeroporto());
            history.setDestinoAeroporto(request.getDestinoAeroporto());
            history.setDataPartida(request.getDataPartida());
            history.setDistanciaKm(request.getDistanciaKm()); // Correção: Campo obrigatório
            history.setPrevisao("No Horário"); // Valor padrão para respeitar a constraint do banco
            history.setProbabilidadeAtraso(0.0);
            history.setAtrasoPrevisto(false);
            history.setDiaDaSemana(request.getDataPartida().getDayOfWeek().name());
            history.setStatus(false);
            history.setTempoRespostaMs(0.0);
            history.setRequestAt(LocalDateTime.now());

            try {
                historyRepository.save(history);
            } catch (Exception dbEx) {
                // Logar mas não deixar derrubar a resposta original de 503
                System.err.println("Erro ao salvar histórico de falha: " + dbEx.getMessage());
            }

            throw new MlServiceUnavailableException("Serviço de ML indisponível");

        } catch (Exception e) {
            // Erro de parse, resposta inválida, NaN quebrado, etc
            throw new ModelNotLoadedException("Modelo de ML não carregado ou resposta inválida");
        }
    }

    /*
     * ===========================
     * EXPLICABILIDADE (CORE)
     * ===========================
     */
    private List<String> gerarExplicacoes(
            PredictionRequest request,
            DataScienceResponse dsResponse) {
        List<String> explicacoes = new ArrayList<>();

        // 1. Adiciona explicações que vieram diretamente do Modelo de ML (Python)
        if (dsResponse.getExplanations() != null) {
            explicacoes.addAll(dsResponse.getExplanations());
        }

        // 2. Lógica local de apoio (Regras de negócio Java)
        int hour = request.getDataPartida().getHour();
        if (hour >= 15 && hour <= 22) {
            explicacoes.add("Horário da tarde/noite aumenta o risco de atraso");
        }

        if (dsResponse.getInternalMetrics() != null) {
            if (dsResponse.getInternalMetrics().getHistoricalOriginRisk() != null &&
                    dsResponse.getInternalMetrics().getHistoricalOriginRisk() > 0.20) {
                explicacoes.add("Aeroporto de origem possui histórico elevado de atrasos");
            }

            if (dsResponse.getInternalMetrics().getHistoricalCarrierRisk() != null &&
                    dsResponse.getInternalMetrics().getHistoricalCarrierRisk() > 0.15) {
                explicacoes.add("Companhia aérea apresenta risco histórico relevante de atraso");
            }
        }

        if (explicacoes.isEmpty()) {
            explicacoes.add("Não foram identificados fatores críticos relevantes");
        }

        return explicacoes;
    }

    /*
     * ===========================
     * MAPEAMENTOS
     * ===========================
     */
    private PredictionResponse mapToPredictionResponse(
            DataScienceResponse dsResponse,
            PredictionRequest request) {
        PredictionResponse response = new PredictionResponse();

        // Converte a String do Python para o Integer esperado pelo Front-End (0 ou 1)
        Integer predicao = "Pontual".equalsIgnoreCase(dsResponse.getPrediction()) ? 0 : 1;

        response.setPredicao(predicao);
        response.setProbabilidade(dsResponse.getProbability());
        response.setMensagem(dsResponse.getMessage());

        // Vincula as explicações (API + Local)
        response.setExplicacoes(gerarExplicacoes(request, dsResponse));

        if (dsResponse.getInternalMetrics() != null) {
            response.setMetricasInternas(
                    PredictionResponse.InternalMetrics.builder()
                            .riscoHistoricoOrigem(dsResponse.getInternalMetrics().getHistoricalOriginRisk())
                            .riscoHistoricoCompanhia(dsResponse.getInternalMetrics().getHistoricalCarrierRisk())
                            .fonte(dsResponse.getInternalMetrics().getSource())
                            .build());
        }

        return response;
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
        if (input.contains("SOUTHWEST"))
            return "WN";

        throw new InvalidCarrierException("Companhia inválida: " + companhiaInput);
    }
}
package com.flightontime.flightontime.domain.service;

import com.flightontime.flightontime.api.dto.request.PredictionRequest;
import com.flightontime.flightontime.api.dto.response.PredictionResponse;
import com.flightontime.flightontime.client.MLApiClient;
import com.flightontime.flightontime.domain.mapper.FlightMlMapper;
import com.flightontime.flightontime.domain.mapper.FlightPredictionResponseMapper;
import com.flightontime.flightontime.domain.model.FlightPredictionResponse;
import com.flightontime.flightontime.domain.model.PredictionHistory;
import com.flightontime.flightontime.domain.repository.PredictionHistoryRepository;
import lombok.RequiredArgsConstructor; // Usando RequiredArgsConstructor para injeção limpa
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FlightPredictionService {

    private final MLApiClient mlApiClient;
    private final PredictionHistoryRepository historyRepository; // Repositório injetado

    public PredictionResponse predict(PredictionRequest request) {
        // 1. Converte Request da API para formato do Modelo ML (Python)
        var mlRequest = FlightMlMapper.toMl(request);

        // 2. Chama a API Python
        FlightPredictionResponse mlResponse;
        try {
            mlResponse = mlApiClient.predict(mlRequest);
        } catch (Exception e) {
            throw new RuntimeException("AI Service unavailable or returned an error: " + e.getMessage(), e);
        }

        // 3. Converte Resposta do Modelo para formato da API
        var apiResponse = FlightPredictionResponseMapper.toApi(mlResponse);

        // 4. SALVAR NO BANCO DE DADOS (A parte que faltava!)
        saveHistory(request, apiResponse);

        return apiResponse;
    }

    private void saveHistory(PredictionRequest request, PredictionResponse response) {
        try {
            PredictionHistory history = new PredictionHistory();

            // Mapeando dados da requisição (Ajustado para os nomes em português da
            // Entidade)
            history.setCompanhiaAerea(request.getCarrier() != null ? request.getCarrier().name() : null);
            history.setOrigemAeroporto(request.getOrigin());
            history.setDestinoAeroporto(request.getDestination());

            // Convertendo a String do DTO para LocalDateTime
            // Certifique-se que request.getDepartureDate() retorna uma String no formato
            // ISO (ex: 2023-10-27T10:15:30)
            history.setDataPartida(request.getDepartureDate());

            if (request.getDepartureDate() != null) {
                // Converte DayOfWeek (1=Seg, 7=Dom) para índice 0=Dom, 1=Seg...
                int dayIndex = request.getDepartureDate().getDayOfWeek().getValue() % 7;
                history.setDiaDaSemana(getDayOfWeekName(dayIndex));
            }

            history.setModeloVersao("randomforest_v7_final.pkl");

            // Mapeando dados da resposta
            // Nota: Verifique se 'status' na Resposta é Boolean ou String para bater com a
            // Entidade
            history.setAtrasoPrevisto(
                    com.flightontime.flightontime.api.dto.enums.PredictionStatus.DELAYED.equals(response.getStatus()));
            history.setProbabilidadeAtraso(response.getProbability());

            // Metadados e Status fixo
            history.setStatus(true); // O campo 'status' na sua entidade é obrigatório (nullable = false)
            history.setRequestAt(LocalDateTime.now()); // Na entidade o campo é 'requestAt', não 'createdAt'

            // Persistir
            historyRepository.save(history);

        } catch (Exception e) {
            // Log de erro para não quebrar a resposta ao usuário se o banco falhar
            System.err.println("Erro ao salvar histórico de predição: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método auxiliar para converter número do dia em nome
    private String getDayOfWeekName(Integer dayOfWeek) {
        if (dayOfWeek == null)
            return null;

        String[] days = { "Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado" };
        return dayOfWeek >= 0 && dayOfWeek < days.length ? days[dayOfWeek] : null;
    }
}
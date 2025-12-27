package com.flightontime.flightontime.domain.mapper;

import com.flightontime.flightontime.api.dto.enums.PredictionStatus;
import com.flightontime.flightontime.api.dto.response.PredictionResponse;
import com.flightontime.flightontime.domain.model.FlightPredictionResponse;

public class FlightPredictionResponseMapper {

    private FlightPredictionResponseMapper() {}

    public static PredictionResponse toApi(FlightPredictionResponse ml) {

        return new PredictionResponse(
                mapStatus(ml.getPrevisao()),
                ml.getProbabilidadeAtraso()
        );
    }

    private static PredictionStatus mapStatus(String previsao) {
        if (previsao == null) {
            throw new IllegalArgumentException("Previsão do ML não pode ser nula");
        }

        return switch (previsao.toUpperCase()) {
            case "ATRASADO" -> PredictionStatus.DELAYED;
            case "NO_HORARIO", "NO HORARIO", "ONTIME" -> PredictionStatus.ON_TIME;
            default -> throw new IllegalArgumentException("Status desconhecido: " + previsao);
        };
    }
}

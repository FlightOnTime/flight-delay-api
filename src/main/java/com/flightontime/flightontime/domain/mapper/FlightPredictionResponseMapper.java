package com.flightontime.flightontime.domain.mapper;

import com.flightontime.flightontime.api.dto.response.PredictionResponse;
import com.flightontime.flightontime.domain.model.FlightPredictionResponse;

public class FlightPredictionResponseMapper {

    private FlightPredictionResponseMapper() {}

    public static PredictionResponse toApi(
            FlightPredictionResponse mlResponse
    ) {
        return new PredictionResponse(
                mlResponse.getPrevisao(),
                mlResponse.getProbabilidadeAtraso()
        );
    }
}

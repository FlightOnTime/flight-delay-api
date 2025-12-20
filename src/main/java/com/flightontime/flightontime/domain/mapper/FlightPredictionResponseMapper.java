package com.flightontime.flightontime.domain.mapper;

import com.flightontime.flightontime.domain.model.FlightPredictionResponse;

public class FlightPredictionResponseMapper {

    private FlightPredictionResponseMapper() {}

    public static FlightPredictionResponse fromMl(
            FlightPredictionResponse mlResponse
    ) {
        FlightPredictionResponse response = new FlightPredictionResponse();

        response.setPrevisao(mlResponse.getPrevisao());
        response.setProbabilidadeAtraso(mlResponse.getProbabilidadeAtraso());
        response.setConfianca(mlResponse.getConfianca());
        response.setPrincipaisFatores(mlResponse.getPrincipaisFatores());
        response.setRecomendacoes(mlResponse.getRecomendacoes());

        return response;
    }
}

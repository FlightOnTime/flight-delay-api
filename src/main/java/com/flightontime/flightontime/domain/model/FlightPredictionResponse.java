package com.flightontime.flightontime.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class FlightPredictionResponse {

    private String previsao;

    @JsonProperty("probabilidade_atraso")
    private double probabilidadeAtraso;

    private String confianca;

    @JsonProperty("principais_fatores")
    private List<String> principaisFatores;

    private List<String> recomendacoes;
}

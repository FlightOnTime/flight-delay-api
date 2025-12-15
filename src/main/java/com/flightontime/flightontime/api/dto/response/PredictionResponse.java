package com.flightontime.flightontime.api.dto.response;

public class PredictionResponse {

    private String status;
    private double probabilidade;

    public PredictionResponse(String status, double probabilidade) {
        this.status = status;
        this.probabilidade = probabilidade;
    }

    public String getStatus() {
        return status;
    }

    public double getProbabilidade() {
        return probabilidade;
    }
}

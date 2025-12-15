package com.flightontime.flightontime.api.dto.request;

import java.time.LocalDateTime;


public class PredictionRequest {

    private String companhia;
    private String origem_aeroporto;
    private String destino_aeroporto;
    private LocalDateTime data_partida;
    private double distancia_km;

    public PredictionRequest(String companhia, String origem, String destino, LocalDateTime data_partida, double distancia_km) {
        this.companhia = companhia;
        this.origem_aeroporto = origem;
        this.destino_aeroporto = destino;
        this.data_partida = data_partida;
        this.distancia_km = distancia_km;
    }

    public String getCompanhia() {
        return companhia;
    }

    public String getOrigem_aeroporto() {
        return origem_aeroporto;
    }

    public String getDestino_aeroporto() {
        return destino_aeroporto;
    }

    public LocalDateTime getData_partida() {
        return data_partida;
    }
    public double getDistancia_km() {
        return distancia_km;
    }

    public void setCompanhia(String companhia) {
        this.companhia = companhia;
    }

    public void setOrigem_aeroporto(String origem_aeroporto) {
        this.origem_aeroporto = origem_aeroporto;
    }

    public void setDestino_aeroporto(String destino_aeroporto) {
        this.destino_aeroporto = destino_aeroporto;
    }

    public void setData_partida(LocalDateTime data_partida) {
        this.data_partida = data_partida;
    }

    public void setDistancia_km(double distancia_km) {
        this.distancia_km = distancia_km;
    }

}




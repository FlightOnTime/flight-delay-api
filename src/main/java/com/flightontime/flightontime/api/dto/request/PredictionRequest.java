package com.flightontime.flightontime.api.dto.request;

import java.util.Date;

public class PredictionRequest {

    private String companhia;
    private String origem;
    private String destino;
    private Date data_partida;
    private double distancia_km;

    public PredictionRequest(String companhia, String origem, String destino, Date data_partida, double distancia_km) {
        this.companhia = companhia;
        this.origem = origem;
        this.destino = destino;
        this.data_partida = data_partida;
        this.distancia_km = distancia_km;
    }

    public String getCompanhia() {
        return companhia;
    }

    public String getOrigem() {
        return origem;
    }

    public String getDestino() {
        return destino;
    }

    public Date getData_partida() {
        return data_partida;
    }

    public double getDistancia_km() {
        return distancia_km;
    }

    public void setCompanhia(String companhia) {
        this.companhia = companhia;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public void setData_partida(Date data_partida) {
        this.data_partida = data_partida;
    }

    public void setDistancia_km(double distancia_km) {
        this.distancia_km = distancia_km;
    }
}




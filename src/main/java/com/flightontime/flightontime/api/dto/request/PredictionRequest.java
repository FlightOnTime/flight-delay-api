package com.flightontime.flightontime.api.dto.request;

import java.time.LocalDateTime;


public class PredictionRequest {

    private String companhia;
    private String origemAeroporto;
    private String destinoAeroporto;
    private LocalDateTime dataPartida;
    private double distanciakm;

    public PredictionRequest(String companhia, String origemAeroporto, String destinoAeroporto, LocalDateTime dataPartida, double distanciakm) {
        this.companhia = companhia;
        this.origemAeroporto = origemAeroporto;
        this.destinoAeroporto = destinoAeroporto;
        this.dataPartida = dataPartida;
        this.distanciakm = distanciakm;
    }

    public String getCompanhia() {
        return companhia;
    }

    public void setCompanhia(String companhia) {
        this.companhia = companhia;
    }

    public String getOrigemAeroporto() {
        return origemAeroporto;
    }

    public void setOrigemAeroporto(String origemAeroporto) {
        this.origemAeroporto = origemAeroporto;
    }

    public String getDestinoAeroporto() {
        return destinoAeroporto;
    }

    public void setDestinoAeroporto(String destinoAeroporto) {
        this.destinoAeroporto = destinoAeroporto;
    }

    public LocalDateTime getDataPartida() {
        return dataPartida;
    }

    public void setDataPartida(LocalDateTime dataPartida) {
        this.dataPartida = dataPartida;
    }

    public double getDistanciakm() {
        return distanciakm;
    }

    public void setDistanciakm(double distanciakm) {
        this.distanciakm = distanciakm;
    }
}




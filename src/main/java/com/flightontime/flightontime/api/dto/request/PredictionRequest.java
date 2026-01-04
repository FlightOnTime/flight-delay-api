package com.flightontime.flightontime.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PredictionRequest {

    @NotBlank(message = "Companhia é obrigatória")
    @JsonProperty("companhia") // Contrato v2.1: Frontend envia "companhia"
    private String companhia;

    @NotBlank(message = "Origem é obrigatória")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Código IATA da origem deve ter 3 letras")
    @JsonProperty("origem_aeroporto") // Contrato v2.1
    private String origemAeroporto;

    @NotBlank(message = "Destino é obrigatório")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Código IATA do destino deve ter 3 letras")
    @JsonProperty("destino_aeroporto") // Contrato v2.1
    private String destinoAeroporto;

    @NotNull(message = "Data de partida é obrigatória")
    @JsonProperty("data_partida") // Contrato v2.1
    private LocalDateTime dataPartida;

    @NotNull(message = "Distância é obrigatória")
    @Positive(message = "Distância deve ser positiva")
    @JsonProperty("distancia_km") // Contrato v2.1: Entrada em KM
    private Double distanciaKm;
}
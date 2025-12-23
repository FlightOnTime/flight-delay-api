package com.flightontime.flightontime.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "prediction_history")
public class PredictionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "companhia_aerea", nullable = false)
    private String companhiaAerea;

    @Column(name = "origem_aeroporto", nullable = false)
    private String origemAeroporto;

    @Column(name = "destino_aeroporto", nullable = false)
    private String destinoAeroporto;

    @Column(name = "data_partida", nullable = false)
    private LocalDateTime dataPartida;

    @Column(name = "dia_da_semana")
    private String diaDaSemana;

    @Column(name = "atraso_previsto", nullable = false)
    private Boolean atrasoPrevisto;

    @Column(name = "probabilidade_atraso", nullable = false)
    private Double probabilidadeAtraso;

    @Column(name = "modelo_versao")
    private String modeloVersao;

    @Column(name = "tempo_resposta_ms")
    private Double tempoRespostaMs;

    @Column(nullable = false)
    private Boolean status;

    @Column(name = "request_at", nullable = false)
    private LocalDateTime requestAt;
}
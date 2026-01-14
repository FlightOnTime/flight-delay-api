package com.flightontime.flightontime.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PredictionResponse {

    @JsonProperty("predicao")
    private Integer predicao; // 1 = atraso, 0 = pontual

    @JsonProperty("probabilidade")
    private Double probabilidade;

    @JsonProperty("mensagem")
    private String mensagem;

    @JsonProperty("explicacoes")
    private List<String> explicacoes; // (Explicabilidade)

    @JsonProperty("metricas_internas")
    private InternalMetrics metricasInternas;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InternalMetrics {

        @JsonProperty("risco_historico_origem")
        private Double riscoHistoricoOrigem;

        @JsonProperty("risco_historico_companhia")
        private Double riscoHistoricoCompanhia;

        @JsonProperty("fonte")
        private String fonte;
    }
}

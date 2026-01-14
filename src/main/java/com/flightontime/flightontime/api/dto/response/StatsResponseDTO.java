package com.flightontime.flightontime.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Estatísticas agregadas das previsões de voos")
public class StatsResponseDTO {

    @JsonProperty("periodo")
    @Schema(description = "Período analisado", example = "Últimas 24 horas")
    private String periodo;

    @JsonProperty("total_previsoes")
    @Schema(description = "Total de previsões realizadas", example = "1547")
    private Long totalPrevisoes;

    @JsonProperty("taxa_atraso_previsto")
    @Schema(description = "Percentual de voos com atraso previsto", example = "42.5")
    private BigDecimal taxaAtrasoPrevisto;

    @JsonProperty("taxa_sucesso_api")
    @Schema(description = "Percentual de requisições bem-sucedidas", example = "99.2")
    private BigDecimal taxaSucessoApi;

    @JsonProperty("tempo_resposta_medio_ms")
    @Schema(description = "Tempo médio de resposta da API ML em milissegundos", example = "245")
    private BigDecimal tempoRespostaMedioMs;

    @JsonProperty("aeroportos_mais_problematicos")
    @Schema(description = "Top 5 aeroportos com maior taxa de atraso")
    private List<AeroportoStatDTO> aeroportosMaisProblematicos;

    @JsonProperty("companhias_melhor_desempenho")
    @Schema(description = "Top 5 companhias com menor taxa de atraso")
    private List<CompanhiaStatDTO> companhiasMelhorDesempenho;

    @JsonProperty("ultima_atualizacao")
    @Schema(description = "Data/hora da última atualização dos dados", example = "2026-01-07T13:42:00")
    private LocalDateTime ultimaAtualizacao;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AeroportoStatDTO {
        @JsonProperty("codigo_iata")
        private String codigoIata;

        @JsonProperty("total_voos")
        private Long totalVoos;

        @JsonProperty("taxa_atraso")
        private BigDecimal taxaAtraso;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompanhiaStatDTO {
        @JsonProperty("codigo_iata")
        private String codigoIata;

        @JsonProperty("total_voos")
        private Long totalVoos;

        @JsonProperty("taxa_atraso")
        private BigDecimal taxaAtraso;
    }
}

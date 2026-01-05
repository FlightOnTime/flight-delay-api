package com.flightontime.flightontime.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataScienceResponse {

    @JsonProperty("prediction")
    private String prediction; // Mudado para String porque o Python retorna "Pontual" ou "Atraso"

    @JsonProperty("probability")
    @com.fasterxml.jackson.annotation.JsonAlias("probability_delay")
    private Double probability;

    @JsonProperty("message")
    private String message;

    @JsonProperty("recommendation")
    private String recommendation;

    @JsonProperty("internal_metrics")
    private InternalMetrics internalMetrics;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InternalMetrics {

        @JsonProperty("historical_origin_risk")
        private Double historicalOriginRisk;

        @JsonProperty("historical_carrier_risk")
        private Double historicalCarrierRisk;

        @JsonProperty("source")
        private String source;
    }
}
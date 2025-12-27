package com.flightontime.flightontime.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FlightPredictionRequest {

    private String carrier;

    private String origin;

    private String dest;

    @JsonProperty("day_of_week")
    private Integer dayOfWeek;

    @JsonProperty("crs_dep_time")
    private Integer crsDepTime;

    private Double distance;

    @JsonProperty("origin_delay_rate")
    private Double originDelayRate;

    @JsonProperty("carrier_delay_rate")
    private Double carrierDelayRate;

    @JsonProperty("origin_traffic")
    private Integer originTraffic;
}

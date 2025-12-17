package com.flightontime.flightontime.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FlightPredictionRequest {

    @JsonProperty("Airline")
    private String airline;

    @JsonProperty("Origin")
    private String origin;

    @JsonProperty("Dest")
    private String dest;

    @JsonProperty("Month")
    private Integer month;

    @JsonProperty("DayOfWeek")
    private Integer dayOfWeek;

    @JsonProperty("CRSDepTime")
    private Integer crsDepTime;

    @JsonProperty("Distance")
    private Integer distance;
}

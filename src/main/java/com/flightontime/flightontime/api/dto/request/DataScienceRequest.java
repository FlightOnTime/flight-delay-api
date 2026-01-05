package com.flightontime.flightontime.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataScienceRequest {

    @JsonProperty("airline")
    private String airline;

    @JsonProperty("origin")
    private String origin;

    @JsonProperty("dest")
    private String dest;

    @JsonProperty("flight_date")
    private String flightDate;

    @JsonProperty("day_of_week")
    private Integer dayOfWeek;

    @JsonProperty("crs_dep_time")
    private Integer crsDepTime;

    @JsonProperty("distance")
    private Double distance;
}

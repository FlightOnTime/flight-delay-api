package com.flightontime.flightontime.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("carrier")
    private String carrier;

    @JsonProperty("origin")
    private String origin;

    @JsonProperty("dest")
    private String destination;

    @JsonProperty("departure_date")
    private LocalDateTime departureDate;

    @JsonProperty("distance")
    private double distanceMiles;

    @JsonProperty("day_of_week")
    private int dayOfWeek;

    @JsonProperty("origin_delay_rate")
    private double originDelayRate;

    @JsonProperty("carrier_delay_rate")
    private double carrierDelayRate;

    @JsonProperty("origin_traffic")
    private double accumulatedAirportTraffic;
}



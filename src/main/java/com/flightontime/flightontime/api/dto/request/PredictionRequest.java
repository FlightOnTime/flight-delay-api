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

    @JsonProperty("destination")
    private String destination;

    @JsonProperty("departure_date")
    private LocalDateTime departureDate;

    @JsonProperty("distance_miles")
    private double distanceMiles;

    @JsonProperty("airport_delay_rate")
    private double airportDelayRate;

    @JsonProperty("airline_delay_rate")
    private double airlineDelayRate;

    @JsonProperty("accumulated_airport_traffic")
    private double accumulatedAirportTraffic;
}



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

    @NotBlank
    @JsonProperty("carrier")
    private String carrier;

    @NotBlank
    @JsonProperty("origin")
    private String origin;

    @NotBlank
    @JsonProperty("destination")
    private String destination;

    @NotNull
    @JsonProperty("departure_date")
    private LocalDateTime departureDate;

    @Positive
    @JsonProperty("distance_miles")
    private double distanceMiles;

    @Min(0)
    @Max(1)
    @JsonProperty("airport_delay_rate")
    private double airportDelayRate;

    @Min(0)
    @Max(1)
    @JsonProperty("airline_delay_rate")
    private double airlineDelayRate;

    @PositiveOrZero
    @JsonProperty("accumulated_airport_traffic")
    private double accumulatedAirportTraffic;
}

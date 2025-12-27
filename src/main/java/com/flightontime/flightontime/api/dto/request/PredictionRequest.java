package com.flightontime.flightontime.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flightontime.flightontime.domain.enums.Airline;
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
    private Airline carrier;

    @NotBlank
    @JsonProperty("origin")
    private String origin;

    @NotBlank
    @JsonProperty("dest")
    private String destination;

    @NotNull
    @JsonProperty("departure_date")
    private LocalDateTime departureDate;

    @Positive
    @JsonProperty("distance")
    private double distanceMiles;

    @Min(1)
    @Max(7)
    @JsonProperty("day_of_week")
    private int dayOfWeek;

    @DecimalMin("0.0")
    @DecimalMax("1.0")
    @JsonProperty("origin_delay_rate")
    private double originDelayRate;

    @DecimalMin("0.0")
    @DecimalMax("1.0")
    @JsonProperty("carrier_delay_rate")
    private double carrierDelayRate;

    @PositiveOrZero
    @JsonProperty("origin_traffic")
    private Integer accumulatedAirportTraffic;

}



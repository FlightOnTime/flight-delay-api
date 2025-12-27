package com.flightontime.flightontime.api.dto.response;

import com.flightontime.flightontime.api.dto.enums.PredictionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictionResponse {

    private PredictionStatus status;
    private Double probability;
}

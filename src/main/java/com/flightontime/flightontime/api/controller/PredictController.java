package com.flightontime.flightontime.api.controller;

import com.flightontime.flightontime.api.dto.request.PredictionRequest;
import com.flightontime.flightontime.api.dto.response.PredictionResponse;
import com.flightontime.flightontime.domain.service.FlightPredictionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/predict")
@AllArgsConstructor
public class PredictController {

    private final FlightPredictionService predictionService;

    @PostMapping
    public ResponseEntity<PredictionResponse> predict(
            @Valid @RequestBody PredictionRequest request
    ) {
        PredictionResponse response = predictionService.predict(request);

        return ResponseEntity.ok(response);
    }
}

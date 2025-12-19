package com.flightontime.flightontime.api.controller;

import com.flightontime.flightontime.api.dto.request.PredictionRequest;
import com.flightontime.flightontime.domain.model.FlightPredictionResponse;
import com.flightontime.flightontime.service.FlightPredictionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/predict")
public class PredictController {

    private final FlightPredictionService predictionService;

    public PredictController(FlightPredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @PostMapping
    public ResponseEntity<FlightPredictionResponse> predict(
            @RequestBody PredictionRequest request
    ) {
        FlightPredictionResponse response =
                predictionService.predict(request);

        return ResponseEntity.ok(response);
    }
}

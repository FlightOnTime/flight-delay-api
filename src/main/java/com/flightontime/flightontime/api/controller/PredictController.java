package com.flightontime.flightontime.api.controller;

import com.flightontime.flightontime.api.dto.request.PredictionRequest;
import com.flightontime.flightontime.api.dto.response.PredictionResponse;
import com.flightontime.flightontime.domain.mapper.FlightPredictionResponseMapper;
import com.flightontime.flightontime.domain.model.FlightPredictionResponse;
import com.flightontime.flightontime.domain.service.FlightPredictionService;
import jakarta.validation.Valid;
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
    public ResponseEntity<PredictionResponse> predict(
            @Valid @RequestBody PredictionRequest request
    ) {
        FlightPredictionResponse mlResponse = predictionService.predict(request);
        PredictionResponse apiResponse = FlightPredictionResponseMapper.toApi(mlResponse);

        return ResponseEntity.ok(apiResponse);
    }
}

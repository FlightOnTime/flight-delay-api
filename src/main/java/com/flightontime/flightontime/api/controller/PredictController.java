package com.flightontime.flightontime.api.controller;

import com.flightontime.flightontime.api.dto.request.PredictionRequest;
import com.flightontime.flightontime.api.dto.response.PredictionResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/predict")
public class PredictController {

    @PostMapping
    public PredictionResponse predict(@RequestBody PredictionRequest request) {

        // MOCK temporário — até integração com modelo
        return new PredictionResponse(
                "Atrasado",
                0.75
        );
    }
}

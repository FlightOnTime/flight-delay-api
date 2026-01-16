package com.flightontime.flightontime.api.controller;

import com.flightontime.flightontime.api.dto.request.PredictionRequest;
import com.flightontime.flightontime.api.dto.response.PredictionResponse;
import com.flightontime.flightontime.domain.service.FlightPredictionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1") // Ajustado conforme contrato
@CrossOrigin(origins = "*") // Permite chamadas do Front local
public class PredictController {

    private final FlightPredictionService service;

    public PredictController(FlightPredictionService service) {
        this.service = service;
    }

    @PostMapping("/predict")
    public ResponseEntity<PredictionResponse> predict(@RequestBody @Valid PredictionRequest request) {
        PredictionResponse response = service.predict(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/predict/batch")
    public ResponseEntity<List<PredictionResponse>> predictBatch(@RequestBody List<PredictionRequest> requests) {
        List<PredictionResponse> responses = new ArrayList<>();

        // Processa um por um reutilizando a lógica existente
        for (PredictionRequest request : requests) {
            responses.add(service.predict(request));
        }

        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("FlightOnTime API (MVP Backend) - Online ✈️");
    }
}
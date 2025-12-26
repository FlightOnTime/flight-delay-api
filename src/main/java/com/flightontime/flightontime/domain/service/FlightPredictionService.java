package com.flightontime.flightontime.domain.service;

import com.flightontime.flightontime.api.dto.request.PredictionRequest;
import com.flightontime.flightontime.domain.mapper.FlightMlMapper;
import com.flightontime.flightontime.domain.model.FlightPredictionRequest;
import com.flightontime.flightontime.domain.model.FlightPredictionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Service
public class FlightPredictionService {

    private final RestTemplate restTemplate;
    private final String mlApiUrl;

    public FlightPredictionService(RestTemplate restTemplate, @Value("${flight-delay-ds.url}") String mlApiUrl) {
        this.restTemplate = restTemplate;
        this.mlApiUrl = mlApiUrl;
    }

    public FlightPredictionResponse predict(PredictionRequest request) {
        FlightPredictionRequest mlRequest = FlightMlMapper.toMl(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<FlightPredictionRequest> entity = new HttpEntity<>(mlRequest, headers);

        // Assumes endpoint is /predict based on standard FastAPI practices
        return restTemplate.postForObject(mlApiUrl + "/predict", entity, FlightPredictionResponse.class);
    }
}

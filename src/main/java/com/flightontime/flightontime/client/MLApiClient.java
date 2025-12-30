package com.flightontime.flightontime.client;

import com.flightontime.flightontime.domain.model.FlightPredictionRequest;
import com.flightontime.flightontime.domain.model.FlightPredictionResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@AllArgsConstructor
public class MLApiClient {

    private final WebClient webClient;

    public FlightPredictionResponse predict(FlightPredictionRequest request) {

        return webClient.post()
                .uri("/v1/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(FlightPredictionResponse.class)
                .block();
    }
}
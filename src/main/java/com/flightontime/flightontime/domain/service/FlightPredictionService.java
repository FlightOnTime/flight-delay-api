package com.flightontime.flightontime.domain.service;

import com.flightontime.flightontime.api.dto.request.PredictionRequest;
import com.flightontime.flightontime.api.dto.response.PredictionResponse;
import com.flightontime.flightontime.client.MLApiClient;
import com.flightontime.flightontime.domain.mapper.FlightMlMapper;
import com.flightontime.flightontime.domain.mapper.FlightPredictionResponseMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FlightPredictionService {

    private final MLApiClient mlApiClient;

    public PredictionResponse predict(PredictionRequest request) {

        var mlRequest = FlightMlMapper.toMl(request);

        var mlResponse = mlApiClient.predict(mlRequest);

        return FlightPredictionResponseMapper.toApi(mlResponse);
    }
}

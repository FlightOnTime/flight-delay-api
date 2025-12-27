package com.flightontime.flightontime.domain.mapper;

import com.flightontime.flightontime.api.dto.request.PredictionRequest;
import com.flightontime.flightontime.domain.model.FlightPredictionRequest;

import java.time.LocalDateTime;

public class FlightMlMapper {

    private FlightMlMapper() {}

    public static FlightPredictionRequest toMl(PredictionRequest dto) {


        LocalDateTime date = dto.getDepartureDate();

        return new FlightPredictionRequest(
                dto.getCarrier().getCodigo(),
                dto.getOrigin(),
                dto.getDestination(),
                date.getDayOfWeek().getValue(),
                date.getHour() * 100 + date.getMinute(),
                dto.getDistanceMiles(),
                dto.getOriginDelayRate(),
                dto.getCarrierDelayRate(),
                dto.getAccumulatedAirportTraffic()
        );
    }
}
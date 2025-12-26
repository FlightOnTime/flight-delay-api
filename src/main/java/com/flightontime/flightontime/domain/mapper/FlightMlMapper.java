package com.flightontime.flightontime.domain.mapper;

import com.flightontime.flightontime.api.dto.request.PredictionRequest;
import com.flightontime.flightontime.domain.model.FlightPredictionRequest;

public class FlightMlMapper {

    private FlightMlMapper() {}

    public static FlightPredictionRequest toMl(PredictionRequest dto) {

        FlightPredictionRequest ml = new FlightPredictionRequest();

        // PT -> EN
        ml.setCarrier(dto.getCarrier());
        ml.setOrigin(dto.getOrigin());
        ml.setDest(dto.getDestination());

        // data -> dayOfWeek (1=Mon ... 7=Sun)
        ml.setDayOfWeek(dto.getDepartureDate().getDayOfWeek().getValue());

        // horário -> HHmm
        ml.setCrsDepTime(
                dto.getDepartureDate().getHour() * 100
                        + dto.getDepartureDate().getMinute()
        );

        ml.setDistance(dto.getDistanceMiles());

        // taxas já vêm prontas
        ml.setOriginDelayRate(dto.getAirportDelayRate());
        ml.setCarrierDelayRate(dto.getAirlineDelayRate());

        // tráfego (double -> int)
        ml.setOriginTraffic((int) dto.getAccumulatedAirportTraffic());

        return ml;
    }
}

package com.flightontime.flightontime.domain.mapper;

import com.flightontime.flightontime.api.dto.request.PredictionRequest;
import com.flightontime.flightontime.domain.model.FlightPredictionRequest;

public class FlightMlMapper {

    private static final double KM_TO_MILES = 0.621371;

    private FlightMlMapper() {}

    public static FlightPredictionRequest toMl(PredictionRequest dto) {

        FlightPredictionRequest ml = new FlightPredictionRequest();

        // PT -> EN
        ml.setCarrier(dto.getCompanhia());
        ml.setOrigin(dto.getOrigemAeroporto());
        ml.setDest(dto.getDestinoAeroporto());

        // data -> dayOfWeek (1=Mon ... 7=Sun)
        ml.setDayOfWeek(dto.getDataPartida().getDayOfWeek().getValue());

        // horário -> HHmm
        ml.setCrsDepTime(
                dto.getDataPartida().getHour() * 100
                        + dto.getDataPartida().getMinute()
        );

        // km -> milhas
        ml.setDistance(dto.getDistanciakm() * KM_TO_MILES);

        // taxas já vêm prontas
        ml.setOriginDelayRate(dto.getTaxaAtrasoAeroporto());
        ml.setCarrierDelayRate(dto.getTaxaAtrasoCompanhia());

        // tráfego (double -> int)
        ml.setOriginTraffic((int) dto.getTraficoAcumuladoAeroporto());

        return ml;
    }
}

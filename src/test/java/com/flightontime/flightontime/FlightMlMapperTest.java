package com.flightontime.flightontime;

import com.flightontime.flightontime.api.dto.request.PredictionRequest;
import com.flightontime.flightontime.domain.enums.Airline;
import com.flightontime.flightontime.domain.mapper.FlightMlMapper;
import com.flightontime.flightontime.domain.model.FlightPredictionRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FlightMlMapperTest {

    @Test
    void deveMapearPredictionRequestParaFlightMlRequest() {

        PredictionRequest dto = PredictionRequest.builder()
                .carrier(Airline.GOL_DEVELOPMENTS)
                .origin("VCP")
                .destination("REC")
                .flightNumber("G3-1234")
                .departureDate(LocalDateTime.of(2025, 3, 18, 16, 20))
                .distanceMiles(2120.0)
                .dayOfWeek(2)
                .originDelayRate(0.0)
                .carrierDelayRate(0.0)
                .isHoliday(0)
                .accumulatedAirportTraffic(0)
                .build();

        FlightPredictionRequest ml = FlightMlMapper.toMl(dto);

        assertAll(
                () -> assertEquals("G4", ml.getCarrier()), // IATA
                () -> assertEquals("VCP", ml.getOrigin()),
                () -> assertEquals("REC", ml.getDest()),
                () -> assertEquals(2, ml.getDayOfWeek()), // terça
                () -> assertEquals(1620, ml.getCrsDepTime()),
                () -> assertEquals(2120.0, ml.getDistance(), 0.01),
                () -> assertEquals(0, ml.getOriginDelayRate()),
                () -> assertEquals(0.0, ml.getCarrierDelayRate()),
                () -> assertEquals(0, ml.getOriginTraffic()));
    }
}

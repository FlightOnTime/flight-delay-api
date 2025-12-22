package com.flightontime.flightontime;

import com.flightontime.flightontime.api.dto.request.PredictionRequest;
import com.flightontime.flightontime.domain.mapper.FlightMlMapper;
import com.flightontime.flightontime.domain.model.FlightPredictionRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FlightMlMapperTest {
    @Test
    void deveMapearPredictionRequestParaFlightMlRequest() {

        PredictionRequest dto = new PredictionRequest(
                "AZUL",
                "VCP",
                "REC",
                LocalDateTime.of(2025, 3, 18, 16, 20),
                2120.0,
                0.40,
                0.28,
                2500
        );

        FlightPredictionRequest ml = FlightMlMapper.toMl(dto);

        assertAll(
                () -> assertEquals("AZUL", ml.getCarrier()),
                () -> assertEquals("VCP", ml.getOrigin()),
                () -> assertEquals("REC", ml.getDest()),
                () -> assertEquals(2, ml.getDayOfWeek()),   // terça
                () -> assertEquals(1620, ml.getCrsDepTime()),
                () -> assertEquals(2120.0, ml.getDistance(), 0.01),
                () -> assertEquals(0.40, ml.getOriginDelayRate()),
                () -> assertEquals(0.28, ml.getCarrierDelayRate()),
                () -> assertEquals(2500, ml.getOriginTraffic())
        );
    }
}

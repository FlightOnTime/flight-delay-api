package com.flightontime.flightontime;

import com.flightontime.flightontime.domain.mapper.FlightPredictionResponseMapper;
import com.flightontime.flightontime.domain.model.FlightPredictionResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FlightPredictionResponseMapperTest {

    @Test
    void deveCopiarResponseDoMlParaApi() {

        FlightPredictionResponse ml = new FlightPredictionResponse();
        ml.setPrevisao("ATRASADO");
        ml.setProbabilidadeAtraso(0.78);
        ml.setConfianca("MEDIA");
        ml.setPrincipaisFatores(
                List.of("Alta taxa de atraso", "Horário de pico")
        );
        ml.setRecomendacoes(
                List.of("Chegar mais cedo", "Monitorar status")
        );

        FlightPredictionResponse api =
                FlightPredictionResponseMapper.fromMl(ml);

        assertAll(
                () -> assertEquals("ATRASADO", api.getPrevisao()),
                () -> assertEquals(0.78, api.getProbabilidadeAtraso()),
                () -> assertEquals("MEDIA", api.getConfianca()),
                () -> assertEquals(2, api.getPrincipaisFatores().size()),
                () -> assertEquals(2, api.getRecomendacoes().size())
        );
    }
}

package com.flightontime.flightontime;

import com.flightontime.flightontime.api.dto.response.PredictionResponse;
import com.flightontime.flightontime.domain.mapper.FlightPredictionResponseMapper;
import com.flightontime.flightontime.domain.model.FlightPredictionResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FlightPredictionResponseMapperTest {

    @Test
    void deveMapearResponseDoMlParaApiComApenasDoisCampos() {

        FlightPredictionResponse ml = new FlightPredictionResponse();
        ml.setPrevisao("ATRASADO");
        ml.setProbabilidadeAtraso(0.78);
        ml.setConfianca("MEDIA"); // ignorado
        ml.setPrincipaisFatores(null); // ignorado
        ml.setRecomendacoes(null); // ignorado

        PredictionResponse api =
                FlightPredictionResponseMapper.toApi(ml);

        assertAll(
                () -> assertEquals("ATRASADO", api.getPrevisao()),
                () -> assertEquals(0.78, api.getProbabilidade())
        );
    }
}

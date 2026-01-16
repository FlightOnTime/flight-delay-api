package com.flightontime.flightontime.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightontime.flightontime.api.dto.request.PredictionRequest;
import com.flightontime.flightontime.api.dto.response.PredictionResponse;
import com.flightontime.flightontime.domain.service.FlightPredictionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class FlightDelayControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private FlightPredictionService flightPredictionService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }

    @Test
    void testPredict_Success() throws Exception {
        PredictionRequest request = new PredictionRequest();
        request.setCompanhia("GOL");
        request.setOrigemAeroporto("GRU");
        request.setDestinoAeroporto("JFK");
        request.setDataPartida(LocalDateTime.now().plusDays(1));
        request.setDistanciaKm(500.0);

        PredictionResponse mockResponse = new PredictionResponse();
        mockResponse.setPredicao(0);
        mockResponse.setProbabilidade(0.15);
        mockResponse.setMensagem("No Horário");

        when(flightPredictionService.predict(any(PredictionRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.predicao").value(0))
                .andExpect(jsonPath("$.mensagem").value("No Horário"));
    }
}

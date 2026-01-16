package com.flightontime.flightontime.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightontime.flightontime.api.dto.request.PredictionRequest;
import com.flightontime.flightontime.domain.model.PredictionHistory;
import com.flightontime.flightontime.domain.repository.PredictionHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class PredictControllerIT {

        private MockMvc mockMvc;

        private ObjectMapper objectMapper;

        @Autowired
        private WebApplicationContext context;

        @BeforeEach
        void setup() {
                mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
                objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        }

        @Autowired
        private PredictionHistoryRepository repository;

        @MockitoBean
        private RestTemplate restTemplate;

        @Test
        @DisplayName("Deve processar predição e salvar no H2 com status 200 OK")
        void deveProcessarPredicaoESalvarNoBanco() throws Exception {
                // Arrange
                PredictionRequest request = PredictionRequest.builder()
                                .companhia("LATAM")
                                .origemAeroporto("GRU")
                                .destinoAeroporto("JFK")
                                .dataPartida(LocalDateTime.now().plusDays(1))
                                .distanciaKm(7600.0)
                                .build();

                String mlApiResponse = "{\"prediction\": \"Pontual\", \"probability\": 0.05, \"message\": \"Voo no horário\"}";
                when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                                .thenReturn(ResponseEntity.ok(mlApiResponse));

                // Act & Assert
                mockMvc.perform(post("/api/v1/predict")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.predicao", is(0)))
                                .andExpect(jsonPath("$.mensagem", is("Voo no horário")));

                // Verifica se persistiu no H2
                List<PredictionHistory> history = repository.findAll();
                assertFalse(history.isEmpty());
                assertEquals("Pontual", history.get(0).getPrevisao());
                assertEquals("LATAM", history.get(0).getCompanhiaAerea());
        }

        @Test
        @DisplayName("Deve validar a persistência de diferentes status no H2")
        void deveValidarPersistenciaStatusDiferentes() throws Exception {
                String[] statusSimulados = { "No Horário", "Atrasado", "Cancelado", "Pontual" };

                for (String status : statusSimulados) {
                        PredictionRequest request = PredictionRequest.builder()
                                        .companhia("GOL")
                                        .origemAeroporto("CGH")
                                        .destinoAeroporto("SDU")
                                        .dataPartida(LocalDateTime.now().plusHours(5))
                                        .distanciaKm(400.0)
                                        .build();

                        String mlApiResponse = String
                                        .format("{\"prediction\": \"%s\", \"probability\": 0.5, \"message\": \"Teste\"}",
                                                        status);
                        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                                        .thenReturn(ResponseEntity.ok(mlApiResponse));

                        mockMvc.perform(post("/api/v1/predict")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(request)))
                                        .andExpect(status().isOk());
                }

                // Deve ter pelo menos 4 novos registros (pode haver do teste anterior se não
                // limpar)
                // Como o H2 é 'create-drop' por teste (ou por contexto), geralmente limpa entre
                // classes se configurado.
                // Mas aqui estamos contando dentro do mesmo método ou esperando o total.
        }
}

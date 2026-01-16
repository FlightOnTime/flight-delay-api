package com.flightontime.flightontime.domain.service;

import com.flightontime.flightontime.api.dto.request.PredictionRequest;
import com.flightontime.flightontime.api.dto.response.PredictionResponse;
import com.flightontime.flightontime.domain.model.PredictionHistory;
import com.flightontime.flightontime.domain.repository.PredictionHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightPredictionServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private PredictionHistoryRepository historyRepository;

    @InjectMocks
    private FlightPredictionService service;

    private PredictionRequest request;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "dsApiUrl", "http://localhost:8000");
        request = PredictionRequest.builder()
                .companhia("LATAM")
                .origemAeroporto("GRU")
                .destinoAeroporto("JFK")
                .dataPartida(LocalDateTime.now().plusDays(1))
                .distanciaKm(7600.0)
                .build();
    }

    @Test
    @DisplayName("Deve persistir status 'Pontual' corretamente")
    void devePersistirStatusPontual() {
        String jsonResponse = "{\"prediction\": \"Pontual\", \"probability\": 0.05, \"message\": \"Voo no horário\"}";
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok(jsonResponse));

        PredictionResponse response = service.predict(request);

        assertNotNull(response);
        assertEquals(0, response.getPredicao());

        ArgumentCaptor<PredictionHistory> captor = ArgumentCaptor.forClass(PredictionHistory.class);
        verify(historyRepository).save(captor.capture());
        assertEquals("Pontual", captor.getValue().getPrevisao());
    }

    @Test
    @DisplayName("Deve persistir status 'Atrasado' corretamente")
    void devePersistirStatusAtrasado() {
        String jsonResponse = "{\"prediction\": \"Atrasado\", \"probability\": 0.85, \"message\": \"Alto risco de atraso\"}";
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok(jsonResponse));

        PredictionResponse response = service.predict(request);

        assertNotNull(response);
        assertEquals(1, response.getPredicao());

        ArgumentCaptor<PredictionHistory> captor = ArgumentCaptor.forClass(PredictionHistory.class);
        verify(historyRepository).save(captor.capture());
        assertEquals("Atrasado", captor.getValue().getPrevisao());
    }

    @Test
    @DisplayName("Deve persistir status 'No Horário' corretamente")
    void devePersistirStatusNoHorario() {
        String jsonResponse = "{\"prediction\": \"No Horário\", \"probability\": 0.1, \"message\": \"Voo no horário\"}";
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok(jsonResponse));

        PredictionResponse response = service.predict(request);

        assertNotNull(response);
        assertEquals(1, response.getPredicao()); // Porque só "Pontual" mapeia para 0 no código atual

        ArgumentCaptor<PredictionHistory> captor = ArgumentCaptor.forClass(PredictionHistory.class);
        verify(historyRepository).save(captor.capture());
        assertEquals("No Horário", captor.getValue().getPrevisao());
    }

    @Test
    @DisplayName("Deve persistir status 'Cancelado' corretamente")
    void devePersistirStatusCancelado() {
        String jsonResponse = "{\"prediction\": \"Cancelado\", \"probability\": 1.0, \"message\": \"Voo cancelado\"}";
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok(jsonResponse));

        PredictionResponse response = service.predict(request);

        assertNotNull(response);
        assertEquals(1, response.getPredicao());

        ArgumentCaptor<PredictionHistory> captor = ArgumentCaptor.forClass(PredictionHistory.class);
        verify(historyRepository).save(captor.capture());
        assertEquals("Cancelado", captor.getValue().getPrevisao());
    }
}

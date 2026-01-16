package com.flightontime.flightontime.service;

import com.flightontime.flightontime.api.dto.request.PredictionRequest;
import com.flightontime.flightontime.domain.model.PredictionHistory;
import com.flightontime.flightontime.domain.repository.PredictionHistoryRepository;
import com.flightontime.flightontime.domain.service.FlightPredictionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightDelayServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private PredictionHistoryRepository historyRepository;

    @InjectMocks
    private FlightPredictionService flightPredictionService;

    @Test
    void testPredict_Success() {
        // Arrange
        // Set ML API URL manually since @Value is not processed in pure Mockito test
        ReflectionTestUtils.setField(flightPredictionService, "dsApiUrl", "http://test-url");

        PredictionRequest request = new PredictionRequest();
        request.setCompanhia("GOL");
        request.setOrigemAeroporto("GRU");
        request.setDestinoAeroporto("JFK");
        request.setDataPartida(LocalDateTime.of(2023, 10, 10, 10, 0));
        request.setDistanciaKm(500.0);

        String dsResponseJson = "{\"prediction\": \"Pontual\", \"probability\": 0.1, \"message\": \"Voo pontual\", \"explanations\": [\"Bom tempo\"]}";

        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(dsResponseJson, HttpStatus.OK));

        // Act
        var response = flightPredictionService.predict(request);

        // Assert
        assertNotNull(response);
        assertEquals(0, response.getPredicao()); // 0 = No Horário
        assertEquals(0.1, response.getProbabilidade());

        // Verify repository save
        verify(historyRepository, times(1)).save(any(PredictionHistory.class));
    }
}

package com.flightontime.flightontime.domain.service;

import com.flightontime.flightontime.api.dto.StatsResponseDTO;
import com.flightontime.flightontime.domain.repository.PredictionHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private PredictionHistoryRepository repository;

    @InjectMocks
    private StatsService statsService;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        // Mock de dados básicos - usando lenient() para evitar
        // UnnecessaryStubbingException
        // em testes que não utilizam todos os mocks (ex: testes de cálculo matemático)
        org.mockito.Mockito.lenient().when(repository.countByPeriod(any())).thenReturn(1000L);
        org.mockito.Mockito.lenient().when(repository.countDelayedByPeriod(any())).thenReturn(420L);
        org.mockito.Mockito.lenient().when(repository.countSuccessfulByPeriod(any())).thenReturn(998L);
        org.mockito.Mockito.lenient().when(repository.avgResponseTimeByPeriod(any())).thenReturn(245.67);

        List<Object[]> mockAeroportos = new java.util.ArrayList<>();
        mockAeroportos.add(new Object[] { "JFK", 100L, 58L });
        org.mockito.Mockito.lenient().when(repository.findTopDelayedOrigins(any())).thenReturn(mockAeroportos);

        List<Object[]> mockCompanhias = new java.util.ArrayList<>();
        mockCompanhias.add(new Object[] { "AS", 50L, 9L });
        org.mockito.Mockito.lenient().when(repository.findTopPerformingCarriers(any())).thenReturn(mockCompanhias);
    }

    @Test
    @DisplayName("Deve calcular estatísticas das últimas 24 horas corretamente")
    void deveCalcularEstatisticas24Horas() {
        // Act
        StatsResponseDTO stats = statsService.getStats24Hours();

        // Assert
        assertNotNull(stats);
        assertEquals("Últimas 24 horas", stats.getPeriodo());
        assertEquals(1000L, stats.getTotalPrevisoes());
        assertEquals(new BigDecimal("42.00"), stats.getTaxaAtrasoPrevisto());
        assertEquals(new BigDecimal("99.80"), stats.getTaxaSucessoApi());
    }

    @Test
    @DisplayName("Deve calcular estatísticas dos últimos 7 dias corretamente")
    void deveCalcularEstatisticas7Dias() {
        // Act
        StatsResponseDTO stats = statsService.getStats7Days();

        // Assert
        assertNotNull(stats);
        assertEquals("Últimos 7 dias", stats.getPeriodo());
        assertEquals(1000L, stats.getTotalPrevisoes());
    }

    @Test
    @DisplayName("Deve calcular estatísticas dos últimos 30 dias corretamente")
    void deveCalcularEstatisticas30Dias() {
        // Act
        StatsResponseDTO stats = statsService.getStats30Days();

        // Assert
        assertNotNull(stats);
        assertEquals("Últimos 30 dias", stats.getPeriodo());
    }

    @Test
    @DisplayName("Deve retornar Top 5 aeroportos mais problemáticos")
    void deveRetornarTop5AeroportosMaisProblematicos() {
        // Act
        StatsResponseDTO stats = statsService.getStats24Hours();

        // Assert
        assertFalse(stats.getAeroportosMaisProblematicos().isEmpty());
        assertEquals("JFK", stats.getAeroportosMaisProblematicos().get(0).getCodigoIata());
        assertEquals(100L, stats.getAeroportosMaisProblematicos().get(0).getTotalVoos());
        assertEquals(new BigDecimal("58.00"),
                stats.getAeroportosMaisProblematicos().get(0).getTaxaAtraso());
    }

    @Test
    @DisplayName("Deve retornar Top 5 companhias com melhor desempenho")
    void deveRetornarTop5CompanhiasMelhorDesempenho() {
        // Act
        StatsResponseDTO stats = statsService.getStats24Hours();

        // Assert
        assertFalse(stats.getCompanhiasMelhorDesempenho().isEmpty());
        assertEquals("AS", stats.getCompanhiasMelhorDesempenho().get(0).getCodigoIata());
        assertEquals(50L, stats.getCompanhiasMelhorDesempenho().get(0).getTotalVoos());
        assertEquals(new BigDecimal("18.00"),
                stats.getCompanhiasMelhorDesempenho().get(0).getTaxaAtraso());
    }

    @Test
    @DisplayName("Deve retornar zero quando nenhuma previsão realizada")
    void deveRetornarZeroQuandoNenhumaPrevisao() {
        // Arrange
        when(repository.countByPeriod(any())).thenReturn(0L);
        when(repository.countDelayedByPeriod(any())).thenReturn(0L);
        when(repository.countSuccessfulByPeriod(any())).thenReturn(0L);
        when(repository.avgResponseTimeByPeriod(any())).thenReturn(null);
        when(repository.findTopDelayedOrigins(any())).thenReturn(Collections.emptyList());
        when(repository.findTopPerformingCarriers(any())).thenReturn(Collections.emptyList());

        // Act
        StatsResponseDTO stats = statsService.getStats24Hours();

        // Assert
        assertEquals(BigDecimal.ZERO, stats.getTaxaAtrasoPrevisto());
        assertEquals(BigDecimal.ZERO, stats.getTaxaSucessoApi());
        assertEquals(BigDecimal.ZERO, stats.getTempoRespostaMedioMs());
        assertTrue(stats.getAeroportosMaisProblematicos().isEmpty());
        assertTrue(stats.getCompanhiasMelhorDesempenho().isEmpty());
    }

    @Test
    @DisplayName("Deve calcular percentual corretamente")
    void deveCalcularPercentualCorretamente() {
        // Test internal calculation method directly
        BigDecimal result = statsService.calculatePercentage(50L, 100L);
        assertEquals(new BigDecimal("50.00"), result);

        result = statsService.calculatePercentage(1L, 3L);
        assertEquals(new BigDecimal("33.33"), result);

        result = statsService.calculatePercentage(0L, 100L);
        assertEquals(new BigDecimal("0.00"), result);
    }

    @Test
    @DisplayName("Deve retornar zero quando total é zero ou null")
    void deveRetornarZeroQuandoTotalZeroOuNull() {
        // Test edge cases for percentage calculation
        BigDecimal result = statsService.calculatePercentage(50L, 0L);
        assertEquals(BigDecimal.ZERO, result);

        result = statsService.calculatePercentage(50L, null);
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @DisplayName("Deve incluir ultima atualização no resultado")
    void deveIncluirUltimaAtualizacao() {
        // Act
        StatsResponseDTO stats = statsService.getStats24Hours();

        // Assert
        assertNotNull(stats.getUltimaAtualizacao());
    }

    @Test
    @DisplayName("Deve calcular tempo médio de resposta corretamente")
    void deveCalcularTempoMedioRespostaCorretamente() {
        // Act
        StatsResponseDTO stats = statsService.getStats24Hours();

        // Assert
        assertEquals(new BigDecimal("245.67"), stats.getTempoRespostaMedioMs());
    }
}

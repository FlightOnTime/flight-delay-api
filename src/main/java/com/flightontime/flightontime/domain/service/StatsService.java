package com.flightontime.flightontime.domain.service;

import com.flightontime.flightontime.api.dto.StatsResponseDTO;
import com.flightontime.flightontime.api.dto.StatsResponseDTO.AeroportoStatDTO;
import com.flightontime.flightontime.api.dto.StatsResponseDTO.CompanhiaStatDTO;
import com.flightontime.flightontime.domain.repository.PredictionHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsService {

        private final PredictionHistoryRepository repository;

        /**
         * Retorna estatísticas das últimas 24 horas
         */
        public StatsResponseDTO getStats24Hours() {
                return getStatsByPeriod(LocalDateTime.now().minusHours(24), "Últimas 24 horas");
        }

        /**
         * Retorna estatísticas dos últimos 7 dias
         */
        public StatsResponseDTO getStats7Days() {
                return getStatsByPeriod(LocalDateTime.now().minusDays(7), "Últimos 7 dias");
        }

        /**
         * Retorna estatísticas dos últimos 30 dias
         */
        public StatsResponseDTO getStats30Days() {
                return getStatsByPeriod(LocalDateTime.now().minusDays(30), "Últimos 30 dias");
        }

        /**
         * Lógica centralizada para calcular estatísticas
         */
        private StatsResponseDTO getStatsByPeriod(LocalDateTime startDate, String periodoLabel) {
                log.info("Calculando estatísticas para período: {}", periodoLabel);

                // Métricas principais
                Long totalPrevisoes = repository.countByPeriod(startDate);
                Long totalAtrasados = repository.countDelayedByPeriod(startDate);
                Long totalSucesso = repository.countSuccessfulByPeriod(startDate);
                Double avgResponseTime = repository.avgResponseTimeByPeriod(startDate);

                // Calcular taxas
                BigDecimal taxaAtraso = calculatePercentage(totalAtrasados, totalPrevisoes);
                BigDecimal taxaSucesso = calculatePercentage(totalSucesso, totalPrevisoes);
                BigDecimal tempoMedio = avgResponseTime != null
                                ? BigDecimal.valueOf(avgResponseTime).setScale(2, RoundingMode.HALF_UP)
                                : BigDecimal.ZERO;

                // Top aeroportos problemáticos
                List<AeroportoStatDTO> aeroportos = repository.findTopDelayedOrigins(startDate)
                                .stream()
                                .limit(5)
                                .map(row -> AeroportoStatDTO.builder()
                                                .codigoIata((String) row[0])
                                                .totalVoos(((Number) row[1]).longValue())
                                                .taxaAtraso(calculatePercentage(
                                                                ((Number) row[2]).longValue(),
                                                                ((Number) row[1]).longValue()))
                                                .build())
                                .collect(Collectors.toList());

                // Top companhias com melhor desempenho
                List<CompanhiaStatDTO> companhias = repository.findTopPerformingCarriers(startDate)
                                .stream()
                                .limit(5)
                                .map(row -> CompanhiaStatDTO.builder()
                                                .codigoIata((String) row[0])
                                                .totalVoos(((Number) row[1]).longValue())
                                                .taxaAtraso(calculatePercentage(
                                                                ((Number) row[2]).longValue(),
                                                                ((Number) row[1]).longValue()))
                                                .build())
                                .collect(Collectors.toList());

                log.info("Estatísticas calculadas: {} previsões, taxa atraso {}%",
                                totalPrevisoes, taxaAtraso);

                return StatsResponseDTO.builder()
                                .periodo(periodoLabel)
                                .totalPrevisoes(totalPrevisoes)
                                .taxaAtrasoPrevisto(taxaAtraso)
                                .taxaSucessoApi(taxaSucesso)
                                .tempoRespostaMedioMs(tempoMedio)
                                .aeroportosMaisProblematicos(aeroportos)
                                .companhiasMelhorDesempenho(companhias)
                                .ultimaAtualizacao(LocalDateTime.now())
                                .build();
        }

        /**
         * Calcula percentual com 2 casas decimais
         */
        public BigDecimal calculatePercentage(Long part, Long total) {
                if (total == null || total == 0) {
                        return BigDecimal.ZERO;
                }
                return BigDecimal.valueOf(part)
                                .multiply(BigDecimal.valueOf(100))
                                .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
        }
}

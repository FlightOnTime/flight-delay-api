package com.flightontime.flightontime.domain.repository;

import com.flightontime.flightontime.domain.model.PredictionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PredictionHistoryRepository
                extends JpaRepository<PredictionHistory, Long> {

        /**
         * Conta total de previsões em um período
         */
        @Query("SELECT COUNT(p) FROM PredictionHistory p WHERE p.requestAt >= :startDate")
        Long countByPeriod(@Param("startDate") LocalDateTime startDate);

        /**
         * Conta previsões com atraso previsto em um período
         */
        @Query("SELECT COUNT(p) FROM PredictionHistory p " +
                        "WHERE p.requestAt >= :startDate AND p.atrasoPrevisto = true")
        Long countDelayedByPeriod(@Param("startDate") LocalDateTime startDate);

        /**
         * Conta requisições bem-sucedidas em um período
         */
        @Query("SELECT COUNT(p) FROM PredictionHistory p " +
                        "WHERE p.requestAt >= :startDate AND p.status = true")
        Long countSuccessfulByPeriod(@Param("startDate") LocalDateTime startDate);

        /**
         * Calcula tempo médio de resposta em um período
         */
        @Query("SELECT AVG(p.tempoRespostaMs) FROM PredictionHistory p " +
                        "WHERE p.requestAt >= :startDate AND p.status = true")
        Double avgResponseTimeByPeriod(@Param("startDate") LocalDateTime startDate);

        /**
         * Top 5 aeroportos de origem com mais atrasos
         */
        @Query("SELECT p.origemAeroporto as airport, COUNT(p) as total, " +
                        "SUM(CASE WHEN p.atrasoPrevisto = true THEN 1 ELSE 0 END) as delayed " +
                        "FROM PredictionHistory p " +
                        "WHERE p.requestAt >= :startDate " +
                        "GROUP BY p.origemAeroporto " +
                        "ORDER BY (SUM(CASE WHEN p.atrasoPrevisto = true THEN 1 ELSE 0 END) * 1.0 / COUNT(p)) DESC")
        List<Object[]> findTopDelayedOrigins(@Param("startDate") LocalDateTime startDate);

        /**
         * Top 5 companhias com melhor desempenho (menor taxa de atraso)
         */
        @Query("SELECT p.companhiaAerea as carrier, COUNT(p) as total, " +
                        "SUM(CASE WHEN p.atrasoPrevisto = true THEN 1 ELSE 0 END) as delayed " +
                        "FROM PredictionHistory p " +
                        "WHERE p.requestAt >= :startDate " +
                        "GROUP BY p.companhiaAerea " +
                        "HAVING COUNT(p) >= 10 " +
                        "ORDER BY (SUM(CASE WHEN p.atrasoPrevisto = true THEN 1 ELSE 0 END) * 1.0 / COUNT(p)) ASC")
        List<Object[]> findTopPerformingCarriers(@Param("startDate") LocalDateTime startDate);
}

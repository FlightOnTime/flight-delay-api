package com.flightontime.flightontime.domain.repository;

import com.flightontime.flightontime.domain.model.PredictionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PredictionHistoryRepository
        extends JpaRepository<PredictionHistory, Long> {
}

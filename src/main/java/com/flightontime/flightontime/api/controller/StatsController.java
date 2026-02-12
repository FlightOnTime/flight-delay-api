package com.flightontime.flightontime.api.controller;

import com.flightontime.flightontime.api.dto.StatsResponseDTO;
import com.flightontime.flightontime.domain.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/stats")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Estatísticas", description = "Endpoints para consulta de estatísticas agregadas")
public class StatsController {

    private final StatsService statsService;

    @GetMapping
    @Operation(summary = "Obter estatísticas de previsões", description = "Retorna estatísticas agregadas das previsões realizadas em diferentes períodos. "
            +
            "Por padrão retorna dados das últimas 24 horas.")
    @ApiResponse(responseCode = "200", description = "Estatísticas calculadas com sucesso")
    public ResponseEntity<StatsResponseDTO> getStats(
            @Parameter(description = "Período de análise: 24h, 7d ou 30d", example = "24h") @RequestParam(defaultValue = "24h") String periodo) {
        log.info("GET /api/v1/stats?periodo={}", periodo);

        StatsResponseDTO stats = switch (periodo.toLowerCase()) {
            case "7d" -> statsService.getStats7Days();
            case "30d" -> statsService.getStats30Days();
            default -> statsService.getStats24Hours();
        };

        return ResponseEntity.ok(stats);
    }
}

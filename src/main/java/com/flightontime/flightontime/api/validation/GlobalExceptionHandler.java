package com.flightontime.flightontime.api.validation;

import com.flightontime.flightontime.api.exception.InvalidAirportException;
import com.flightontime.flightontime.api.exception.InvalidCarrierException;
import com.flightontime.flightontime.api.exception.MlServiceUnavailableException;
import com.flightontime.flightontime.api.exception.ModelNotLoadedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {

                List<String> errors = ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(f -> f.getDefaultMessage())
                        .collect(Collectors.toList());

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ApiErrorResponse(400, "VALIDATION_ERROR", errors));
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ApiErrorResponse> handleInvalidFormat(HttpMessageNotReadableException ex) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ApiErrorResponse(
                                400,
                                "VALIDATION_ERROR",
                                List.of("Formato do campo inválido")
                        ));
        }

        @ExceptionHandler(InvalidAirportException.class)
        public ResponseEntity<ApiErrorResponse> handleInvalidAirport(InvalidAirportException ex) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ApiErrorResponse(
                                400,
                                "INVALID_AIRPORT",
                                List.of(ex.getMessage())
                        ));
        }

        @ExceptionHandler(InvalidCarrierException.class)
        public ResponseEntity<ApiErrorResponse> handleInvalidCarrier(InvalidCarrierException ex) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ApiErrorResponse(
                                400,
                                "INVALID_CARRIER",
                                List.of(ex.getMessage())
                        ));
        }

        @ExceptionHandler(ModelNotLoadedException.class)
        public ResponseEntity<ApiErrorResponse> handleModelNotLoaded(ModelNotLoadedException ex) {
                return ResponseEntity
                        .status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(new ApiErrorResponse(
                                503,
                                "MODEL_NOT_LOADED",
                                List.of(ex.getMessage())
                        ));
        }

        @ExceptionHandler(MlServiceUnavailableException.class)
        public ResponseEntity<ApiErrorResponse> handleMlServiceUnavailable(MlServiceUnavailableException ex) {
                return ResponseEntity
                        .status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(new ApiErrorResponse(
                                503,
                                "ML_SERVICE_UNAVAILABLE",
                                List.of(ex.getMessage())
                        ));
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ApiErrorResponse(
                                400,
                                "BUSINESS_ERROR",
                                List.of(ex.getMessage())
                        ));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex) {
                ex.printStackTrace();
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiErrorResponse(
                                500,
                                "INTERNAL_ERROR",
                                List.of("Erro interno inesperado: " + ex.getMessage())
                        ));
        }
}

package com.flightontime.flightontime.api.validation;

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
                                .map(f -> "Campo " + f.getField() + " " + f.getDefaultMessage())
                                .collect(Collectors.toList());

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(new ApiErrorResponse(400, "VALIDATION_ERROR", errors));
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(new ApiErrorResponse(400, "BUSINESS_ERROR", List.of(ex.getMessage())));
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ApiErrorResponse> handleInvalidFormat(HttpMessageNotReadableException ex) {
                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(new ApiErrorResponse(
                                                400,
                                                "VALIDATION_ERROR",
                                                List.of("Campo inválido ou formato incorreto: "
                                                                + ex.getMostSpecificCause().getMessage())));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex) {
                ex.printStackTrace();
                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ApiErrorResponse(500, "INTERNAL_ERROR",
                                                List.of("Erro interno inesperado: " + ex.getMessage())));
        }
}

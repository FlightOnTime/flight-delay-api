package com.flightontime.flightontime.api.exception;

public class MlServiceUnavailableException extends RuntimeException {
    public MlServiceUnavailableException(String message) {
        super(message);
    }
}

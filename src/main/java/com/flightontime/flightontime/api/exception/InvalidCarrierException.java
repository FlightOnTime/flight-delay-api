package com.flightontime.flightontime.api.exception;

public class InvalidCarrierException extends RuntimeException{
    public InvalidCarrierException(String message) {
        super(message);
    }
}

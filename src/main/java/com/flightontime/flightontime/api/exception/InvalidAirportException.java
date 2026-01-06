package com.flightontime.flightontime.api.exception;

public class InvalidAirportException extends RuntimeException{
    public InvalidAirportException(String message) {
        super(message);
    }
}

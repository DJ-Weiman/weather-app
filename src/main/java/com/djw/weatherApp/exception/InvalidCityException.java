package com.djw.weatherApp.exception;

public class InvalidCityException extends RuntimeException {
    public InvalidCityException(String message) {
        super(message);
    }
}

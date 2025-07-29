package com.djw.weatherApp.exception;

public class OpenWeatherMapApiException extends RuntimeException{

    public OpenWeatherMapApiException(String message){
        super(message);
    }
}

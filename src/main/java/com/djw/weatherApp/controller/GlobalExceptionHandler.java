package com.djw.weatherApp.controller;

import com.djw.weatherApp.domain.dto.APIErrorResponse;
import com.djw.weatherApp.exception.InvalidCityException;
import com.djw.weatherApp.exception.OpenWeatherMapApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OpenWeatherMapApiException.class)
    public ResponseEntity<APIErrorResponse> handleOpenWeatherApiException(OpenWeatherMapApiException ex){
        HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
        APIErrorResponse errorResponse = APIErrorResponse.builder()
                .status(status.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(InvalidCityException.class)
    public ResponseEntity<APIErrorResponse> handleInvalidCityException(InvalidCityException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        APIErrorResponse errorResponse = APIErrorResponse.builder()
                .status(status.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, status);
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<APIErrorResponse> handleRuntimeException(RuntimeException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        APIErrorResponse errorResponse = APIErrorResponse.builder()
                .status(status.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, status);
    }
}

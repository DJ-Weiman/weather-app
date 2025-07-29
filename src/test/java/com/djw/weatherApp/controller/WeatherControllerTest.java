package com.djw.weatherApp.controller;

import com.djw.weatherApp.domain.dto.WeatherSummaryDTO;
import com.djw.weatherApp.exception.InvalidCityException;
import com.djw.weatherApp.exception.OpenWeatherMapApiException;
import com.djw.weatherApp.service.WeatherService;
import com.djw.weatherApp.utils.CityNameValidator;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherControllerTest {

    @Mock
    private WeatherService weatherService;

    @Mock
    private CityNameValidator cityNameValidator;

    @InjectMocks
    private WeatherController weatherController;

    private WeatherSummaryDTO mockSummaryDTO;

    @BeforeEach
    void setUp() {
        // Initialize a common mock response for successful scenarios
        mockSummaryDTO = new WeatherSummaryDTO(
                "Colombo",
                26.81,
                "2025-07-29",
                "2025-08-02"
        );
    }

    @Test
    void getWeatherSummary_ValidCity_ReturnsOk() throws ExecutionException, InterruptedException {
        when(weatherService.getWeatherSummaryForCity("Colombo"))
                .thenReturn(CompletableFuture.completedFuture(mockSummaryDTO));

        CompletableFuture<ResponseEntity<WeatherSummaryDTO>> futureResponse =
                weatherController.getWeatherSummaryForCity("Colombo");

        ResponseEntity<WeatherSummaryDTO> response = futureResponse.get();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockSummaryDTO, response.getBody());

        verify(weatherService, times(1)).getWeatherSummaryForCity("Colombo");
    }

    @Test
    void getWeatherSummary_NullCity_ThrowsInvalidCityException() {
        doThrow(new InvalidCityException("City name cannot be null or empty."))
                .when(cityNameValidator).validateCityName(null);

        InvalidCityException thrown = assertThrows(InvalidCityException.class, () -> {
            weatherController.getWeatherSummaryForCity(null);
        });
        assertEquals("City name cannot be null or empty.", thrown.getMessage());

        verifyNoInteractions(weatherService);
    }

    @Test
    void getWeatherSummary_EmptyCity_ThrowsInvalidCityException() {
        doThrow(new InvalidCityException("City name cannot be null or empty."))
                .when(cityNameValidator).validateCityName("");

        InvalidCityException thrown = assertThrows(InvalidCityException.class, () -> {
            weatherController.getWeatherSummaryForCity("");
        });
        assertEquals("City name cannot be null or empty.", thrown.getMessage());
        verifyNoInteractions(weatherService);
    }

    @Test
    void getWeatherSummary_CityWithNumbers_ThrowsInvalidCityException() {
        doThrow(new InvalidCityException("City name contains invalid characters. Only letters, spaces, hyphens, and apostrophes are allowed."))
                .when(cityNameValidator).validateCityName("Colombo123");

        InvalidCityException thrown = assertThrows(InvalidCityException.class, () -> {
            weatherController.getWeatherSummaryForCity("Colombo123");
        });
        assertEquals("City name contains invalid characters. Only letters, spaces, hyphens, and apostrophes are allowed.", thrown.getMessage());
        verifyNoInteractions(weatherService);
    }

    @Test
    void getWeatherSummary_ServiceThrowsOpenWeatherMapApiException_PropagatesException() {
        when(weatherService.getWeatherSummaryForCity("Colombo"))
                .thenReturn(CompletableFuture.failedFuture(new OpenWeatherMapApiException("API down")));

        ExecutionException thrown = assertThrows(ExecutionException.class, () -> {
            weatherController.getWeatherSummaryForCity("Colombo").get();
        });

        assertInstanceOf(OpenWeatherMapApiException.class, thrown.getCause());
        assertEquals("API down", thrown.getCause().getMessage());

        verify(weatherService, times(1)).getWeatherSummaryForCity("Colombo");
    }

    @Test
    void getWeatherSummary_ServiceThrowsGenericRuntimeException_PropagatesException() {
        when(weatherService.getWeatherSummaryForCity("Colombo"))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("Something unexpected happened")));

        ExecutionException thrown = assertThrows(ExecutionException.class, () -> {
            weatherController.getWeatherSummaryForCity("Colombo").get();
        });

        // Verify the cause of the ExecutionException
        assertInstanceOf(RuntimeException.class, thrown.getCause());
        assertEquals("Something unexpected happened", thrown.getCause().getMessage());

        verify(weatherService, times(1)).getWeatherSummaryForCity("Colombo");
    }
}
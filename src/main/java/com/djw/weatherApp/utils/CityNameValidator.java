package com.djw.weatherApp.utils;

import com.djw.weatherApp.exception.InvalidCityException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class CityNameValidator {

    private static final Pattern CITY_NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s-']+$");

    public void validateCityName(String city){
        if (city == null || city.trim().isEmpty())
            throw new InvalidCityException("City name cannot be null or empty.");

        if (!CITY_NAME_PATTERN.matcher(city).matches())
            throw new InvalidCityException("City name contains invalid characters. Only letters, spaces, hyphens, and apostrophes are allowed.");
    }
}

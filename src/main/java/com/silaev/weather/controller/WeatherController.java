package com.silaev.weather.controller;

import com.silaev.weather.dto.AverageDto;
import com.silaev.weather.dto.CityDto;
import com.silaev.weather.service.WeatherService;
import com.silaev.weather.util.DateTimeConverter;
import com.silaev.weather.version.ApiV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;

/**
 * RestController for basic REST API as per ApiV1
 */
@RestController
@ApiV1
@RequiredArgsConstructor
@Slf4j
@Validated
public class WeatherController {
    private final WeatherService weatherService;
    private final DateTimeConverter dateTimeConverter;

    @GetMapping(value = "/averages/{cityCountryName}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AverageDto> getAverageTemperaturesAndPressure(
            @NotNull @Size(min = 3, max = 100) @PathVariable("cityCountryName") String cityCountryName){
        Instant instantThreshold = dateTimeConverter.getInstantThreshold();
        log.debug("getAverageTemperaturesAndPressure, instantThreshold: {}", instantThreshold.toString());

        validateCityName(cityCountryName);
        AverageDto dto = weatherService.getAverageTemperaturesAndPressure(cityCountryName, instantThreshold);
        return ResponseEntity.ok(dto);
    }

    private void validateCityName(String cityCountryName) {
        //O(1)
        boolean anyMatch =
                weatherService.getCities().contains(new CityDto(cityCountryName));
        if (!anyMatch){
            // better create a custom validator via public @interface
            // and add it as annotation to cityCountryName path variable
            throw new ValidationException(
                    String.format("Cannot find cityCountryName: %s in a dictionary. " +
                            "Please, use REST API: %s to get a full dictionary", cityCountryName, "/cities"));
        }
    }

    @GetMapping(value = "/cities", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<CityDto>> getCities(){
        log.debug("getCities");
        return ResponseEntity.ok(weatherService.getCities());
    }
}

package com.silaev.weather.controller;

import com.silaev.weather.dto.AverageDto;
import com.silaev.weather.dto.CityDto;
import com.silaev.weather.service.WeatherService;
import com.silaev.weather.util.DateTimeConverter;
import com.silaev.weather.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class WeatherControllerTest {
    @Mock
    private WeatherService weatherService;

    @Mock
    private DateTimeConverter dateTimeConverter;

    @InjectMocks
    private WeatherController weatherController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldGetAverageTemperaturesAndPressure() {
        //GIVEN
        String cityCountryName = "London,GB";
        Instant instantThreshold = Instant.parse("2018-11-10T18:00:00Z");
        when(dateTimeConverter.getInstantThreshold())
                .thenReturn(instantThreshold);
        when(weatherService.getCities())
                .thenReturn(new HashSet<>(Collections.singletonList(new CityDto(cityCountryName))));
        BigDecimal dailyAverage = BigDecimal.valueOf(-4.77);
        BigDecimal nightlyAverage = BigDecimal.valueOf(-0.36);
        BigDecimal pressureAverage = BigDecimal.valueOf(1033.50);
        AverageDto averageDtoExpected = TestUtil.mockAverageDto(dailyAverage, nightlyAverage, pressureAverage);
        when(weatherService.getAverageTemperaturesAndPressure(cityCountryName, instantThreshold))
                .thenReturn(averageDtoExpected);

        //WHEN
        ResponseEntity<AverageDto> responseEntity =
                weatherController.getAverageTemperaturesAndPressure(cityCountryName);

        //THEN
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        AverageDto body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals(averageDtoExpected, body);
    }

    @Test(expected = ValidationException.class)
    public void shouldNotGetAverageTemperaturesAndPressure() {
        //GIVEN
        String cityCountryName = "London,GB";
        String cityCountryNameIncorrect = "London,GL";
        Instant instantThreshold = Instant.parse("2018-11-10T18:00:00Z");
        when(dateTimeConverter.getInstantThreshold())
                .thenReturn(instantThreshold);
        when(weatherService.getCities())
                .thenReturn(new HashSet<>(Collections.singletonList(new CityDto(cityCountryName))));
        //WHEN
        ResponseEntity<AverageDto> responseEntity =
                weatherController.getAverageTemperaturesAndPressure(cityCountryNameIncorrect);

        //THEN
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    public void getCities() {
        //GIVEN
        String cityCountryName = "London,GB";
        CityDto cityDto = new CityDto(cityCountryName);
        when(weatherService.getCities())
                .thenReturn(new HashSet<>(Collections.singletonList(cityDto)));

        //WHEN
        ResponseEntity<Set<CityDto>> responseEntity = weatherController.getCities();

        //THEN
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Set<CityDto> body = responseEntity.getBody();
        assertNotNull(body);
        assertFalse(body.isEmpty());
        assertEquals(1, body.size());
        assertTrue(body.contains(cityDto));
    }
}
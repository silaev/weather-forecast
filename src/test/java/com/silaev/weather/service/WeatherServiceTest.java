package com.silaev.weather.service;

import com.silaev.weather.converter.CityConverter;
import com.silaev.weather.converter.WeatherSummaryStatisticsConverter;
import com.silaev.weather.dto.AverageDto;
import com.silaev.weather.dto.CityDto;
import com.silaev.weather.dto.ForecastResponseDto;
import com.silaev.weather.util.TestUtil;
import com.silaev.weather.util.WeatherUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class WeatherServiceTest {
    public static final String APP_ID = "appId";
    public static final int LAST_DAYS = 3;
    public static final String URL = "https://api.openweathermap.org/data/2.5/forecast";
    @Mock
    private RestTemplate restTemplate;

    @Spy
    private WeatherSummaryStatisticsConverter statisticsConverter;

    @Spy
    private CityConverter cityConverter;

    @InjectMocks
    private WeatherService weatherService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        weatherService.setAppId(APP_ID);
        weatherService.setLastDays(LAST_DAYS);
        weatherService.setURL(URL);
    }

    @Test
    public void shouldGetAverageTemperaturesAndPressure() {
        //GIVEN
        String cityCountryName = "London,GB";
        Instant instantThreshold = Instant.parse("2018-11-11T18:00:00Z");
        String url = URL + "?q=" + cityCountryName + "&units=metric&appid=" + APP_ID;
        BigDecimal dailyAverage = BigDecimal.valueOf(-4.77);
        BigDecimal nightlyAverage = BigDecimal.valueOf(-1.71);
        BigDecimal pressureAverage = BigDecimal.valueOf(1034.19);
        AverageDto averageDtoExpected =
            TestUtil.mockAverageDto(dailyAverage, nightlyAverage, pressureAverage);

        ResponseEntity<ForecastResponseDto> responseEntity =
            new ResponseEntity<>(TestUtil.mockForecastResponseDto(), HttpStatus.OK);
        when(restTemplate.exchange(url, HttpMethod.GET, WeatherUtil.getHttpEntity(),
            ForecastResponseDto.class)).thenReturn(responseEntity);
        //WHEN
        AverageDto averageDtoActual =
            weatherService.getAverageTemperaturesAndPressure(cityCountryName, instantThreshold);

        //THEN
        assertNotNull(averageDtoActual);
        assertEquals(averageDtoExpected, averageDtoActual);
    }

    @Test
    public void getCities() {
        //GIVEN
        int expectedSize = 168820;

        //WHEN
        Set<CityDto> cities = weatherService.getCities();

        //THEN
        assertNotNull(cities);
        assertEquals(expectedSize, cities.size());
    }
}

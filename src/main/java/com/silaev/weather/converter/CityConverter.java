package com.silaev.weather.converter;

import com.silaev.weather.dto.AverageDto;
import com.silaev.weather.dto.CityDto;
import com.silaev.weather.model.City;
import com.silaev.weather.util.WeatherSummaryStatistics;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.MathContext;

/**
 * Converts City to CityDto.
 */
@Component
public class CityConverter implements Converter<City, CityDto> {

    @Override
    public CityDto convert(City city) {
        if (city == null) {
            return null;
        }

        return new CityDto(city.getName() + "," + city.getCountry());
    }
}

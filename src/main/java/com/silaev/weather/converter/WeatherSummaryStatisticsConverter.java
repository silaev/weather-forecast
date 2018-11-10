package com.silaev.weather.converter;

import com.silaev.weather.dto.AverageDto;
import com.silaev.weather.util.WeatherSummaryStatistics;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.MathContext;

/**
 * Converts WeatherSummaryStatisticsConverter to AverageDto.
 */
@Component
public class WeatherSummaryStatisticsConverter implements Converter<WeatherSummaryStatistics, AverageDto> {
    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL128;

    @Override
    public AverageDto convert(WeatherSummaryStatistics summaryStatistics) {
        if (summaryStatistics == null) {
            return null;
        }

        return AverageDto.builder()
                .dailyAverage(summaryStatistics.getAverageTemperatureDaily(MathContext.DECIMAL128))
                .nightlyAverage(summaryStatistics.getAverageTemperatureNightly(MathContext.DECIMAL128))
                .pressureAverage(summaryStatistics.getAveragePressure(MATH_CONTEXT))
                .build();
    }
}

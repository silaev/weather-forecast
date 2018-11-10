package com.silaev.weather.util;

import com.silaev.weather.dto.FiguresDto;
import com.silaev.weather.dto.ForecastDto;
import com.silaev.weather.model.DayPart;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collector;

@Data
@Slf4j
public class WeatherSummaryStatistics implements Consumer<ForecastDto> {

    private BigDecimal sumTemperatureDaily = WeatherUtil.BIG_DECIMAL_ZERO;
    private BigDecimal sumTemperatureNightly = WeatherUtil.BIG_DECIMAL_ZERO;
    private BigDecimal sumPressure = WeatherUtil.BIG_DECIMAL_ZERO;
    private long countDaily;
    private long countNightly;
    private long count;

    public static Collector<ForecastDto, ?, WeatherSummaryStatistics> statistics() {
        return Collector.of(WeatherSummaryStatistics::new,
                WeatherSummaryStatistics::accept,
                WeatherSummaryStatistics::merge,
                Collector.Characteristics.UNORDERED);
    }

    public void accept(ForecastDto f) {
        Objects.requireNonNull(f);
        FiguresDto figuresDto = f.getFiguresDto();
        Objects.requireNonNull(figuresDto);

        if (count == 0) {
            if (WeatherUtil.getDayPart(f.getTimestamp())== DayPart.DAILY){
                sumTemperatureDaily = figuresDto.getTemperature();
                countDaily++;
            } else {
                sumTemperatureNightly = figuresDto.getTemperature();
                countNightly++;
            }
            sumPressure = figuresDto.getPressure();
        } else {
            if (WeatherUtil.getDayPart(f.getTimestamp())== DayPart.DAILY){
                sumTemperatureDaily = WeatherUtil.setScaleAndRound(sumTemperatureDaily.add(figuresDto.getTemperature()));
                countDaily++;
            } else {
                sumTemperatureNightly = WeatherUtil.setScaleAndRound(sumTemperatureNightly.add(figuresDto.getTemperature()));
                countNightly++;
            }
            sumPressure = WeatherUtil.setScaleAndRound(sumPressure.add(figuresDto.getPressure()));
        }
        count++;
    }

    private WeatherSummaryStatistics merge(WeatherSummaryStatistics s) {
        if (s.count > 0) {
            if (count == 0) {
                count = s.getCount();
                countDaily = s.getCountDaily();
                countNightly = s.getCountNightly();
                sumTemperatureDaily = s.getSumTemperatureDaily();
                sumTemperatureNightly = s.getSumTemperatureNightly();
                sumPressure = s.getSumPressure();
            } else {
                count += s.getCount();
                countDaily += s.getCountDaily();
                countNightly += s.getCountNightly();
                sumTemperatureDaily = WeatherUtil.setScaleAndRound(sumTemperatureDaily.add(s.getSumTemperatureDaily()));
                sumTemperatureNightly = WeatherUtil.setScaleAndRound(sumTemperatureNightly.add(s.getSumTemperatureNightly()));
                sumPressure = WeatherUtil.setScaleAndRound(sumPressure.add(s.getSumPressure()));
            }
        }
        return this;
    }

    public BigDecimal getAverageTemperatureDaily(MathContext mc) {
        return countDaily < 2 ? sumTemperatureDaily :
                WeatherUtil.setScaleAndRound(sumTemperatureDaily.divide(BigDecimal.valueOf(countDaily), mc));
    }

    public BigDecimal getAverageTemperatureNightly(MathContext mc) {
        return countNightly < 2 ? sumTemperatureNightly :
                WeatherUtil.setScaleAndRound(sumTemperatureNightly.divide(BigDecimal.valueOf(countNightly), mc));
    }

    public BigDecimal getAveragePressure(MathContext mc) {
        return count < 2 ? sumPressure :
                WeatherUtil.setScaleAndRound(sumPressure.divide(BigDecimal.valueOf(count), mc));
    }
}

package com.silaev.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Dto for working with openweathermap downstream service
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastResponseDto {
    @JsonProperty(value = "cod")
    private Integer responseCode;

    @JsonProperty(value = "list")
    private List<ForecastDto> forecastDtos;
}

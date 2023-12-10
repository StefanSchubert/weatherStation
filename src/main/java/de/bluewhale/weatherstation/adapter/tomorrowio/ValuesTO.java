package de.bluewhale.weatherstation.adapter.tomorrowio;

import lombok.Data;

@Data
public class ValuesTO {
        private Double cloudBase;
        private Double cloudCeiling;
        private Double cloudCover;
        private Double dewPoint;
        private Double evapotranspiration;
        private Double freezingRainIntensity;
        private Double humidity;
        private Double iceAccumulation;
        private Double iceAccumulationLwe;
        private Double precipitationProbability;
        private Double pressureSurfaceLevel;
        private Double rainAccumulation;
        private Double rainAccumulationLwe;
        private Double rainIntensity;
        private Double sleetAccumulation;
        private Double sleetAccumulationLwe;
        private Double sleetIntensity;
        private Double snowAccumulation;
        private Double snowAccumulationLwe;
        private Double snowIntensity;
        private Double temperature;
        private Double temperatureApparent;
        private Double uvHealthConcern;
        private Double uvIndex;
        private Double visibility;
        private Integer weatherCode;
        private Double windDirection;
        private Double windGust;
        private Double windSpeed;
}

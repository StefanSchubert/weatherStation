package de.bluewhale.weatherstation;

import de.bluewhale.weatherstation.config.LocationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;


@EnableCaching
@EnableConfigurationProperties(LocationProperties.class)
@SpringBootApplication(scanBasePackages = "de.bluewhale.weatherstation")
public class WeatherStationApp {
    public static void main(String[] args) {
        SpringApplication.run(WeatherStationApp.class, args);
    }

}
package de.bluewhale.weatherstation.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "coordinates")
@Data
public class LocationProperties {

    private List<Location> locations;

    @Data
    public static class Location {
        private String name;
        private String coords;
    }
}


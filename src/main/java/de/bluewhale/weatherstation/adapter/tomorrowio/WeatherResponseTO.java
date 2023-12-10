package de.bluewhale.weatherstation.adapter.tomorrowio;

import lombok.Data;

@Data
public class WeatherResponseTO {
    private TimelinesTO timelines;
    private LocationTO location;
}

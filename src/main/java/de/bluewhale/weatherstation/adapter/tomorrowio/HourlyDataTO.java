package de.bluewhale.weatherstation.adapter.tomorrowio;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class HourlyDataTO {
        private ZonedDateTime time;
        private ValuesTO values;
}

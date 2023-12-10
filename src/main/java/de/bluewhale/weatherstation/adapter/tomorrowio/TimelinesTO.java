package de.bluewhale.weatherstation.adapter.tomorrowio;

import lombok.Data;

import java.util.List;

@Data
public class TimelinesTO {
        private List<HourlyDataTO> hourly;

}

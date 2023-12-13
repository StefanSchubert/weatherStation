package de.bluewhale.weatherstation.adapter.tomorrowio;

import de.bluewhale.weatherstation.config.LocationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class PrometheusConverterService {

    @Autowired
    LocationProperties locationProperties;

    @Autowired
    ApiDAO weatherDAO;

    @Cacheable(value = "weatherData", key = "#root.methodName")
    public String buildPrometheusStyleWeatherMetrics() {

        WeatherResponseTO responseTO;
        StringBuffer sb = new StringBuffer();
        sb.append("# Fetched using OpenAPI tomorrow.io free api weather feed. Recordings from: ");
        sb.append(new Date());
        sb.append("\n");
        sb.append("# Example https://api.tomorrow.io/v4/weather/forecast?location=42.3478,-71.0466&apikey=YOUR_API_KEY");
        sb.append("#\n");
        sb.append("# Requires tomorrow io user profile set to 'metric' measurement system\n");
        sb.append("#\n");
        sb.append("#\n");
        sb.append("# Metrics Dictionary:\n");
        sb.append("#\n");
        sb.append("# wind_speed:            measure in m/s at 10m high\n");
        sb.append("# wind_direction:        provided in 360° degrees\n");
        sb.append("# rain_accumulation:     provided in mm meaning l/m2\n");
        sb.append("# temperature:           provided in °C\n");
        sb.append("# humidity:              percentage of water vapor in the air\n");
        sb.append("# pressureSurfaceLevel:  measured in hPa\n");
        sb.append("# uvindex:               strenght of UV Radiation, check https://www.dwd.de/DE/leistungen/gefahrenindizesuvi/gefahrenindexuvi.html for details.\n");
        sb.append("#\n");
        sb.append("#\n");

        for (LocationProperties.Location location : locationProperties.getLocations()) {

            responseTO = weatherDAO.fetchCurrentWeather(location.getCoords());
            try {
                // Throttle openAPI access
                Thread.sleep(250l);
            } catch (InterruptedException e) {
                log.warn("Couldn't delay subsequent requests.");
            }

            if (responseTO != null) {

                sb.append("#\n");
                sb.append("# Data for " + location.getName());

                List<HourlyDataTO> hourlyDataList = responseTO.getTimelines().getHourly();

                // the service delivers a forecast on the next day. For recording
                // we won't the one which is for just now with a good accuracy.
                Optional<HourlyDataTO> latestHourlyData = hourlyDataList.stream()
                        .min(Comparator.comparing(HourlyDataTO::getTime));

                if (latestHourlyData.isPresent()) {
                    HourlyDataTO dataTO = latestHourlyData.get();

                    sb.append(" measured at " + dataTO.getTime());
                    sb.append("\n#\n");

                    ValuesTO values = dataTO.getValues();

                    add2StringBufferIfNotNull(sb, "wind_speed", values.getWindSpeed(), location);
                    add2StringBufferIfNotNull(sb, "wind_direction", values.getWindDirection(), location);
                    add2StringBufferIfNotNull(sb, "rain_accumulation", values.getRainAccumulation(), location);
                    add2StringBufferIfNotNull(sb, "temperature", values.getTemperature(), location);
                    add2StringBufferIfNotNull(sb, "humidity", values.getHumidity(), location);
                    add2StringBufferIfNotNull(sb, "pressureSurfaceLevel", values.getPressureSurfaceLevel(), location);
                    add2StringBufferIfNotNull(sb, "uvindex", values.getUvIndex(), location);
                } else {
                    sb.append("# No timeline retrieved");
                    sb.append("\n");
                    log.warn("Retrieved empty timelines! Remote Service malfunctioned!");
                }
            } else {
                sb.append("# ERROR: Could not access or process remote data provider.");
                sb.append("\n");
            }
        }

        return sb.toString();

    }

    private void add2StringBufferIfNotNull(StringBuffer sb, String label, Double value, LocationProperties.Location location) {
        if (value != null) {

            String[] parts = location.getCoords().split(",");
            Float latitude = Float.parseFloat(parts[0]);
            Float longitude = Float.parseFloat(parts[1]);

            String line = String.format(Locale.US, "%s{location=\"%s\",lat=%f,lon=%f} %f\n",
                    label, location.getName(), latitude, longitude, value);
            sb.append(line);
        }
    }

}

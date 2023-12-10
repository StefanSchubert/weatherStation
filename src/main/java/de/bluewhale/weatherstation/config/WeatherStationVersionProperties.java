package de.bluewhale.weatherstation.config;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.PropertiesPropertySource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Properties;

/**
 * Custom version property provider, e.g. for banner usage.
 */
public class WeatherStationVersionProperties implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    /** Format of a formatted version. */
    private static final String VERSION_FORMAT = "(v%s)";

    /** The APPs version. */
    private final String version;
    /** The formatted APPs version. */
    private final String formattedVersion;

    /**
     * Default constructor.
     */
    public WeatherStationVersionProperties() {
        this(WeatherStationVersionProperties.class.getPackage());
    }

    /**
     * Constructs a new APPs version based on the implementation version of a specific package.
     * @param pkg the package
     */
    WeatherStationVersionProperties(final Package pkg) {
        this.version = Optional.ofNullable(pkg.getImplementationVersion())
                .map(String::trim)
                .orElse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYYMMddHHmmss")) + "-SNAPSHOT");
        this.formattedVersion = String.format(VERSION_FORMAT, version);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onApplicationEvent(final ApplicationEnvironmentPreparedEvent event) {
        event.getEnvironment().getPropertySources().addFirst(getPropertiesPropertySource());
    }

    /**
     * Provides APPs specific version properties to be used at any point in the application.
     * @return the property source
     */
    private PropertiesPropertySource getPropertiesPropertySource() {
        final Properties props = new Properties();
        props.put("WeatherStation.version", version);
        props.put("WeatherStation.formatted-version", formattedVersion);

        return new PropertiesPropertySource(this.getClass().getSimpleName(), props);
    }
}

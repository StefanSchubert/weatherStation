package de.bluewhale.weatherstation.adapter.tomorrowio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Service
@Slf4j
public class ApiDAO {

    @Autowired
    RestTemplate restTemplate;

    @Value("${api-keys.tomorrow-io}")
    String apiKey;

    static HttpHeaders httpHeaders = null;
    final static String WEATHER_REQUEST_ENDPOINT = "https://api.tomorrow.io/v4/weather/forecast";

    public WeatherResponseTO fetchCurrentWeather(String locationCoords) {

        String endpointURL = WEATHER_REQUEST_ENDPOINT + "?location=\""+locationCoords+"\"&timesteps=1h&units=metric&apikey="+ this.apiKey;
        log.info("Requesting {}",endpointURL);
        final HttpHeaders headers = buildHttpHeader();
        final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<WeatherResponseTO> responseEntity;

        try {
            responseEntity = restTemplate.exchange(endpointURL, HttpMethod.GET, requestEntity,
                    WeatherResponseTO.class);
        } catch (RestClientException e) {
            log.error("Couldn't communicate with ClimateWeather Service. Reason was: {}",e.getMessage());
            return null;
        }
        return responseEntity.getBody();
    }

    private static HttpHeaders buildHttpHeader() {

        if (null == ApiDAO.httpHeaders) {
            final HttpHeaders headers = new HttpHeaders();
            final ArrayList<MediaType> mediaTypes = new ArrayList<MediaType>();
            mediaTypes.add(MediaType.APPLICATION_JSON);
            headers.setAccept(mediaTypes);
            headers.add("user-agent", "SpringBoot HttpClient");
            httpHeaders = headers;
        }
        return httpHeaders;
    }
}

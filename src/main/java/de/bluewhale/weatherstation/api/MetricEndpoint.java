package de.bluewhale.weatherstation.api;

import de.bluewhale.weatherstation.adapter.tomorrowio.PrometheusConverterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Slf4j
public class MetricEndpoint {

    @Autowired
    PrometheusConverterService prometheusConverterService;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/metrics",
            produces = { "text/plain" }
    )
    ResponseEntity<String> getWeatherData() {

        StringBuffer sb = new StringBuffer();
        sb.append("# Weather Metrics\n");
        sb.append("#\n");

        String scrapedWeatherData = prometheusConverterService.buildPrometheusStyleWeatherMetrics();
        sb.append(scrapedWeatherData);

        return new ResponseEntity<>(sb.toString(), HttpStatus.OK);
    }

}

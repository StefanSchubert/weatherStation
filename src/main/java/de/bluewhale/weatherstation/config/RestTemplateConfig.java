package de.bluewhale.weatherstation.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
@Slf4j
public class RestTemplateConfig {

    @Value("${http.timeout_in_secs:15}")
    private long http_timeout_in_secs;

    @Value("${http.proxy.host:null}")
    private String httpProxyHost;

    @Value("${http.proxy.port:0}")
    private Integer httpProxyPort;

    @Bean
    public RestTemplate restTemplate(ObjectMapper objectMapper) {

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);

        RestTemplate restTemplate = new RestTemplateBuilder()
                .additionalMessageConverters(converter)
                .setConnectTimeout(Duration.of(http_timeout_in_secs, ChronoUnit.SECONDS))
                .build();

        if (httpProxyHost != null && httpProxyHost.length() > 4 && httpProxyPort != null) {
            log.info("Proxyconfiguration discovered, using {}:{}", httpProxyHost, httpProxyPort);
            // Proxy inkludieren
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(httpProxyHost, httpProxyPort));
            requestFactory.setProxy(proxy);
            restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(requestFactory));
        } else {
            log.info("No Proxyconf found, continue without it.");
            restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(restTemplate.getRequestFactory()));
        }

        return restTemplate;
    }
}

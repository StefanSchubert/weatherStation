package de.bluewhale.weatherstation.config;

import tools.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;

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
    public JsonMapper jsonMapper() {
        return JsonMapper.builder().build();
    }

    @Bean
    public RestTemplate restTemplate(JsonMapper jsonMapper) {

        final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        final Duration timeout = Duration.ofSeconds(http_timeout_in_secs);
        requestFactory.setConnectTimeout(timeout);
        requestFactory.setReadTimeout(timeout);

        if (httpProxyHost != null && httpProxyHost.length() > 4 && httpProxyPort != null) {
            log.info("Proxyconfiguration discovered, using {}:{}", httpProxyHost, httpProxyPort);
            final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(httpProxyHost, httpProxyPort));
            requestFactory.setProxy(proxy);
        } else {
            log.info("No Proxyconf found, continue without it.");
        }

        final RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(requestFactory));
        restTemplate.getMessageConverters().add(0, new JacksonJsonHttpMessageConverter(jsonMapper));

        return restTemplate;
    }
}

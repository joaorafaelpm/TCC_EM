package com.pendezzapizza.pendezzapizza_api.core.redis;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Using the builder is best practice as it applies 
        // default message converters and settings
        return builder.build();
    }
}
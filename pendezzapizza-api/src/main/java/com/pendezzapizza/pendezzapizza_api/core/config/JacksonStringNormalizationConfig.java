package com.pendezzapizza.pendezzapizza_api.core.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class JacksonStringNormalizationConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer trimmingStringCustomizer() {
        return builder -> builder.deserializerByType(String.class, new StringDeserializer() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String value = super.deserialize(p, ctxt);
                if (value == null)
                    return null;
                // trim nas pontas + colapsa espaços internos múltiplos em um só
                String normalized = value.trim().replaceAll("\\s+", " ");
                return normalized.isEmpty() ? null : normalized;
            }
        });
    }
}
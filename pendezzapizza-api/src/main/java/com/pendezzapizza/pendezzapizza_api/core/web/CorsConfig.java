package com.pendezzapizza.pendezzapizza_api.core.web;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Mapeia todas as rotas da sua aplicação
                .allowedOrigins("**"); // Permite requisições da origem "http://127.0.0.1:5500"
    }

    @Bean
    public Filter shallowEtagHeaderFilter () {
        return new ShallowEtagHeaderFilter();
    }
}
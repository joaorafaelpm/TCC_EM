package com.pendezzafood.pendezzapizza.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    // Depois eu limito ainda mais as URIs de requisição e os métodos
    // Verificar isso mais tarde --> TODO: Eu posso transcrever para a minha camada do front de API e consumir do front end ao invés do back end, isso adiciona uma camada de segurança porém eu tenho medo de deixar lento!!!
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Mapeia todas as rotas da sua aplicação
                .allowedOrigins("http://localhost:3000") // Permite requisições de qualquer origem 
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite os métodos HTTP especificados
                .allowedHeaders("*"); // Permite todos os cabeçalhos
                //.allowCredentials(true); // Permite o envio de credenciais (como cookies e autenticação)
    }

}

package com.pendezzapizza.pendezzapizza_api.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ResourceServerConfig {

    @Bean
    public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Adicione liberações públicas se necessário, ex: /public/**, /v3/api-docs
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                // Habilita o login via formulário para o usuário se autenticar no Authorization Server
                .formLogin(loginFormConfigurer ->
                        loginFormConfigurer.loginPage("/login").permitAll())
                // Habilita a validação de tokens JWT para requisições de API
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> {
                            // CUIDADO: Usar localhost aqui força a aplicação a chamar a si mesma via rede.
                            // Em produção, certifique-se que a aplicação consegue se enxergar externamente.
                            jwtConfigurer.jwkSetUri("http://pendezzapizza-api:8080/oauth2/jwks");
                            jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter());
                        })
                );

        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> authorities = jwt.getClaimAsStringList("authorities");

            if (authorities == null) {
                return Collections.emptyList();
            }

            var authoritiesConverter = new JwtGrantedAuthoritiesConverter();
            Collection<GrantedAuthority> grantedAuthorities = authoritiesConverter.convert(jwt);

            grantedAuthorities.addAll(authorities
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList());

            return grantedAuthorities;
        });

        return converter;
    }
}
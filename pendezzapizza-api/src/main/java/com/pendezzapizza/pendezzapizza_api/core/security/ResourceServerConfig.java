package com.pendezzapizza.pendezzapizza_api.core.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class ResourceServerConfig {

    private final SecurityController securityController;

    @Bean
    public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Adicione liberações públicas se necessário, ex: /public/**, /v3/api-docs
                        .requestMatchers(
                                "/images/**",
                                "/css/**",
                                "/js/**",
                                "/favicon.png",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v1/users/register",
                                "/oauth2/iniciar-login",
                                "/redirect"
                        ).permitAll()
                        .requestMatchers(
                                "/HNAP1/**",
                                "/cgi/**",
                                "/loginMsg.js"
                        ).denyAll()
                        .anyRequest().authenticated()
                )
                .requestCache(cache -> cache.requestCache(requestCache()))

                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                // Habilita o login via formulário para o usuário se autenticar no Authorization Server
                .formLogin(loginFormConfigurer ->
                        loginFormConfigurer.loginPage("/login").permitAll())
                // Habilita a validação de tokens JWT para requisições de API
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer ->
                                // Agora usa o bean abaixo, que tem cache
                                jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder
                .withJwkSetUri(securityController.getUrl())
                .build();
    }

    @Bean
    public org.springframework.security.web.savedrequest.RequestCache requestCache() {
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setRequestMatcher(request ->
                !request.getRequestURI().startsWith("/.well-known/")
        );
        return requestCache;
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
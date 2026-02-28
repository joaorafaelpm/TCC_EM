package com.pendezzapizza.pendezzapizza_api.core.security.authorizationserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.pendezzapizza.pendezzapizza_api.domain.model.Permission;
import com.pendezzapizza.pendezzapizza_api.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class AuthorizationServerConfig {

    @Value("${pendezzapizza.auth.rsa-key-size:2048}")
    private static int rsaKeySize;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();
        authorizationServerConfigurer.authorizationEndpoint(
                customizer -> customizer.consentPage("/oauth2/consent"));
        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, (authorizationServer) ->
                        authorizationServer.oidc(Customizer.withDefaults())
                )
                .cors(Customizer.withDefaults())
                .formLogin(loginFormConfigurer ->
                        loginFormConfigurer.loginPage("/login").permitAll())
                .authorizeHttpRequests((authorize) ->
                        authorize
                       .anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder, JdbcOperations jdbcOperation) {
        return new JdbcRegisteredClientRepository(jdbcOperation);
    }

//        Aqui a gente pega a nossa classe do par de chaves do servidor de autenticação e monta o nosso jwt com base nisso, a classe e sua respectiva configuração é feita a partir do application.properties, onde todo o arquivo da chave está na codificação em base64 então o arquivo é o mesmo toda vez que nós reiniciamos o servidor, para facilitar no desenvolvimento, vou alterar para gerar uma chave nova a cada vez que o servidor reiniciar, para assim não manter o login quando eu reiniciar a aplicação
    @Bean
    public JWKSource<SecurityContext> jwkSource(JwtKeyStoreProperties properties) throws Exception {
        char[] keyStorePass = properties.getPassword().toCharArray();
        String keypairAlias = properties.getKeypairAlias();

        Resource jksLocation = properties.getJksLocation();
        InputStream inputStream = jksLocation.getInputStream();
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(inputStream, keyStorePass);

        RSAKey rsaKey = RSAKey.load(keyStore, keypairAlias, keyStorePass);

        return new ImmutableJWKSet<>(new JWKSet(rsaKey));
    }
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {

            // Obtém o gerador de chaves para do algorítmo RSA (é assimétrico e usa um par de chaves: pública e privada)
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

            // Inicializa o gerador com um tamanho de chave de 2048 bits
            // Quanto maior o número, mais difícil é quebrar a criptografia, mas mais lento é o processamento
            // 2048 é o padrão de segurança recomendado atualmente.
            keyPairGenerator.initialize(rsaKeySize);

            // Gera o par de chaves pública e privada
            keyPair = keyPairGenerator.generateKeyPair();

        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao gerar chaves RSA", ex);
        }
        return keyPair;
    }


    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer(UserRepository userRepository) {
        return context -> {
            Authentication authentication = context.getPrincipal();
            String grantType = context.getAuthorizationGrantType().getValue();

            if ("client_credentials".equals(grantType)) {
                String clientId = context.getRegisteredClient().getClientId();

                if ("pendezzapizza-tests".equals(clientId)) {
                    String gerenteEmail = "joao.ger@pendezzapizza.com";

                    var user = userRepository.findByEmail(gerenteEmail)
                            .orElseThrow(() -> new RuntimeException("Usuário gerente não encontrado"));

                    Set<String> authorities = user.getGroups().stream()
                            .flatMap(group -> group.getPermission().stream())
                            .map(Permission::getName)
                            .collect(Collectors.toSet());

                    context.getClaims().claim("user_id", user.getId().toString());
                    context.getClaims().claim("email", user.getEmail());
                    context.getClaims().claim("authorities", authorities);
                }
                return;
            }

            if (authentication.getPrincipal() instanceof User springUser) {

                var user = userRepository.findByEmail(springUser.getUsername())
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

                Set<String> authorities = springUser.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet());

                context.getClaims().claim("user_id", user.getId());
                context.getClaims().claim("email", user.getEmail());
                context.getClaims().claim("authorities", authorities);
            }
        };
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> authorities = jwt.getClaimAsStringList("authorities");
            if (authorities == null) {
                return Collections.emptyList();
            }

            JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
            Collection<GrantedAuthority> grantedAuthorities = authoritiesConverter.convert(jwt);

            grantedAuthorities.addAll(authorities
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList()
            );

            return grantedAuthorities;
        });

        return converter ;
    }
    @Bean
    public OAuth2AuthorizationService oAuth2AuthorizationService(JdbcOperations jdbcOperations,
                                                                 RegisteredClientRepository clientRepository) {
        var rowMapper = new JdbcOAuth2AuthorizationService
                .OAuth2AuthorizationRowMapper(clientRepository);
        var parametersMapper = new JdbcOAuth2AuthorizationService
                .OAuth2AuthorizationParametersMapper();

        ObjectMapper objectMapper = new ObjectMapper();
        ClassLoader classLoader = JdbcOAuth2AuthorizationService.class.getClassLoader();

        objectMapper.registerModules(SecurityJackson2Modules.getModules(classLoader));
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
        objectMapper.addMixIn(UUID.class, UUIDMixin.class);

        rowMapper.setObjectMapper(objectMapper);
        parametersMapper.setObjectMapper(objectMapper);

        var service = new JdbcOAuth2AuthorizationService(jdbcOperations, clientRepository);
        service.setAuthorizationRowMapper(rowMapper);
        service.setAuthorizationParametersMapper(parametersMapper);

        return service;
    }

    @Bean
    public OAuth2AuthorizationConsentService consentService (JdbcOperations jdbcOperations
            , RegisteredClientRepository clientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcOperations , clientRepository);
    }

    @Bean
    public OAuth2AuthorizationQueryService auth2AuthorizationQueryService (JdbcOperations jdbcOperations
            , RegisteredClientRepository clientRepository) {
        return new JdbcOAuth2AuthorizationQueryService(jdbcOperations , clientRepository);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer("http://localhost")
                .build();
    }
}
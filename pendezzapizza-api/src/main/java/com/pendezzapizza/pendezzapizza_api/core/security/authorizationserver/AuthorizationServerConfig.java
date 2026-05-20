package com.pendezzapizza.pendezzapizza_api.core.security.authorizationserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.pendezzapizza.pendezzapizza_api.core.security.SecurityController;
import com.pendezzapizza.pendezzapizza_api.domain.model.Permission;
import com.pendezzapizza.pendezzapizza_api.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@AllArgsConstructor
public class AuthorizationServerConfig {

    private final SecurityController securityController;

    // REMOVIDO: @Value("${pendezzapizza.auth.rsa-key-size:2048}") private static int rsaKeySize
    // Spring não injeta @Value em campos estáticos — o valor sempre seria 0.
    // O método generateRsaKey() também foi removido pois não era chamado em lugar nenhum.

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();
        authorizationServerConfigurer.authorizationEndpoint(
                customizer -> customizer.consentPage("/oauth2/consent"));
        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, authorizationServer ->
                        authorizationServer.oidc(Customizer.withDefaults())
                )
                .cors(Customizer.withDefaults())
                .formLogin(loginFormConfigurer ->
                        loginFormConfigurer.loginPage("/login").permitAll())
                .authorizeHttpRequests(authorize ->
                        authorize.anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder,
                                                                 JdbcOperations jdbcOperation) {
        return new JdbcRegisteredClientRepository(jdbcOperation);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(JwtKeyStoreProperties properties)
            throws KeyStoreException, JOSEException, NoSuchAlgorithmException, CertificateException, IOException {
        char[] keyStorePass = properties.getPassword().toCharArray();
        String keypairAlias = properties.getKeypairAlias();

        Resource jksLocation = properties.getJksLocation();
        InputStream inputStream = jksLocation.getInputStream();
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(inputStream, keyStorePass);

        RSAKey rsaKey = RSAKey.load(keyStore, keypairAlias, keyStorePass);
        return new ImmutableJWKSet<>(new JWKSet(rsaKey));
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer(UserRepository userRepository) {
        return context -> {
            String grantType = context.getAuthorizationGrantType().getValue();

            // Log em DEBUG — só aparece se logging.level estiver configurado para DEBUG.
            // Nunca vai vazar em produção com logging.level.root=INFO.
            log.debug("JWT customizer — grantType: {}, principalType: {}",
                    grantType,
                    context.getPrincipal().getPrincipal().getClass().getName());

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

            // Extrai o email do principal independente do grant type.
            // Necessário porque no refresh_token o principal não é um User completo —
            // o Spring Authorization Server usa o getName() que retorna o email/username.
            String email = extractEmail(context);
            if (email == null) return;

            var user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + email));

            // Sempre busca as permissões atualizadas do banco —
            // garante que promoções de grupo (ex: cadastrar restaurante) refletem no novo token.
            Set<String> authorities = user.getGroups().stream()
                    .flatMap(group -> group.getPermission().stream())
                    .map(Permission::getName)
                    .collect(Collectors.toSet());

            context.getClaims().claim("user_id", user.getId());
            context.getClaims().claim("email", user.getEmail());
            context.getClaims().claim("authorities", authorities);
        };
    }

    // Extrai o email do principal de forma defensiva, cobrindo todos os grant types:
    // - authorization_code: principal é User do Spring Security → getUsername()
    // - refresh_token: principal pode ser String ou outro tipo → getName()
    // A guarda com contains("@") evita buscar "anonymousUser" ou outros valores
    // inválidos no banco, o que causaria uma RuntimeException desnecessária.
    private String extractEmail(JwtEncodingContext context) {
        Authentication authentication = context.getPrincipal();

        if (authentication.getPrincipal() instanceof User springUser) {
            return springUser.getUsername();
        }

        if (authentication.getPrincipal() instanceof String str) {
            return str.contains("@") ? str : null;
        }

        // Fallback para refresh_token e outros fluxos
        String name = authentication.getName();
        return (name != null && name.contains("@")) ? name : null;
    }

    // CORRIGIDO: era um método privado sem @Bean — o Spring nunca registrava este converter,
    // então as authorities do claim "authorities" no JWT nunca eram lidas pelo resource server.
    // Agora é um @Bean e deve ser referenciado no seu ResourceServerConfig/WebSecurityConfig assim:
    //
    //   http.oauth2ResourceServer(oauth2 -> oauth2
    //       .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
    //   );
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> authorities = jwt.getClaimAsStringList("authorities");
            if (authorities == null) {
                return Collections.emptyList();
            }

            JwtGrantedAuthoritiesConverter scopeConverter = new JwtGrantedAuthoritiesConverter();
            Collection<GrantedAuthority> grantedAuthorities = scopeConverter.convert(jwt);

            // Adiciona as permissões customizadas (GERENCIAR_RESTAURANTE, etc.)
            // além dos escopos padrão (SCOPE_READ, SCOPE_WRITE)
            grantedAuthorities.addAll(
                    authorities.stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList()
            );

            return grantedAuthorities;
        });

        return converter;
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
    public OAuth2AuthorizationConsentService consentService(JdbcOperations jdbcOperations,
                                                            RegisteredClientRepository clientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcOperations, clientRepository);
    }

    @Bean
    public OAuth2AuthorizationQueryService auth2AuthorizationQueryService(JdbcOperations jdbcOperations,
                                                                          RegisteredClientRepository clientRepository) {
        return new JdbcOAuth2AuthorizationQueryService(jdbcOperations, clientRepository);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(securityController.getUrlIssuer())
                .build();
    }
}
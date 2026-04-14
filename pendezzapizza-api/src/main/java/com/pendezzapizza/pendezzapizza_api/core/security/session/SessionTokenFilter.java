package com.pendezzapizza.pendezzapizza_api.core.security.session;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class SessionTokenFilter extends OncePerRequestFilter {

    private final TokenSessionService tokenSessionService;
    private final RestTemplate restTemplate;

    @Value("${pendezzapizza.auth.client-id}")
    private String clientId;
    @Value("${pendezzapizza.auth.client-secret}")
    private String clientSecret;
    @Value("${pendezzapizza.auth.token-url}")
    private String tokenUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain filterChain) throws ServletException, IOException {

        String sessionId = extractSessionId(request);

        if (sessionId != null) {
            TokenSession session = tokenSessionService.getSession(sessionId);

            if (session != null) {
                // Verifica se o access_token está próximo de expirar
                String accessToken = getValidAccessToken(sessionId, session, response);

                if (accessToken != null) {
                    filterChain.doFilter(new BearerTokenRequestWrapper(request, accessToken), response);
                    return;
                }
                // refresh_token expirou — limpa sessão e manda pro login
                tokenSessionService.deleteSession(sessionId);
                clearSessionCookie(response);
            } else {
                clearSessionCookie(response);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getValidAccessToken(String sessionId, TokenSession session,
                                       HttpServletResponse response) {
        try {
            // Tenta decodificar o JWT para ver se está expirado
            JWT jwt = JWTParser.parse(session.getAccessToken());
            Date expiration = jwt.getJWTClaimsSet().getExpirationTime();

            // Renova se expira nos próximos 5 minutos
            boolean expirandoEmBreve = expiration != null &&
                    expiration.before(new Date(System.currentTimeMillis() + 5 * 60 * 1000));

            if (expirandoEmBreve) {
                return refreshAccessToken(sessionId, session.getRefreshToken());
            }

            return session.getAccessToken();

        } catch (Exception e) {
            // Token inválido — tenta renovar
            return refreshAccessToken(sessionId, session.getRefreshToken());
        }
    }

    private String refreshAccessToken(String sessionId, String refreshToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth(clientId, clientSecret);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "refresh_token");
            body.add("refresh_token", refreshToken);

            ResponseEntity<Map> res = restTemplate.exchange(
                    tokenUrl, HttpMethod.POST,
                    new HttpEntity<>(body, headers), Map.class
            );

            String newAccessToken = (String) res.getBody().get("access_token");
            String newRefreshToken = (String) res.getBody().get("refresh_token");

            // Atualiza o Redis com os novos tokens
            tokenSessionService.refreshSession(sessionId, newAccessToken, newRefreshToken);

            return newAccessToken;

        } catch (Exception e) {
            log.warn("Falha ao renovar token para sessão {}: {}", sessionId, e.getMessage());
            return null; // refresh_token expirou — força novo login
        }
    }

    private String extractSessionId(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        return Arrays.stream(request.getCookies())
                .filter(c -> "SESSION_ID".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    private void clearSessionCookie(HttpServletResponse response) {
        ResponseCookie expiredCookie = ResponseCookie.from("SESSION_ID", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", expiredCookie.toString());
    }

    // Wrapper que adiciona o Authorization header na request original
    static class BearerTokenRequestWrapper extends HttpServletRequestWrapper {
        private final String accessToken;

        public BearerTokenRequestWrapper(HttpServletRequest request, String accessToken) {
            super(request);
            this.accessToken = accessToken;
        }

        @Override
        public String getHeader(String name) {
            if ("Authorization".equalsIgnoreCase(name)) {
                return "Bearer " + accessToken;
            }
            return super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            if ("Authorization".equalsIgnoreCase(name)) {
                return Collections.enumeration(List.of("Bearer " + accessToken));
            }
            return super.getHeaders(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            List<String> names = Collections.list(super.getHeaderNames());
            if (names.stream().noneMatch("Authorization"::equalsIgnoreCase)) {
                names.add("Authorization");
            }
            return Collections.enumeration(names);
        }
    }



}
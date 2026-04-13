package com.pendezzapizza.pendezzapizza_api.core.security.session;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class SessionTokenFilter extends OncePerRequestFilter {

    private final TokenSessionService tokenSessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String sessionId = extractSessionId(request);
        //  Debug
        log.debug("SESSION_ID do cookie: {}", sessionId);
        log.debug("Cookies recebidos: {}",
                request.getCookies() != null ? Arrays.toString(request.getCookies()) : "nenhum");
        if (sessionId != null) {
            TokenSession session = tokenSessionService.getSession(sessionId);
            // Debug
            log.debug("Sessão encontrada no Redis: {}", session != null);

            if (session != null) {
                // Injeta o Bearer token na request antes de passar pro Spring Security
                HttpServletRequest wrappedRequest = new BearerTokenRequestWrapper(
                        request, session.getAccessToken()
                );
                filterChain.doFilter(wrappedRequest, response);
                return;
            } else {
                // Sessão expirou — limpa o cookie
                log.warn("Sessão não encontrada no Redis: {}", sessionId);
                clearSessionCookie(response);
            }
        }

        filterChain.doFilter(request, response);
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
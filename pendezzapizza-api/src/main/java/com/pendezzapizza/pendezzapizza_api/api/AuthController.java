package com.pendezzapizza.pendezzapizza_api.api;

import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
import com.pendezzapizza.pendezzapizza_api.core.security.session.TokenSession;
import com.pendezzapizza.pendezzapizza_api.core.security.session.TokenSessionService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TokenSessionService tokenSessionService;
    private final PendezzaPizzaSecurity pendezzaPizzaSecurity;

    @Value("${pendezzapizza.auth.token-url}")
    private String tokenUrl; // ex: http://localhost/oauth2/token

    @Value("${pendezzapizza.auth.client-id}")
    private String clientId;

    @Value("${pendezzapizza.auth.client-secret}")
    private String clientSecret;

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) return ResponseEntity.status(401).build();

        Map<String, Object> user = Map.of(
                "userId", jwt.getClaim("user_id"),
                "email", jwt.getClaim("email"),
                "authorities", jwt.getClaim("authorities")
        );
        return ResponseEntity.ok(user);
    }

    // Troca o refresh_token salvo na sessão por um novo access_token,
    // atualiza a sessão no Redis e retorna as authorities atualizadas.
    // Chamado pelo frontend após qualquer ação que mude os grupos do usuário.
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @CookieValue(value = "SESSION_ID", required = false) String sessionId) {

        if (sessionId == null) return ResponseEntity.status(401).build();

        TokenSession session = tokenSessionService.getSession(sessionId);
        if (session == null || session.getRefreshToken() == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            // Chama o Authorization Server interno com o refresh_token
            String credentials = Base64.getEncoder()
                    .encodeToString((clientId + ":" + clientSecret).getBytes());

            String body = "grant_type=refresh_token&refresh_token=" + session.getRefreshToken();

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(tokenUrl))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", "Basic " + credentials)
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.warn("Falha ao renovar token — status {}: {}", response.statusCode(), response.body());
                return ResponseEntity.status(401).build();
            }

            // Extrai os novos tokens da resposta JSON
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<?, ?> tokenResponse = mapper.readValue(response.body(), Map.class);

            String newAccessToken  = (String) tokenResponse.get("access_token");
            String newRefreshToken = (String) tokenResponse.get("refresh_token");

            if (newAccessToken == null) {
                return ResponseEntity.status(500).body("Token ausente na resposta");
            }

            // Atualiza a sessão Redis com os novos tokens
            tokenSessionService.refreshSession(sessionId, newAccessToken,
                    newRefreshToken != null ? newRefreshToken : session.getRefreshToken());

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            log.error("Erro ao fazer refresh de token", e);
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @CookieValue(value = "SESSION_ID", required = false) String sessionId,
            HttpServletResponse response) {

        if (sessionId != null) {
            tokenSessionService.deleteSession(sessionId);
        }

        ResponseCookie cookie = ResponseCookie.from("SESSION_ID", "")
                .httpOnly(true).path("/").maxAge(0).build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().build();
    }
}
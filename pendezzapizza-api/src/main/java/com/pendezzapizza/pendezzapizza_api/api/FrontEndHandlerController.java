package com.pendezzapizza.pendezzapizza_api.api;

import com.pendezzapizza.pendezzapizza_api.core.pcke.PkceService;
import com.pendezzapizza.pendezzapizza_api.core.security.session.TokenSessionService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/redirect")
@RequiredArgsConstructor
public class FrontEndHandlerController {

    private final TokenSessionService tokenSessionService;
    private final RestTemplate restTemplate;
    private final PkceService pkceService;

    @Value("${pendezzapizza.auth.token-url}")
    private String tokenUrl;

    @Value("${pendezzapizza.auth.client-id}")
    private String clientId;

    @Value("${pendezzapizza.auth.client-secret}")
    private String clientSecret;

    @Value("${pendezzapizza.auth.redirect-uri}")
    private String redirectUri;

    @Value("${pendezzapizza.frontend.url}")
    private String frontendUrl;

    @GetMapping
    public void captureCode(@RequestParam(required = false) String code,
                            @RequestParam(required = false) String state,
                            @RequestParam(required = false) String error,
                            HttpServletResponse response) throws IOException {

        if (error != null) {
            // Redireciona pro frontend com o erro para exibir uma mensagem
            response.sendRedirect(frontendUrl + "?error=" + error);
            return;
        }

        // 1. Recupera e deleta o code_verifier do Redis
        String codeVerifier = pkceService.consumeVerifier(state);

        // 2. Troca o code pelo token, agora com o code_verifier
        Map<String, String> tokens = exchangeCodeForTokens(code, codeVerifier);

        // 3. Salva tokens na sessão (como antes)
        String sessionId = tokenSessionService.createSession(
                tokens.get("access_token"),
                tokens.get("refresh_token")
        );

        // 4. Seta cookie e redireciona
        ResponseCookie cookie = ResponseCookie.from("SESSION_ID", sessionId)
                .httpOnly(true)
                .secure(false)
//                .sameSite("Strict")
                .sameSite("Lax")
                .path("/")
                .maxAge(3600)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.sendRedirect(frontendUrl);
    }
    private Map<String, String> exchangeCodeForTokens(String code, String codeVerifier) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // Basic Auth com clientId:clientSecret
        headers.setBasicAuth(clientId, clientSecret);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("redirect_uri", redirectUri);
        body.add("code_verifier", codeVerifier);

        ResponseEntity<Map> responseEntity = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                Map.class
        );

        return responseEntity.getBody();
    }
}
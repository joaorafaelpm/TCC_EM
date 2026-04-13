package com.pendezzapizza.pendezzapizza_api.core.pcke;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
public class PkceController {

    private final PkceService pkceService;

    @Value("${pendezzapizza.auth.authorization-url}")
    private String authorizationUrl;

    @Value("${pendezzapizza.auth.client-id}")
    private String clientId;

    @Value("${pendezzapizza.auth.redirect-uri}")
    private String redirectUri;


    @GetMapping("/oauth2/iniciar-login")
    public void iniciarLogin(HttpServletResponse response) throws Exception {
        PkceService.PkceData pkce = pkceService.generate();

        String authUrl = UriComponentsBuilder
                .fromUriString(authorizationUrl)
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("scope", "READ WRITE")
                .queryParam("state", pkce.state())
                .queryParam("code_challenge", pkce.codeChallenge())
                .queryParam("code_challenge_method", "S256")
                .build().toUriString();

        response.sendRedirect(authUrl);
    }

}

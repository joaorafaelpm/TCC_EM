package com.pendezzapizza.pendezzapizza_api.api;

import com.pendezzapizza.pendezzapizza_api.core.security.session.TokenSessionService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TokenSessionService tokenSessionService;

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

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @CookieValue(value = "SESSION_ID", required = false) String sessionId,
            HttpServletResponse response) {

        if (sessionId != null) {
            tokenSessionService.deleteSession(sessionId);
        }

        // Apaga o cookie
        ResponseCookie cookie = ResponseCookie.from("SESSION_ID", "")
            .httpOnly(true).path("/").maxAge(0).build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().build();
    }
}
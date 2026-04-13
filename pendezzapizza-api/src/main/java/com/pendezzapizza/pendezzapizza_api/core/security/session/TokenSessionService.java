package com.pendezzapizza.pendezzapizza_api.core.security.session;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenSessionService {

    // Chave padrão: "session:<uuid>"
    private static final String PREFIX = "session:";
    private static final Duration SESSION_TTL = Duration.ofHours(1);

    private final RedisTemplate<String, TokenSession> redisTemplate;

    public String createSession(String accessToken, String refreshToken) {
        String sessionId = UUID.randomUUID().toString();
        TokenSession session = new TokenSession(accessToken, refreshToken);
        redisTemplate.opsForValue().set(PREFIX + sessionId, session, SESSION_TTL);
        return sessionId;
    }

    public TokenSession getSession(String sessionId) {
        return redisTemplate.opsForValue().get(PREFIX + sessionId);
    }

    public void refreshSession(String sessionId, String newAccessToken, String newRefreshToken) {
        TokenSession session = new TokenSession(newAccessToken, newRefreshToken);
        // Mantém o mesmo TTL da sessão original
        Long ttl = redisTemplate.getExpire(PREFIX + sessionId);
        Duration remaining = ttl != null && ttl > 0 ? Duration.ofSeconds(ttl) : SESSION_TTL;
        redisTemplate.opsForValue().set(PREFIX + sessionId, session, remaining);
    }

    public void deleteSession(String sessionId) {
        redisTemplate.delete(PREFIX + sessionId);
    }
}
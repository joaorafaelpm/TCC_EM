package com.pendezzapizza.pendezzapizza_api.core.security.session;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenSessionService {

    private static final String SESSION_PREFIX = "session:";
    private static final String USER_SESSIONS_PREFIX = "user-sessions:";
    private static final Duration SESSION_TTL = Duration.ofDays(30);

    private final RedisTemplate<String, TokenSession> redisTemplate;
    private final RedisTemplate<String, String> stringRedisTemplate;

    public String createSession(String accessToken, String refreshToken, String userId) {
        String sessionId = UUID.randomUUID().toString();
        TokenSession session = new TokenSession(accessToken, refreshToken, userId, Instant.now());

        // Salva a sessão
        redisTemplate.opsForValue().set(SESSION_PREFIX + sessionId, session, SESSION_TTL);

        // Adiciona ao índice do usuário
        stringRedisTemplate.opsForSet().add(USER_SESSIONS_PREFIX + userId, sessionId);
        stringRedisTemplate.expire(USER_SESSIONS_PREFIX + userId, SESSION_TTL);

        return sessionId;
    }

    public TokenSession getSession(String sessionId) {
        return redisTemplate.opsForValue().get(SESSION_PREFIX + sessionId);
    }

    public void refreshSession(String sessionId, String newAccessToken, String newRefreshToken) {
        TokenSession existing = getSession(sessionId);
        if (existing == null) return;

        Long ttl = redisTemplate.getExpire(SESSION_PREFIX + sessionId);
        Duration remaining = ttl != null && ttl > 0 ? Duration.ofSeconds(ttl) : SESSION_TTL;

        TokenSession updated = new TokenSession(
                newAccessToken, newRefreshToken,
                existing.getUserId(), existing.getCreatedAt()
        );
        redisTemplate.opsForValue().set(SESSION_PREFIX + sessionId, updated, remaining);
    }

    // Lista todas as sessões ativas de um usuário
    public List<TokenSession> getSessionsByUser(String userId) {
        Set<String> sessionIds = stringRedisTemplate.opsForSet()
                .members(USER_SESSIONS_PREFIX + userId);

        if (sessionIds == null) return List.of();

        return sessionIds.stream()
                .map(id -> redisTemplate.opsForValue().get(SESSION_PREFIX + id))
                .filter(Objects::nonNull)
                .toList();
    }

    // Invalida todas as sessões de um usuário (ex: troca de senha)
    public void deleteAllSessionsByUser(String userId) {
        Set<String> sessionIds = stringRedisTemplate.opsForSet()
                .members(USER_SESSIONS_PREFIX + userId);

        if (sessionIds != null) {
            sessionIds.forEach(id -> redisTemplate.delete(SESSION_PREFIX + id));
        }
        stringRedisTemplate.delete(USER_SESSIONS_PREFIX + userId);
    }

    public void deleteSession(String sessionId) {
        TokenSession session = getSession(sessionId);
        if (session != null) {
            // Remove do índice do usuário também
            stringRedisTemplate.opsForSet()
                    .remove(USER_SESSIONS_PREFIX + session.getUserId(), sessionId);
        }
        redisTemplate.delete(SESSION_PREFIX + sessionId);
    }
}
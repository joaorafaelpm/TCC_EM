package com.pendezzapizza.pendezzapizza_api.core.pcke;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PkceService {

    private static final String PREFIX = "pkce:";
    private static final Duration PKCE_TTL = Duration.ofMinutes(5); // curto, é temporário

    private final RedisTemplate<String, PkceSession> redisTemplate;

    public PkceData generate() throws NoSuchAlgorithmException {
        // Gera o code_verifier: string aleatória de 64 bytes em base64url
        byte[] bytes = new byte[64];
        new SecureRandom().nextBytes(bytes);
        String codeVerifier = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        // Gera o code_challenge: SHA-256 do code_verifier em base64url
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
        String codeChallenge = Base64.getUrlEncoder().withoutPadding().encodeToString(hash);

        // State aleatório para vincular o callback
        String state = UUID.randomUUID().toString();

        // Salva no Redis com TTL curto
        redisTemplate.opsForValue().set(
            PREFIX + state, 
            new PkceSession(codeVerifier), 
            PKCE_TTL
        );

        return new PkceData(state, codeChallenge);
    }

    public String consumeVerifier(String state) {
        PkceSession session = redisTemplate.opsForValue().get(PREFIX + state);
        if (session == null) {
            throw new RuntimeException("PKCE session expirada ou inválida");
        }
        // Deleta imediatamente — uso único
        redisTemplate.delete(PREFIX + state);
        return session.getCodeVerifier();
    }

    public record PkceData(String state, String codeChallenge) {}
}
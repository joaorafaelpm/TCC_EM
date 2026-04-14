package com.pendezzapizza.pendezzapizza_api.core.security.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenSession implements Serializable {
    private String accessToken;
    private String refreshToken;
    private String userId;
    private Instant createdAt;
}
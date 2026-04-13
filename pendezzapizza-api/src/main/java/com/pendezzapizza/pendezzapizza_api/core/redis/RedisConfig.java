package com.pendezzapizza.pendezzapizza_api.core.redis;

import com.pendezzapizza.pendezzapizza_api.core.pcke.PkceSession;
import com.pendezzapizza.pendezzapizza_api.core.security.session.TokenSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, TokenSession> tokenSessionRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, TokenSession> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<String, PkceSession> pkceSessionRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, PkceSession> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}

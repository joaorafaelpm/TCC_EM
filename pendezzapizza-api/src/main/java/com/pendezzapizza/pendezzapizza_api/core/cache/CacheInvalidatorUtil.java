package com.pendezzapizza.pendezzapizza_api.core.cache;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CacheInvalidatorUtil {

    private StringRedisTemplate redisTemplate;

    public void publishCacheInvalidation(String... cacheNames) {
        for (String cacheName : cacheNames) {
            redisTemplate.convertAndSend("cache:invalidate", cacheName);
        }
    }

}

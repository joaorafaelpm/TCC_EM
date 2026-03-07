package com.pendezzapizza.pendezzapizza_api.core.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CacheInvalidationListener implements MessageListener {

    @Autowired
    private CacheManager cacheManager;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String cacheName = new String(message.getBody());
        log.info("Cache invalidation received: '{}' (bytes: {})", cacheName, message.getBody().length);
        Cache cache = cacheManager.getCache(cacheName);
        log.info("Cache found: {}", cache != null);
        if (cache != null) {
            cache.clear();
        }
    }
}
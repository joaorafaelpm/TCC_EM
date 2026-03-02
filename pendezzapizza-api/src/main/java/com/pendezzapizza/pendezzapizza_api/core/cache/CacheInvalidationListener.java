package com.pendezzapizza.pendezzapizza_api.core.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class CacheInvalidationListener implements MessageListener {

    @Autowired
    private CacheManager cacheManager;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String cacheName = new String(message.getBody());
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        }
    }
}
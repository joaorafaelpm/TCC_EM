package com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.save;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Caching(evict = {
        @CacheEvict(value = "orders", allEntries = true),
        @CacheEvict(value = "ordersLastUpdate", allEntries = true),
        @CacheEvict(value = "order", key = "#result.id"),
        @CacheEvict(value = "ordersLastUpdateById", key = "#result.id")
})
public @interface OrdersSaveCacheEvict {}
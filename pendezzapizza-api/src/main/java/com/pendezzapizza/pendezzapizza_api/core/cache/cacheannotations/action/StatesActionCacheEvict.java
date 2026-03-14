package com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.action;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// para (Delete, Ativar, Desativar)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Caching(evict = {
        @CacheEvict(value = "states", allEntries = true),
        @CacheEvict(value = "statesLastUpdate", allEntries = true),
        @CacheEvict(value = "state", key = "#stateId"),
        @CacheEvict(value = "statesLastUpdateById", key = "#stateId")
})
public @interface StatesActionCacheEvict {}
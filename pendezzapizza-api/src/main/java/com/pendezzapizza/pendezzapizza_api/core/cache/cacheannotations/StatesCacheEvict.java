package com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Caching(evict = {
        @CacheEvict(value = "states",            allEntries = true),
        @CacheEvict(value = "statesLastUpdate",  allEntries = true),
        @CacheEvict(value = "state", key = "#id ?: #stateId ?: #state?.id"),
        @CacheEvict(value = "statesLastUpdateById", key = "#id ?: #stateId ?: #state?.id")
})
public @interface StatesCacheEvict {
}
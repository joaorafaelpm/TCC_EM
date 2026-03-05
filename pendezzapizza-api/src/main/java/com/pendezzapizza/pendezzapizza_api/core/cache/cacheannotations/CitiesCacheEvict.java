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
        @CacheEvict(value = "cities", allEntries = true),
        @CacheEvict(value = "citiesLastUpdate", allEntries = true),
        @CacheEvict(value = "city", key = "#id ?: #cityId ?: #city?.id"),
        @CacheEvict(value = "citiesLastUpdateById", key = "#id ?: #cityId ?: #city?.id")
})
public @interface CitiesCacheEvict {
}
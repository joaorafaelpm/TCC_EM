package com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.save;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//// Para (POST/PUT)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Caching(evict = {
        @CacheEvict(value = "restaurants", allEntries = true),
        @CacheEvict(value = "restaurantsLastUpdate", allEntries = true),
        @CacheEvict(value = "restaurant", key = "#result.id"),
        @CacheEvict(value = "restaurantsLastUpdateById", key = "#result.id"),
        @CacheEvict(value = "restaurantsResponsibleUsers", key = "#result.id"),
        @CacheEvict(value = "restaurantsPaymentMethods", key = "#result.id")
})
public @interface RestaurantsSaveCacheEvict {}
package com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.action;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Caching(evict = {
        @CacheEvict(value = "restaurants", allEntries = true),
        @CacheEvict(value = "restaurantsLastUpdate", allEntries = true),
        @CacheEvict(value = "restaurant", key = "#restaurantId"),
        @CacheEvict(value = "restaurantsLastUpdateById", key = "#restaurantId"),
        @CacheEvict(value = "restaurantsResponsibleUsers", key = "#restaurantId"),
        @CacheEvict(value = "restaurantsPaymentMethods", key = "#restaurantId")
})
public @interface RestaurantsActionCacheEvict {}
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
        @CacheEvict(value = "restaurants", allEntries = true),
        @CacheEvict(value = "restaurantsLastUpdate", allEntries = true),
        @CacheEvict(value = "restaurant", key = "#id ?: #restaurantId ?: #restaurant?.id"),
        @CacheEvict(value = "restaurantsLastUpdateById", key = "#id ?: #restaurantId ?: #restaurant?.id"),
        @CacheEvict(value = "restaurantsResponsibleUsers", key = "#id ?: #restaurantId ?: #restaurant?.id"),
        @CacheEvict(value = "restaurantsPaymentMethods", key = "#id ?: #restaurantId ?: #restaurant?.id")
})
public @interface RestaurantsCacheEvict {
}
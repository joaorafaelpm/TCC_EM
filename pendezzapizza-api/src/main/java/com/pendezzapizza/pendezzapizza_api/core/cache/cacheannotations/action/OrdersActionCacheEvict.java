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
        @CacheEvict(value = "orders", allEntries = true),
        @CacheEvict(value = "ordersLastUpdate", allEntries = true),
        @CacheEvict(value = "order", key = "#orderId"),
        @CacheEvict(value = "ordersLastUpdateById", key = "#orderId"),
        @CacheEvict(value = "ordersRestaurant", key = "#restaurantId"),
        @CacheEvict(value = "orderUser", key = "#userId")
})
public @interface OrdersActionCacheEvict {}
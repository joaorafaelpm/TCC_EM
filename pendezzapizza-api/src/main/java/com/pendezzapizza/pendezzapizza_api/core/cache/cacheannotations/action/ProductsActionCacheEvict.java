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
        @CacheEvict(value = "product", key = "#productId"),
        @CacheEvict(value = "productsByRestaurant", allEntries = true),
        @CacheEvict(value = "productsActivesByRestaurant", allEntries = true),
        @CacheEvict(value = "productsLastUpdateDateActivesByRestaurantId", key = "#restaurantId"),
        @CacheEvict(value = "productsLastUpdateDateByRestaurantId", key = "#restaurantId")
})
public @interface ProductsActionCacheEvict {}
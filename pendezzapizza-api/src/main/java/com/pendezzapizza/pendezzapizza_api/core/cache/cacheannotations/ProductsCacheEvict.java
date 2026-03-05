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
        @CacheEvict(value = "product", key = "#restaurantId,#productId"),
        @CacheEvict(value = "productsByRestaurant" , key = "#restaurant"),
        @CacheEvict(value = "productsActivesByRestaurant" , key = "#restaurant"),
        @CacheEvict(value = "productsLastUpdateDateActivesByRestaurantId" , key = "#restaurantId"),
        @CacheEvict(value = "productsLastUpdateDateByRestaurantId" , key = "#restaurantId")

})
public @interface ProductsCacheEvict {
}
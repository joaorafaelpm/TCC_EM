package com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.save;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Para (POST/PUT)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Caching(evict = {
        // Limpa o cache do item específico
        @CacheEvict(value = "product", key = "#result.id"),
        // Limpa TODAS as páginas de produtos daquele restaurante
        @CacheEvict(value = "productsByRestaurant", allEntries = true),
        @CacheEvict(value = "productsActivesByRestaurant", allEntries = true),
        // Limpa as datas de última atualização
        @CacheEvict(value = "productsLastUpdateDateActivesByRestaurantId", key = "#restaurantId"),
        @CacheEvict(value = "productsLastUpdateDateByRestaurantId", key = "#restaurantId")
})
public @interface ProductsSaveCacheEvict {}
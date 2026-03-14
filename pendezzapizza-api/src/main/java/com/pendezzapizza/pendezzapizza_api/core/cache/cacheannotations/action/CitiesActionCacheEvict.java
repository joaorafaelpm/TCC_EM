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
        // Limpa as listas gerais
        @CacheEvict(value = "cities", allEntries = true),
        @CacheEvict(value = "citiesLastUpdate", allEntries = true),

        // Pega o ID diretamente do parâmetro do método (ex: UUID cityId)
        @CacheEvict(value = "city", key = "#cityId"),
        @CacheEvict(value = "citiesLastUpdateById", key = "#cityId")
})
public @interface CitiesActionCacheEvict {
}
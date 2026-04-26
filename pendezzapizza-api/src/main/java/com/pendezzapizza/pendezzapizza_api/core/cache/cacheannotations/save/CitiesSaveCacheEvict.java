package com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.save;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Caching(evict = {
        // Limpa as listas gerais (todas as páginas e data de atualização global)
        @CacheEvict(value = "cities", allEntries = true),
        @CacheEvict(value = "citiesLastUpdate", allEntries = true),

        // Pega o ID do objeto City retornado pelo método save()
        @CacheEvict(value = "city", key = "#result.id"),
        @CacheEvict(value = "cityAndStateName", key = "{#cityName, #stateName}"),
        @CacheEvict(value = "citiesLastUpdateById", key = "#result.id"),
        @CacheEvict(value = "citiesLastUpdateByName", key = "#result.name"),

})
public @interface CitiesSaveCacheEvict {
}
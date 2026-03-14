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
        @CacheEvict(value = "groups", allEntries = true),
        @CacheEvict(value = "groupsLastUpdate", allEntries = true),
        @CacheEvict(value = "group", key = "#result.id"),
        @CacheEvict(value = "groupsLastUpdateById", key = "#result.id")
})
public @interface GroupsSaveCacheEvict {}
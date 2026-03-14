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
        @CacheEvict(value = "permissions", allEntries = true),
        @CacheEvict(value = "permissionsLastUpdate", allEntries = true),
        @CacheEvict(value = "permission", key = "#result.id"),
        @CacheEvict(value = "permissionsLastUpdateById", key = "#result.id")
})
public @interface PermissionsSaveCacheEvict {}
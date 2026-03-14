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
        @CacheEvict(value = "permissions", allEntries = true),
        @CacheEvict(value = "permissionsLastUpdate", allEntries = true),
        @CacheEvict(value = "permission", key = "#permissionId"),
        @CacheEvict(value = "permissionsLastUpdateById", key = "#permissionId")
})
public @interface PermissionsActionCacheEvict {}
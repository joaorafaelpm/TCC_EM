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
        @CacheEvict(value = "users", allEntries = true),
        @CacheEvict(value = "userGroup", allEntries = true),
        @CacheEvict(value = "usersLastUpdate", allEntries = true),
        @CacheEvict(value = "user", key = "#result.id"),
        @CacheEvict(value = "usersLastUpdateById", key = "#result.id"),
        @CacheEvict(value = "usersRestaurants", key = "#result.id")

})
public @interface UsersSaveCacheEvict {}
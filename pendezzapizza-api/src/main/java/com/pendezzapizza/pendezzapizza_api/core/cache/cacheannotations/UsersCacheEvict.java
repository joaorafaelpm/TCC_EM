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
        @CacheEvict(value = "users", allEntries = true),
        @CacheEvict(value = "userGroup", allEntries = true),
        @CacheEvict(value = "usersLastUpdate", allEntries = true),
        @CacheEvict(value = "user", key = "#id ?: #userId ?: #user?.id"),
        @CacheEvict(value = "usersLastUpdateById", key = "#id ?: #userId ?: #user?.id")
})
public @interface UsersCacheEvict {
}
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
        @CacheEvict(value = "groups", allEntries = true),
        @CacheEvict(value = "groupsLastUpdate", allEntries = true),
        @CacheEvict(value = "group", key = "#groupId"),
        @CacheEvict(value = "groupsLastUpdateById", key = "#groupId")
})
public @interface GroupsActionCacheEvict {}
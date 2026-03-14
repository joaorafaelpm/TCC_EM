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
        @CacheEvict(value = "paymentMethods", allEntries = true),
        @CacheEvict(value = "paymentMethodsLastUpdate", allEntries = true),
        @CacheEvict(value = "paymentMethod", key = "#result.id"),
        @CacheEvict(value = "paymentMethodsLastUpdateById", key = "#result.id")
})
public @interface PaymentMethodsSaveCacheEvict {}

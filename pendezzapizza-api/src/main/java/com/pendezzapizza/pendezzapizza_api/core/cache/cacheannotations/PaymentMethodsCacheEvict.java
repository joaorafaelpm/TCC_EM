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
        @CacheEvict(value = "paymentMethods", allEntries = true),
        @CacheEvict(value = "paymentMethodsLastUpdate", allEntries = true),
        @CacheEvict(value = "paymentMethod", key = "#id ?: #paymentMethodId ?: #paymentMethod?.id"),
        @CacheEvict(value = "paymentMethodsLastUpdateById", key = "#id ?: #paymentMethodId ?: #paymentMethod?.id")
})
public @interface PaymentMethodsCacheEvict {
}
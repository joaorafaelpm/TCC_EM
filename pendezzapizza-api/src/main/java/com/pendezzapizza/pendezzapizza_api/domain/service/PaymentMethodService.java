package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.domain.exception.EntityInUseException;
import com.pendezzapizza.pendezzapizza_api.domain.model.PaymentMethod;
import com.pendezzapizza.pendezzapizza_api.domain.repository.PaymentMethodRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentMethodService {

    PaymentMethodRepository paymentMethodRepository;

    @Cacheable("paymentMethods")
    public Page<PaymentMethod> findAll(Pageable pageable) {
        return paymentMethodRepository.findAll(pageable);
    }
    @Cacheable(value = "paymentMethod", key = "#id")
    public PaymentMethod findById (UUID id) {
        return paymentMethodRepository.findByIdOrThrowException(id);
    }

    @Cacheable("paymentMethodsLastUpdate")
    public OffsetDateTime getLastUpdateDate () {
        return paymentMethodRepository.getLastUpdateDate();
    }
    @Cacheable(value = "paymentMethodsLastUpdateById", key = "#id")
    public OffsetDateTime getLastUpdateDateById (UUID id) {
        return paymentMethodRepository.getLastUpdateDateById(id);
    }

    @Caching(evict = {
            @CacheEvict(value = "paymentMethods",            allEntries = true),
            @CacheEvict(value = "paymentMethod",             key = "#paymentMethod.id"),
            @CacheEvict(value = "paymentMethodsLastUpdate",  allEntries = true),
            @CacheEvict(value = "paymentMethodsLastUpdateById", key = "#paymentMethod.id")
    })
    @Transactional
    public PaymentMethod save (PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }

    @Caching(evict = {
            @CacheEvict(value = "paymentMethods",            allEntries = true),
            @CacheEvict(value = "paymentMethod",             key = "#paymentMethod.id"),
            @CacheEvict(value = "paymentMethodsLastUpdate",  allEntries = true),
            @CacheEvict(value = "paymentMethodsLastUpdateById", key = "#paymentMethod.id")
    })
    @Transactional
    public PaymentMethod save (UUID id ,PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }

    @Caching(evict = {
            @CacheEvict(value = "paymentMethods",            allEntries = true),
            @CacheEvict(value = "paymentMethod",             key = "#paymentMethod.id"),
            @CacheEvict(value = "paymentMethodsLastUpdate",  allEntries = true),
            @CacheEvict(value = "paymentMethodsLastUpdateById", key = "#paymentMethod.id")
    })
    @Transactional
    public void remove (UUID id) {
        try {
            paymentMethodRepository.delete(findById(id));
            paymentMethodRepository.flush();
        }
        catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(id);
        }
    }


}
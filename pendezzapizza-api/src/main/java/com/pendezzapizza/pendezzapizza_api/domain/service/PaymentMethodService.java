package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.action.PaymentMethodsActionCacheEvict;
import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.save.PaymentMethodsSaveCacheEvict;
import com.pendezzapizza.pendezzapizza_api.domain.exception.EntityInUseException;
import com.pendezzapizza.pendezzapizza_api.domain.model.PaymentMethod;
import com.pendezzapizza.pendezzapizza_api.domain.repository.PaymentMethodRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class PaymentMethodService {

    PaymentMethodRepository paymentMethodRepository;

    @Cacheable("paymentMethods")
    public Page<PaymentMethod> findAll(Pageable pageable) {
        return paymentMethodRepository.findAll(pageable);
    }
    @Cacheable(value = "paymentMethod", key = "#paymentMethodId")
    public PaymentMethod findById (UUID paymentMethodId) {
        return paymentMethodRepository.findByIdOrThrowException(paymentMethodId);
    }

    @Cacheable("paymentMethodsLastUpdate")
    public OffsetDateTime getLastUpdateDate () {
        return paymentMethodRepository.getLastUpdateDate();
    }
    @Cacheable(value = "paymentMethodsLastUpdateById", key = "#paymentMethodId")
    public OffsetDateTime getLastUpdateDateById (UUID paymentMethodId) {
        return paymentMethodRepository.getLastUpdateDateById(paymentMethodId);
    }

    @PaymentMethodsSaveCacheEvict
    @Transactional
    public PaymentMethod save (PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }

    @PaymentMethodsSaveCacheEvict
    @Transactional
    public PaymentMethod save (UUID paymentMethodId ,PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }

    @PaymentMethodsActionCacheEvict
    @Transactional
    public void remove (UUID paymentMethodId) {
        try {
            paymentMethodRepository.delete(findById(paymentMethodId));
            paymentMethodRepository.flush();
        }
        catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(paymentMethodId);
        }
    }


}
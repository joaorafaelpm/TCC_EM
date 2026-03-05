package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.PaymentMethodsCacheEvict;
import com.pendezzapizza.pendezzapizza_api.domain.exception.EntityInUseException;
import com.pendezzapizza.pendezzapizza_api.domain.model.PaymentMethod;
import com.pendezzapizza.pendezzapizza_api.domain.repository.PaymentMethodRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    @PaymentMethodsCacheEvict
    @Transactional
    public PaymentMethod save (PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }

    @PaymentMethodsCacheEvict
    @Transactional
    public PaymentMethod save (UUID id ,PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }

    @PaymentMethodsCacheEvict
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
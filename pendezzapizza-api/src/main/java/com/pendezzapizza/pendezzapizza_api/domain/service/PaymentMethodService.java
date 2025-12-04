package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.domain.exception.EntityInUseException;
import com.pendezzapizza.pendezzapizza_api.domain.model.PaymentMethod;
import com.pendezzapizza.pendezzapizza_api.domain.repository.PaymentMethodRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentMethodService {

    PaymentMethodRepository paymentMethodRepository;

    public List<PaymentMethod> findAll() {
        return paymentMethodRepository.findAll();
    }

    public PaymentMethod findById (UUID id) {
        return paymentMethodRepository.findByIdOrThrowException(id);
    }

    public OffsetDateTime getLastUpdateDate () {
        return paymentMethodRepository.getLastUpdateDate();
    }
    public OffsetDateTime getLastUpdateDateById (UUID id) {
        return paymentMethodRepository.getLastUpdateDateById(id);
    }

    @Transactional
    public PaymentMethod save (PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }

    @Transactional
    public PaymentMethod save (UUID id ,PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }

    @Transactional
    public void remove (UUID id) {
        try {
            paymentMethodRepository.deleteById(id);
            paymentMethodRepository.flush();
        }
        catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(id);
        }
    }


}
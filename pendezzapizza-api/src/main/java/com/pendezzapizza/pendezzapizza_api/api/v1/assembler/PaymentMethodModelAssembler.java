package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.PaymentMethodMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.PaymentMethodModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.PaymentMethod;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@AllArgsConstructor
public class PaymentMethodModelAssembler{

    private PaymentMethodMapper paymentMethodMapper;

    public PaymentMethodModel toModel(PaymentMethod paymentMethod) {
        return paymentMethodMapper.toModel(paymentMethod);
    }

    public Collection<PaymentMethodModel> toCollectionModel(Collection<PaymentMethod> entities) {
        return entities.stream().map((this::toModel)).toList();
    }

}
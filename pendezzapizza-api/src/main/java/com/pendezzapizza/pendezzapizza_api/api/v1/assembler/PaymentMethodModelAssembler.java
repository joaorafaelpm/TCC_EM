package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.PaymentMethodMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.PaymentMethodModel;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class PaymentMethodModelAssembler{

    @Autowired
    private PaymentMethodMapper paymentMethodMapper;

    @Autowired
    private PendezzaPizzaSecurity pendezzaPizzaSecurity;

    public PaymentMethodModel toModel(PaymentMethod paymentMethod) {
        return paymentMethodMapper.toModel(paymentMethod);
    }

    public Collection<PaymentMethodModel> toCollectionModel(Collection<PaymentMethod> entities) {
        return entities.stream().map((this::toModel)).toList();
    }

}
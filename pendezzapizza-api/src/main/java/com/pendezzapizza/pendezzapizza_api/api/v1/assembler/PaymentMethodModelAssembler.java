package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.PaymentMethodMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.PaymentMethodModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.PaymentMethod;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Assembler da minha entidade de <b>forma de pagamento</b>
 *
 * <p>Essa é uma classe auxiliar que serve para usar o mapper de forma indireta</p>
 * <p>Opto por não usar o mapper direto para abrir a possibilidade de implementação de links (a gosto do freguês). Ou simplesmente adicionar lógica aqui dentro caso seja necessário </p>
 */
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
package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.PaymentMethodDTO;
import com.pendezzapizza.pendezzapizza_api.domain.model.PaymentMethod;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PaymentMethodDisassembler {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    PaymentMethod paymentMethodDTOToPaymentMethod (PaymentMethodDTO paymentMethodDTO) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    void updatePaymentMethodFromDto(PaymentMethodDTO dto, @MappingTarget PaymentMethod entity);


}
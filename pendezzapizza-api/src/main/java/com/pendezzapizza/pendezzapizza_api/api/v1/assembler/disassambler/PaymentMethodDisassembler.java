package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO.PaymentMethodDTO;
import com.pendezzapizza.pendezzapizza_api.domain.model.PaymentMethod;
import org.mapstruct.*;
import org.springframework.context.annotation.Bean;

@Mapper(componentModel = "spring")
public interface PaymentMethodDisassembler {

    @Bean
    PaymentMethod paymentMethodDTOToPaymentMethod (PaymentMethodDTO paymentMethodDTO) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updatePaymentMethodFromDto(PaymentMethodDTO dto, @MappingTarget PaymentMethod entity);


}

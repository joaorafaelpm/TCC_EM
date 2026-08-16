package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.PaymentMethodDTO;
import com.pendezzapizza.pendezzapizza_api.domain.model.PaymentMethod;
import org.mapstruct.*;

/**
 * Disassembler da entidade de forma de pagamento usando a biblioteca Mapstruct, que requer a anotação {@code Mapper} para funcionar corretamente
 *
 * <p>Faço o mapeamento partindo da minha entidade de DTO para a minha original</p>
 * <p>Toda diferença entre o modelo e a entidade original é mapeada nos métodos da interface e para evitar warning é <b>necessário<b> mapear cada um dos parâmetros mesmo que seja o mesmo</p>
 */
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
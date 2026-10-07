package com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.PaymentMethodModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.PaymentMethod;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;

/**
 * Mapeador da entidade de formas de pagamento usando a biblioteca Mapstruct, que requer a anotação {@code Mapper} para funcionar corretamente
 *
 * <p>Toda diferença entre o modelo e a entidade original é mapeada nos métodos da interface</p>
 */
@Mapper(componentModel = "spring")
public interface PaymentMethodMapper {

    @Bean
    PaymentMethodModel toModel(PaymentMethod paymentMethod);

}
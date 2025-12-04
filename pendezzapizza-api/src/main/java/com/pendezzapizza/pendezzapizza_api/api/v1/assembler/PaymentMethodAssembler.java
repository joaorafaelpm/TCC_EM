package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaPizzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.PaymentMethodMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.PaymentMethodModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
public class PaymentMethodAssembler extends RepresentationModelAssemblerSupport<PaymentMethod, PaymentMethodModel> {

    @Autowired
    private PaymentMethodMapper paymentMethodMapper;

    @Autowired
    private PendezzaPizzaLinks links;

    public PaymentMethodAssembler() {
        super(PaymentMethod.class, PaymentMethodModel.class);
    }

    @Override
    public PaymentMethodModel toModel(PaymentMethod entity) {
        PaymentMethodModel model = paymentMethodMapper.toModel(entity);

        model.add(links.linkToPaymentMethod(model.getId()));
        model.add(links.linkToPaymentMethods("paymentMethods"));

        return model;
    }

    public CollectionModel<PaymentMethodModel> toCollection(Collection<PaymentMethod> paymentMethods) {
        List<PaymentMethodModel> list = paymentMethods.stream().map(this::toModel).toList();
        CollectionModel<PaymentMethodModel> collection = CollectionModel.of(list);

        collection.add(links.linkToPaymentMethods("paymentMethods"));
        return collection;
    }

    // Usada para retornar formas de pagamento específicas de um restaurante
    public CollectionModel<PaymentMethodModel> toCollectionRefRestaurant(UUID restaurantId, Collection<PaymentMethod> paymentMethods) {
        CollectionModel<PaymentMethodModel> collection = toCollection(paymentMethods);

        collection.forEach(model ->
                model.add(links.linkToRestaurantPaymentMethodDissociate(
                        restaurantId, model.getId(), "disassociate"))
        );

        return collection
                .removeLinks()
                .add(links.linkToRestaurantPaymentMethods(restaurantId))
                .add(links.linkToRestaurantPaymentMethodAssociate(restaurantId, "associate"));
    }
}

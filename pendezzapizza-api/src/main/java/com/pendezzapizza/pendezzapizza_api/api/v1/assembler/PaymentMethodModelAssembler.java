package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.PaymentMethodMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.controller.PaymentMethodController;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.PaymentMethodModel;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
public class PaymentMethodModelAssembler extends RepresentationModelAssemblerSupport<PaymentMethod, PaymentMethodModel> {

    @Autowired
    private PaymentMethodMapper paymentMethodMapper;

    @Autowired
    private PendezzaLinks pendezzaLinks;

    @Autowired
    private PendezzaPizzaSecurity pendezzaPizzaSecurity;

    public PaymentMethodModelAssembler() {
        super(PaymentMethodController.class, PaymentMethodModel.class);
    }

    @Override
    public PaymentMethodModel toModel(PaymentMethod paymentMethod) {
        PaymentMethodModel paymentMethodModel = paymentMethodMapper.toModel(paymentMethod);

        if (pendezzaPizzaSecurity.canConsultPaymentMethods()) {
            paymentMethodModel.add(pendezzaLinks.linkToPaymentMethod(paymentMethodModel.getId()));
            paymentMethodModel.add(pendezzaLinks.linkToPaymentMethods("paymentMethods"));
        }

        return paymentMethodModel;
    }

    @Override
    public CollectionModel<PaymentMethodModel> toCollectionModel(Iterable<? extends PaymentMethod> entities) {
        List<PaymentMethodModel> list = ((Collection<PaymentMethod>) entities).stream()
                .map(this::toModel)
                .toList();

        CollectionModel<PaymentMethodModel> paymentMethodsCollectionModel = CollectionModel.of(list);

        if (pendezzaPizzaSecurity.canConsultPaymentMethods()) {
            paymentMethodsCollectionModel.add(pendezzaLinks.linkToPaymentMethods("paymentMethods"));
        }

        return paymentMethodsCollectionModel;
    }

    /**
     * Helper method to reduce controller logic, specifically for restaurant-related payment methods.
     */
    public CollectionModel<PaymentMethodModel> toCollectionRefRestaurant(UUID restaurantId, Collection<PaymentMethod> paymentMethods) {
        CollectionModel<PaymentMethodModel> paymentMethodsCollectionModel = toCollectionModel(paymentMethods);

        if (pendezzaPizzaSecurity.canManageRestaurantOperation(restaurantId)) {
            paymentMethodsCollectionModel.forEach(paymentMethodModel ->
                    paymentMethodModel.add(pendezzaLinks.linkToRestaurantPaymentMethodDissociate(
                            restaurantId, paymentMethodModel.getId(), "disassociate"))
            );
        }

        paymentMethodsCollectionModel.removeLinks();

        if (pendezzaPizzaSecurity.canConsultRestaurants()) {
            paymentMethodsCollectionModel.add(pendezzaLinks.linkToRestaurantPaymentMethods(restaurantId));
        }

        if (pendezzaPizzaSecurity.canManageRestaurantRegistrations()) {
            paymentMethodsCollectionModel.add(pendezzaLinks.linkToRestaurantPaymentMethodAssociate(
                    restaurantId, "association"));
        }

        return paymentMethodsCollectionModel;
    }
}
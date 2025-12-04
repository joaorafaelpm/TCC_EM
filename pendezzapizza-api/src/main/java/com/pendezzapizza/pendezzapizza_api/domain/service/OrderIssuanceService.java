package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.domain.exception.BusinessException;
import com.pendezzapizza.pendezzapizza_api.domain.model.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderIssuanceService {

    private final OrderService orderService;
    private final RestaurantService restaurantService;
    private final UserService userService;
    private final PaymentMethodService paymentMethodService;
    private final ProductService productService;
    private final CityService cityService;

    @Transactional
    public Order issueOrder(Order order) {

        assignRelationalObjectsToOrder(order);

        assignUnitPriceAndProductToOrderItem(order);

        order.setShippingFee(order.getRestaurant().getShippingFee());
        order.calculateTotalOrderCost();


        return orderService.save(order);
    }

    public void assignRelationalObjectsToOrder (Order order) {
        UUID restaurantId = order.getRestaurant().getId();
        UUID paymentMethodId = order.getPaymentMethods().getId();
        UUID cityId = order.getDeliveryAddress().getCity().getId();

        City city = cityService.findById(cityId);
        Restaurant restaurant = restaurantService.findById(restaurantId);
        PaymentMethod paymentMethod = paymentMethodService.findById(paymentMethodId);

        UUID userId = order.getClient().getId();
        User user = userService.findById(userId);

        order.getDeliveryAddress().setCity(city);
        order.setRestaurant(restaurant);
        order.setClient(user);
        if (restaurant.doesNotAcceptPaymentMethod(paymentMethod)) {
            throw new BusinessException(String.format("Payment method '%s' is not accepted by this restaurant.",
                    paymentMethod.getDescription()));
        }
        order.setPaymentMethods(paymentMethod);
    }

    public void assignUnitPriceAndProductToOrderItem (Order order) {
        order.getItems().forEach( item -> {
            Product product = productService.findById(order.getRestaurant().getId() , item.getProduct().getId());
            item.setOrder(order);
            item.setProduct(product);
            item.setUnitPrice(product.getPrice());
        });
    }


}
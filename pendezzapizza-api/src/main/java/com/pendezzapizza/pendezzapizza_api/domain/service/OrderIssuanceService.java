package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
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
    private final PaymentMethodService paymentMethodService;
    private final ProductService productService;
    private final CityService cityService;
    private final UserService userService;
    private final PendezzaPizzaSecurity pendezzaPizzaSecurity;

    @Transactional
    public Order issueOrder(Order order) {

        // Atribuímos o restaurante, cliente e forma de pagamento ao Pedido
        assignRelationalObjectsToOrder(order);

        // Atribuímos o preço unitário, o produto e o pedido ao itemPedido
        assignUnitPriceAndProductToOrderItem(order);

        order.setShippingFee(order.getRestaurant().getShippingFee());
        order.calculateTotalOrderCost();

        return orderService.save(order);
    }

    public void assignRelationalObjectsToOrder(Order order) {
        UUID restaurantId = order.getRestaurant().getId();
        UUID paymentMethodId = order.getPaymentMethod().getId();
        UUID cityId = order.getDeliveryAddress().getCity().getId();
        UUID customerId = pendezzaPizzaSecurity.getUserId();

        City city = cityService.findById(cityId);
        Restaurant restaurant = restaurantService.findByIdWithAllDependencies(restaurantId);
        PaymentMethod paymentMethod = paymentMethodService.findById(paymentMethodId);
        User user = userService.findById(customerId);

        order.getDeliveryAddress().setCity(city);
        order.setRestaurant(restaurant);
        order.setCustomer(user);

        if (restaurant.doesNotAcceptPaymentMethod(paymentMethod)) {
            throw new BusinessException(String.format("Forma de pagamento '%s' não é aceita por esse restaurante.",
                    paymentMethod.getDescription()));
        }
        order.setPaymentMethod(paymentMethod);
    }

    public void assignUnitPriceAndProductToOrderItem(Order order) {
        order.getItems().forEach(item -> {
            UUID restaurantId = order.getRestaurant().getId();
            UUID productId = item.getProduct().getId();

            Product product = productService.findById(restaurantId, productId);
            item.setOrder(order);
            item.setProduct(product);
            item.setUnitPrice(product.getPrice());
        });
    }
}
package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.OrderModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.OrderSummaryModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler.OrderDisassembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.OrderModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.OrderSummaryModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.OrderDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.OrderControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.data.PageWrapper;
import com.pendezzapizza.pendezzapizza_api.core.data.PageableTranslator;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.exception.BusinessException;
import com.pendezzapizza.pendezzapizza_api.domain.exception.EntityNotFoundException;
import com.pendezzapizza.pendezzapizza_api.domain.filter.OrderFilter;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import com.pendezzapizza.pendezzapizza_api.domain.service.OrderIssuanceService;
import com.pendezzapizza.pendezzapizza_api.domain.service.OrderService;
import com.pendezzapizza.pendezzapizza_api.infrastructure.repository.spec.OrderSpecs;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "/v1/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class OrderController implements OrderControllerOpenApi {

    private final OrderService orderService;
    private final OrderIssuanceService orderIssuanceService;
    private final OrderModelAssembler orderModelAssembler;
    private final OrderSummaryModelAssembler orderSummaryModelAssembler;
    private final OrderDisassembler orderDisassembler;
    private final PagedResourcesAssembler<Order> pagedResourcesAssembler;

    @CheckSecurity.Orders.CanList
    @GetMapping
    public PagedModel<OrderSummaryModel> search(OrderFilter orderFilter, Pageable pageable) {
        Pageable translatedPageable = translatePageable(pageable);
        Page<Order> ordersPage = orderService.findAll(OrderSpecs.withFilter(orderFilter), translatedPageable);
        ordersPage = new PageWrapper<>(ordersPage, pageable);

        return pagedResourcesAssembler.toModel(ordersPage, orderSummaryModelAssembler);
    }

    @CheckSecurity.Orders.CanSearch
    @GetMapping("/{orderId}")
    public OrderModel findById(@PathVariable UUID orderId) {
        return orderModelAssembler.toModel(orderService.findById(orderId));
    }

    @CheckSecurity.Orders.CanCreate
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderModel save(@RequestBody @Valid OrderDTO orderDTO) {
        try {
            Order order = orderDisassembler.orderDTOToOrder(orderDTO);
            return orderModelAssembler.toModel(orderIssuanceService.issueOrder(order));
        } catch (EntityNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

//    Nós usamos essa tradução para indicar o nome referente à ordenação da requisição, se eu quiser limitar por nome do restaurante eu preciso colocar restaurantName , restaurant.name
    private Pageable translatePageable(Pageable apiPageable) {
        var mapping = Map.of(
                "subtotal", "subtotal",
                "shippingFee", "shippingFee",
                "totalValue", "totalValue",
                "createdAt", "createdAt",
                "restaurant.name", "restaurant.name",
                "customer.name", "customer.name"
        );
        return PageableTranslator.translate(apiPageable, mapping);
    }
}
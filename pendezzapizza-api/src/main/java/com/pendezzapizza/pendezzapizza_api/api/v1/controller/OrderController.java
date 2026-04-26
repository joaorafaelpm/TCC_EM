package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.OrderModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.OrderSummaryModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler.OrderDisassembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.OrderModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.OrderSummaryModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.OrderBatchDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.OrderDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.OrderControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.data.PageWrapper;
import com.pendezzapizza.pendezzapizza_api.core.data.PageableTranslator;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.exception.BusinessException;
import com.pendezzapizza.pendezzapizza_api.domain.exception.EntityNotFoundException;
import com.pendezzapizza.pendezzapizza_api.domain.filter.OrderFilter;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import com.pendezzapizza.pendezzapizza_api.domain.model.OrderBatchModel;
import com.pendezzapizza.pendezzapizza_api.domain.service.OrderBatchIssuanceService;
import com.pendezzapizza.pendezzapizza_api.domain.service.OrderIssuanceService;
import com.pendezzapizza.pendezzapizza_api.domain.service.OrderService;
import com.pendezzapizza.pendezzapizza_api.infrastructure.repository.spec.OrderSpecs;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(path = "/v1/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class OrderController implements OrderControllerOpenApi {

    private final OrderService orderService;
    private final OrderIssuanceService orderIssuanceService;
    private final OrderModelAssembler orderModelAssembler;
    private final OrderSummaryModelAssembler orderSummaryModelAssembler;
    private final OrderDisassembler orderDisassembler;
    private final OrderBatchIssuanceService orderBatchIssuanceService;

    @CheckSecurity.Orders.CanList
    @GetMapping
    public ResponseEntity<Page<OrderSummaryModel>> search(OrderFilter orderFilter, Pageable pageable , ServletWebRequest request) {
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";
        OffsetDateTime lastUpdateDate = orderService.getLastUpdateDate();
        if (lastUpdateDate != null) {
            eTag = String.valueOf(lastUpdateDate.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }

        Pageable translatedPageable = translatePageable(pageable);
        Page<Order> ordersPage = orderService.findAll(OrderSpecs.withFilter(orderFilter), translatedPageable);
        ordersPage = new PageWrapper<>(ordersPage, pageable);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(ordersPage.map(orderSummaryModelAssembler::toModel));
    }

    @CheckSecurity.Orders.CanSearch
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderModel> findById(@PathVariable UUID orderId , ServletWebRequest request) {
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";
        OffsetDateTime lastUpdateDate = orderService.getLastUpdateDateById(orderId);
        if (lastUpdateDate != null) {
            eTag = String.valueOf(lastUpdateDate.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(orderModelAssembler.toModel(orderService.findById(orderId)));
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

    @CheckSecurity.Orders.CanCreate
    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.MULTI_STATUS) // 207 — alguns podem ter falhado
    public OrderBatchModel saveBatch(@RequestBody @Valid OrderBatchDTO batchDTO) {
        return orderBatchIssuanceService.issueBatch(batchDTO.orders());
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
package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.OrderModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.OrderSummaryModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassembler.OrderDisassembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.OrderModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.OrderSummaryModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.OrderBatchDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.OrderDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.OrderControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.data.PageableTranslator;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.exception.BusinessException;
import com.pendezzapizza.pendezzapizza_api.domain.exception.EntityNotFoundException;
import com.pendezzapizza.pendezzapizza_api.domain.filter.OrderTimeFilter;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import com.pendezzapizza.pendezzapizza_api.domain.model.OrderBatchModel;
import com.pendezzapizza.pendezzapizza_api.domain.service.OrderBatchIssuanceService;
import com.pendezzapizza.pendezzapizza_api.domain.service.OrderIssuanceService;
import com.pendezzapizza.pendezzapizza_api.domain.service.OrderService;
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

    /**
     * Lista os pedidos do próprio usuário autenticado.
     *
     * <p>O {@code userId} recebido como parâmetro é validado contra o JWT dentro de
     * {@code @CheckSecurity.Orders.CanListOwnOrders} — se não corresponder ao usuário
     * autenticado, a requisição é rejeitada antes de chegar aqui.</p>
     *
     * Sugestão de implementação para CanListOwnOrders:
     *   hasAuthorityRead() && isAuthenticatedUserEquals(userId)
     */
    @CheckSecurity.Orders.CanListOwnOrders
    @GetMapping("/user")
    public ResponseEntity<Page<OrderSummaryModel>> searchByCustomer(
            @RequestParam(required = true) UUID userId,
            OrderTimeFilter orderTimeFilter,
            Pageable pageable,
            ServletWebRequest request) {
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";
        OffsetDateTime lastUpdateDate = orderService.getLastUpdateDate();
        if (lastUpdateDate != null) {
            eTag = String.valueOf(lastUpdateDate.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }
        Page<Order> ordersPage = orderService.findAllByCustomerId(orderTimeFilter, userId, pageable);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePrivate())
                .eTag(eTag)
                .body(ordersPage.map(orderSummaryModelAssembler::toModel));
    }

    /**
     * Lista os pedidos de um restaurante específico.
     *
     * <p>Acessível pelo gestor do restaurante informado ou por admins com
     * {@code CONSULTAR_PEDIDOS}. A validação de vínculo gestor/restaurante
     * fica no {@code @CheckSecurity.Orders.CanListOwnRestaurantOrders}.</p>
     *
     * Sugestão de implementação para CanListOwnRestaurantOrders:
     *   hasAuthority("CONSULTAR_PEDIDOS") || (hasAuthorityRead() && managesRestaurant(restaurantId))
     */
    @CheckSecurity.Orders.CanListOwnRestaurantOrders
    @GetMapping("/restaurant")
    public ResponseEntity<Page<OrderSummaryModel>> searchByRestaurant(
            @RequestParam(required = true) UUID restaurantId,
            OrderTimeFilter orderTimeFilter,
            Pageable pageable,
            ServletWebRequest request) {
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";
        OffsetDateTime lastUpdateDate = orderService.getLastUpdateDate();
        if (lastUpdateDate != null) {
            eTag = String.valueOf(lastUpdateDate.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }
        Page<Order> ordersPage = orderService.findAllByRestaurantId(orderTimeFilter, restaurantId, pageable);
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
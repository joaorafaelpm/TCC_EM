package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.OrderFlowControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.service.OrderFlowService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/v1/orders/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class OrderFlowController implements OrderFlowControllerOpenApi {

    private final OrderFlowService orderFlowService;

    @CheckSecurity.Orders.CanManage
    @PutMapping("/confirmation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> confirm(@PathVariable UUID orderId) {
        orderFlowService.confirm(orderId);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Orders.CanManage
    @PutMapping("/delivery")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deliver(@PathVariable UUID orderId) {
        orderFlowService.deliver(orderId);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Orders.CanManage
    @PutMapping("/cancellation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> cancel(@PathVariable UUID orderId) {
        orderFlowService.cancel(orderId);
        return ResponseEntity.noContent().build();
    }
}
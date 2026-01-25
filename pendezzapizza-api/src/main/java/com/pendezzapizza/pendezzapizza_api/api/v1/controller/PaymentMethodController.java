package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.PaymentMethodModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler.PaymentMethodDisassembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.PaymentMethodModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.PaymentMethodDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.PaymentMethodControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.PaymentMethod;
import com.pendezzapizza.pendezzapizza_api.domain.service.PaymentMethodService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/v1/payment-methods")
@AllArgsConstructor
public class PaymentMethodController implements PaymentMethodControllerOpenApi {

    private final PaymentMethodService paymentMethodService;
    private final PaymentMethodModelAssembler paymentMethodAssembler;
    private final PaymentMethodDisassembler paymentMethodDisassembler;

    @CheckSecurity.PaymentMethods.CanConsult
    @GetMapping
    public ResponseEntity<CollectionModel<PaymentMethodModel>> all(ServletWebRequest request) {
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";
        OffsetDateTime lastUpdateDate = paymentMethodService.getLastUpdateDate();

        if (lastUpdateDate != null) {
            eTag = String.valueOf(lastUpdateDate.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }

        CollectionModel<PaymentMethodModel> paymentMethodModels = paymentMethodAssembler
                .toCollectionModel(paymentMethodService.findAll());

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(paymentMethodModels);
    }

    @CheckSecurity.PaymentMethods.CanConsult
    @GetMapping("/{paymentMethodId}")
    public ResponseEntity<PaymentMethodModel> findById(@PathVariable UUID paymentMethodId, ServletWebRequest request) {
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";
        OffsetDateTime lastUpdateDate = paymentMethodService.getLastUpdateDateById(paymentMethodId);

        if (lastUpdateDate != null) {
            eTag = String.valueOf(lastUpdateDate.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }

        PaymentMethodModel paymentMethodModel = paymentMethodAssembler
                .toModel(paymentMethodService.findById(paymentMethodId));

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(paymentMethodModel);
    }

    @CheckSecurity.PaymentMethods.CanEdit
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentMethodModel add(@RequestBody @Valid PaymentMethodDTO paymentMethodDTO) {
        PaymentMethod paymentMethod = paymentMethodDisassembler.paymentMethodDTOToPaymentMethod(paymentMethodDTO);
        return paymentMethodAssembler.toModel(paymentMethodService.save(paymentMethod));
    }

    @CheckSecurity.PaymentMethods.CanEdit
    @PutMapping("/{paymentMethodId}")
    public PaymentMethodModel save(@PathVariable UUID paymentMethodId, @RequestBody @Valid PaymentMethodDTO paymentMethodDTO) {
        PaymentMethod existingPaymentMethod = paymentMethodService.findById(paymentMethodId);
        paymentMethodDisassembler.updatePaymentMethodFromDto(paymentMethodDTO, existingPaymentMethod);

        return paymentMethodAssembler.toModel(paymentMethodService.save(existingPaymentMethod));
    }

    @CheckSecurity.PaymentMethods.CanEdit
    @DeleteMapping("/{paymentMethodId}")
    public ResponseEntity<Void> remove(@PathVariable UUID paymentMethodId) {
        paymentMethodService.remove(paymentMethodId);
        return ResponseEntity.noContent().build();
    }
}
package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.PaymentMethodAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler.PaymentMethodDisassembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO.PaymentMethodDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.PaymentMethodModel;
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
public class PaymentMethodController {

    private PaymentMethodService paymentMethodService;

    private PaymentMethodAssembler paymentMethodAssembler;
    private PaymentMethodDisassembler paymentMethodDisassembler;

    @GetMapping
    public ResponseEntity<CollectionModel<PaymentMethodModel>> findAll(ServletWebRequest request) {

        // Generate custom eTag
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());

        String eTag = "0";

        OffsetDateTime lastUpdatedAt = paymentMethodService.getLastUpdateDate();

        if (lastUpdatedAt != null) {
            eTag = String.valueOf(lastUpdatedAt.toEpochSecond());
        }

        // If nothing changed, skip processing
        if (request.checkNotModified(eTag)) {
            return null;
        }

        CollectionModel<PaymentMethodModel> models =
                paymentMethodAssembler.toCollection(paymentMethodService.findAll());

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(models);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodModel> findById(
            @PathVariable UUID id,
            ServletWebRequest request
    ) {
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";

        OffsetDateTime lastUpdatedAt = paymentMethodService.getLastUpdateDateById(id);

        if (lastUpdatedAt != null) {
            eTag = String.valueOf(lastUpdatedAt.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }

        PaymentMethodModel model =
                paymentMethodAssembler.toModel(paymentMethodService.findById(id));

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(model);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentMethodModel add(@RequestBody @Valid PaymentMethodDTO dto) {
        PaymentMethod paymentMethod =
                paymentMethodDisassembler.paymentMethodDTOToPaymentMethod(dto);

        return paymentMethodAssembler.toModel(paymentMethodService.save(paymentMethod));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethodModel> update(
            @PathVariable UUID id,
            @RequestBody @Valid PaymentMethodDTO dto
    ) {
        PaymentMethod old = paymentMethodService.findById(id);
        paymentMethodDisassembler.updatePaymentMethodFromDto(dto, old);

        return ResponseEntity.ok(
                paymentMethodAssembler.toModel(paymentMethodService.save(id, old))
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        paymentMethodService.remove(id);
    }
}

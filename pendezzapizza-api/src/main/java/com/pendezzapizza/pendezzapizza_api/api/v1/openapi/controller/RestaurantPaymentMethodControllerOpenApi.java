package com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.PaymentMethodModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.UUID;

@Tag(name = "Restaurantes")
@SecurityRequirement(name = "security_auth")
public interface RestaurantPaymentMethodControllerOpenApi {

    @Operation(summary = "Lista as formas de pagamento por id de um restaurante", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<Page<PaymentMethodModel>> all(@Parameter(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", description = "Id do restaurante", required = true) UUID restaurantId,
                                                 @Parameter(hidden = true) Pageable pageable,
                                                 @Parameter(hidden = true) ServletWebRequest request);

    @Operation(summary = "Desassocia uma forma de pagamento a um restaurante", responses = {
            @ApiResponse(responseCode = "204", description = "Forma de pagamento desassociada"),
            @ApiResponse(responseCode = "404", description = "Restaurante ou forma de pagamento não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<Void> disassociate(@Parameter(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", description = "Id do restaurante", required = true) UUID restaurantId,
                                      @Parameter(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", description = "Id da forma de pagamento", required = true) UUID paymentMethodId);

    @Operation(summary = "Associa uma forma de pagamento a um restaurante", responses = {
            @ApiResponse(responseCode = "204", description = "Forma de pagamento associada"),
            @ApiResponse(responseCode = "404", description = "Restaurante ou forma de pagamento não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<Void> associate(@Parameter(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", description = "Id do restaurante", required = true) UUID restaurantId,
                                   @Parameter(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", description = "Id da forma de pagamento", required = true) UUID paymentMethodId);
}
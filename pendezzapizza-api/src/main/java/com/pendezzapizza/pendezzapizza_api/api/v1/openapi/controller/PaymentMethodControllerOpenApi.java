package com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.PaymentMethodModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.PaymentMethodDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.UUID;

@Tag(name = "Formas De Pagamento")
public interface PaymentMethodControllerOpenApi {

    @Operation(description = "Lista formas de pagamento")
    ResponseEntity<Page<PaymentMethodModel>> all(
            @Parameter(required = false) String paymentMethodName ,
            @Parameter(hidden = true) Pageable pageable,
            @Parameter(hidden = true) ServletWebRequest request);

    @Operation(summary = "Busca uma forma de pagamento por id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "forma de pagamento não  encontrada", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<PaymentMethodModel> findById(
            @Parameter(description = "Id de uma forma de pagamento", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID paymentMethodId,
            @Parameter(hidden = true) ServletWebRequest request);

    @Operation(summary = "Busca uma forma de pagamento por id", responses = {
            @ApiResponse(responseCode = "201", description = "Forma de pagamento cadastrada")
    })
    PaymentMethodModel add(@RequestBody(description = "Representação de uma nova forma de pagamento", required = true) PaymentMethodDTO paymentMethodDTO);

    @Operation(summary = "Atualiza os dados de uma forma de pagamento por id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "Erro no nome da forma de pagamento", content = @Content(schema = @Schema(ref = "ApiError"))),
            @ApiResponse(responseCode = "404", description = "Forma de pagamento não encontrada", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    PaymentMethodModel save(@Parameter(description = "Id de uma forma de pagamento", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID paymentMethodId,
                            @RequestBody(description = "Representação de uma forma de pagamento com os dados atualizados", required = true) PaymentMethodDTO paymentMethodDTO);

    @Operation(summary = "Remove uma forma de pagamento por id", responses = {
            @ApiResponse(responseCode = "204", description = "Forma de pagamento removida"),
            @ApiResponse(responseCode = "404", description = "Forma de pagamento não encontrada", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<Void> remove(@Parameter(description = "Id de uma forma de pagamento", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID paymentMethodId);
}
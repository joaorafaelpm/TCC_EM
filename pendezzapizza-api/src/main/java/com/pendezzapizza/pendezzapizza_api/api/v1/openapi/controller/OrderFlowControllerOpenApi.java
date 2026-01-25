package com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Pedidos")
public interface OrderFlowControllerOpenApi {

    @Operation(summary = "Confirma um pedido", responses = {
            @ApiResponse(responseCode = "204", description = "Pedido confirmado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<Void> confirm(@Parameter(example = "936dc9ec-05bf-44e5-8c07-7e51adc6083d", description = "Código do pedido a ser confirmado", required = true) UUID codeId);

    @Operation(summary = "Cancela um pedido", responses = {
            @ApiResponse(responseCode = "204", description = "Pedido cancelado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<Void> cancel(@Parameter(example = "936dc9ec-05bf-44e5-8c07-7e51adc6083d", description = "Código do pedido a ser cancelado", required = true) UUID codeId);

    @Operation(summary = "Entrega um pedido", responses = {
            @ApiResponse(responseCode = "204", description = "Pedido entregado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<Void> deliver(@Parameter(example = "936dc9ec-05bf-44e5-8c07-7e51adc6083d", description = "Código do pedido a ser entregue", required = true) UUID codeId);
}
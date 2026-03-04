package com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.OrderModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.OrderSummaryModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.OrderDTO;
import com.pendezzapizza.pendezzapizza_api.core.springdoc.annotations.PageableParameter;
import com.pendezzapizza.pendezzapizza_api.core.springdoc.annotations.PedidoFilterAnnotation;
import com.pendezzapizza.pendezzapizza_api.domain.filter.OrderFilter;
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

@Tag(name = "Pedidos")
public interface OrderControllerOpenApi {

    @PageableParameter
    @PedidoFilterAnnotation
    @Operation(summary = "Lista de pedidos")
    ResponseEntity<Page<OrderSummaryModel>> search(@Parameter(hidden = true) OrderFilter orderFilter,
                                                   @Parameter(hidden = true) Pageable pageable,
                                                   @Parameter(hidden = true) ServletWebRequest request);

    @Operation(summary = "Cadastra um novo pedido", responses = {
            @ApiResponse(responseCode = "201", description = "Pedido cadastrado")
    })
    OrderModel save(@RequestBody(description = "Representação de um novo pedido", required = true) OrderDTO orderDTO);

    @Operation(summary = "Pega um único pedido pelo id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<OrderModel> findById(@Parameter(description = "Id de um pedido", example = "936dc9ec-05bf-44e5-8c07-7e51adc6083d", required = true) UUID orderId,
                        @Parameter(hidden = true) ServletWebRequest request);
}
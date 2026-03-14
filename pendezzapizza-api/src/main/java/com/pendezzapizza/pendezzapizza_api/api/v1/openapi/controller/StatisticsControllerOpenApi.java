package com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller;

import com.pendezzapizza.pendezzapizza_api.core.springdoc.annotations.DailySalesFilterAnnotation;
import com.pendezzapizza.pendezzapizza_api.domain.filter.DailySalesFilter;
import com.pendezzapizza.pendezzapizza_api.domain.model.dto.DailySale;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Estatísticas")
@SecurityRequirement(name = "security_auth")
public interface StatisticsControllerOpenApi {

    @DailySalesFilterAnnotation
    @Operation(summary = "Consulta as vendas diárias", responses = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DailySale.class))),
                    @Content(mediaType = "application/pdf", schema = @Schema(type = "string", format = "binary")),
            })
    })
    List<DailySale> consultDailySales(@Parameter(hidden = true) DailySalesFilter filter,
                                    @Parameter(description = "Deslocamento de horário a ser considerado na consulta em relação ao UTC", schema = @Schema(type = "string", defaultValue = "+00:00")) String timeOffset);

    @Operation(hidden = true)
    ResponseEntity<byte[]> consultDailySalesPdf(DailySalesFilter filter, String timeOffset);
}
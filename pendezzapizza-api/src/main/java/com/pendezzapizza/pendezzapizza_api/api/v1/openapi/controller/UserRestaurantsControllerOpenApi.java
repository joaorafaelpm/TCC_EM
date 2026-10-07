package com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.RestaurantModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.UUID;

@Tag(name = "Users")
public interface UserRestaurantsControllerOpenApi {

    @Operation(summary = "Lista os restaurantes que o usuário é responsável", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<Page<RestaurantModel>> list(@Parameter(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", description = "Id do restaurante", required = true) UUID restaurantId,
                                               @Parameter(hidden = true)Pageable pageable ,
                                               @Parameter(hidden = true) ServletWebRequest request);

}
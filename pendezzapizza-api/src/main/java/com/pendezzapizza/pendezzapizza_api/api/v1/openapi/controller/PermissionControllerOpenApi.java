package com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.PermissionModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

@Tag(name = "Permissões")
public interface PermissionControllerOpenApi {
    @Operation(summary = "Lista  de permissões")
    ResponseEntity<Page<PermissionModel>> findAll(
            @Parameter(hidden = true, description = "Filtra uma permissão pelo nome.") String permissionName,
            @Parameter(hidden = true) Pageable pageable ,
            @Parameter(hidden = true) ServletWebRequest request);
}

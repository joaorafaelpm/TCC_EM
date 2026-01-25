package com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.PermissionModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;

@Tag(name = "Permissões")
public interface PermissionControllerOpenApi {
    @Operation(summary = "Lista  de permissões")
    CollectionModel<PermissionModel> findAll();
}

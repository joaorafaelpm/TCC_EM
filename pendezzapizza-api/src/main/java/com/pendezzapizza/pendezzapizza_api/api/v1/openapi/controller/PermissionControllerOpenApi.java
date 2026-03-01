package com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.PermissionModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Collection;

@Tag(name = "Permissões")
public interface PermissionControllerOpenApi {
    @Operation(summary = "Lista  de permissões")
    Collection<PermissionModel> findAll();
}

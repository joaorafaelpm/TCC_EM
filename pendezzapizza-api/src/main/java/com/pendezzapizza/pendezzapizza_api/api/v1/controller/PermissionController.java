package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.PermissionAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.PermissionModel;
import com.pendezzapizza.pendezzapizza_api.domain.service.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/permissions")
@AllArgsConstructor
public class PermissionController {

    private PermissionService permissionService;

    private PermissionAssembler permissionAssembler;

    @GetMapping
    public CollectionModel<PermissionModel> findAll() {
        return permissionAssembler.toCollection(permissionService.findAll());
    }
}

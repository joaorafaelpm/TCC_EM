package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.PermissionModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.PermissionModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.PermissionControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.Permission;
import com.pendezzapizza.pendezzapizza_api.domain.service.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(path = "/v1/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class PermissionController implements PermissionControllerOpenApi {

    private final PermissionService permissionService;
    private final PermissionModelAssembler permissionAssembler;

    @CheckSecurity.UsersGroupsPermissions.CanConsult
    @GetMapping
    public ResponseEntity<Page<PermissionModel>> findAll(@RequestParam(required = false) String permissionName, Pageable pageable , ServletWebRequest request) {
        Page<Permission> permissions;
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";
        OffsetDateTime lastUpdateDate = permissionService.getLastUpdateDate();
        if (lastUpdateDate != null) {
            eTag = String.valueOf(lastUpdateDate.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }

        if (permissionName == null) {
            permissions = permissionService.findAll(pageable);
        }
        else {
            permissions = permissionService.findAllByName(permissionName , pageable);
        }

        Page<PermissionModel> permissionsModel = permissions.map(permissionAssembler::toModel);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(permissionsModel);
    }
}
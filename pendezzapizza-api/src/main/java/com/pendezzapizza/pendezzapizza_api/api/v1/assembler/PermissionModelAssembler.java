package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.PermissionMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.controller.PermissionController;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.PermissionModel;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.UUID;

@Component
public class PermissionModelAssembler extends RepresentationModelAssemblerSupport<Permission, PermissionModel> {

    @Autowired
    private PendezzaLinks pendezzaLinks;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private PendezzaPizzaSecurity pendezzaPizzaSecurity;

    public PermissionModelAssembler() {
        super(PermissionController.class, PermissionModel.class);
    }

    @Override
    public PermissionModel toModel(Permission permission) {
        PermissionModel permissionModel = permissionMapper.toModel(permission);

        if (pendezzaPizzaSecurity.canConsultUsersGroupsPermissions()) {
            permissionModel.add(pendezzaLinks.linkToPermissions());
        }
        return permissionModel;
    }

    @Override
    public CollectionModel<PermissionModel> toCollectionModel(Iterable<? extends Permission> entities) {
        CollectionModel<PermissionModel> permissionsCollectionModel = super.toCollectionModel(entities);

        if (pendezzaPizzaSecurity.canConsultUsersGroupsPermissions()) {
            permissionsCollectionModel.add(pendezzaLinks.linkToPermissions("permissions"));
        }

        return permissionsCollectionModel;
    }

    public CollectionModel<PermissionModel> toCollectionRefGroup(UUID groupId, Collection<Permission> permissions) {
        CollectionModel<PermissionModel> permissionsCollectionModel = toCollectionModel(permissions);

        if (pendezzaPizzaSecurity.canEditUsersGroupsPermissions()) {
            permissionsCollectionModel.forEach(permissionModel ->
                    permissionModel.add(pendezzaLinks.linkToGroupPermissionDissociation(
                            groupId, permissionModel.getId(), "disassociate")));

            permissionsCollectionModel.add(pendezzaLinks.linkToGroupPermissionAssociation(
                    groupId, null, "associate"));

        }
        return permissionsCollectionModel;
    }
}
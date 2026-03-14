package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaPizzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.PermissionMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.PermissionModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.UUID;

@Component
public class PermissionAssembler extends RepresentationModelAssemblerSupport<Permission, PermissionModel> {

    @Autowired
    private PendezzaPizzaLinks links;

    @Autowired
    private PermissionMapper permissionMapper;

    public PermissionAssembler() {
        super(Permission.class, PermissionModel.class);
    }

    @Override
    public PermissionModel toModel(Permission entity) {
        PermissionModel model = permissionMapper.toModel(entity);
        model.add(links.linkToPermissions());
        return model;
    }

    public CollectionModel<PermissionModel> toCollection(Collection<Permission> permissions) {
        var models = permissions.stream().map(this::toModel).toList();
        CollectionModel<PermissionModel> collection = CollectionModel.of(models);
        collection.add(links.linkToPermissions("permissions"));
        return collection;
    }

    public CollectionModel<PermissionModel> toCollectionRefGroup(UUID groupId, Collection<Permission> permissions) {
        var models = permissions.stream().map(this::toModel).toList();
        CollectionModel<PermissionModel> collection = CollectionModel.of(models);

        collection.forEach(permissionModel ->
                permissionModel.add(links.linkToGroupPermissionDissociation(groupId, permissionModel.getId(), "disassociate"))
        );
        collection.add(links.linkToPermissions("permissions"));
        collection.add(links.linkToGroupPermissionAssociation(groupId, null, "associate"));

        return collection;
    }
}

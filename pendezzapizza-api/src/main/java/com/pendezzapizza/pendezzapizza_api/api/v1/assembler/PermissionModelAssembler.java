package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.PermissionMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.PermissionModel;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class PermissionModelAssembler{


    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private PendezzaPizzaSecurity pendezzaPizzaSecurity;



    public PermissionModel toModel(Permission permission) {
        return permissionMapper.toModel(permission);
    }


    public Collection<PermissionModel> toCollectionModel(Collection<Permission> entities) {
        return entities.stream().map((this::toModel)).toList();
    }

}
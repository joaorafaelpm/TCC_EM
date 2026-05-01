package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.PermissionMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.PermissionModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Permission;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Assembler da minha entidade de <b>permissão</b>
 *
 * <p>Essa é uma classe auxiliar que serve para usar o mapper de forma indireta</p>
 * <p>Opto por não usar o mapper direto para abrir a possibilidade de implementação de links (a gosto do freguês). Ou simplesmente adicionar lógica aqui dentro caso seja necessário </p>
 */
@Component
@AllArgsConstructor
public class PermissionModelAssembler{
    private PermissionMapper permissionMapper;

    public PermissionModel toModel(Permission permission) {
        return permissionMapper.toModel(permission);
    }


    public Collection<PermissionModel> toCollectionModel(Collection<Permission> entities) {
        return entities.stream().map((this::toModel)).toList();
    }

}
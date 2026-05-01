package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.UserMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.UserModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.User;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Assembler da minha entidade de <b>usuário</b>
 *
 * <p>Essa é uma classe auxiliar que serve para usar o mapper de forma indireta</p>
 * <p>Opto por não usar o mapper direto para abrir a possibilidade de implementação de links (a gosto do freguês). Ou simplesmente adicionar lógica aqui dentro caso seja necessário </p>
 */
@Component
@AllArgsConstructor
public class UserModelAssembler  {

    private UserMapper userMapper;

    public UserModel toModel(User entity) {
        return userMapper.toModel(entity);
    }

    public Collection<UserModel> toCollectionModel(Collection<User> entities) {
        return entities.stream().map((this::toModel)).toList();
    }

}
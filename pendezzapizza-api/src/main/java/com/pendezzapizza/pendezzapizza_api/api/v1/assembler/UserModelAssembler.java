package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.UserMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.UserModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.User;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.Collection;


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
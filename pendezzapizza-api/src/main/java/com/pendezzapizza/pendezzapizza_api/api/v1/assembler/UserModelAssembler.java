package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.UserMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.UserModel;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;


@Component
public class UserModelAssembler  {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PendezzaPizzaSecurity pendezzaPizzaSecurity;


    public UserModel toModel(User entity) {
        return userMapper.toModel(entity);
    }

    public Collection<UserModel> toCollectionModel(Collection<User> entities) {
        return entities.stream().map((this::toModel)).toList();
    }

}
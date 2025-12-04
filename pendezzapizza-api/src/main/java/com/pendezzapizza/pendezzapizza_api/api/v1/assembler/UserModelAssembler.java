package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaPizzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.UserMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.controller.UserController;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.UserModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
public class UserModelAssembler extends RepresentationModelAssemblerSupport<User, UserModel> {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PendezzaPizzaLinks links;

    public UserModelAssembler() {
        super(UserController.class, UserModel.class);
    }

    @Override
    public UserModel toModel(User entity) {
        UserModel model = userMapper.toModel(entity);

        model.add(links.linkToUser(model.getId()));
        model.add(links.linkToUsers());
        model.add(links.linkToUserGroups(model.getId(), "groups"));

        return model;
    }

    public CollectionModel<UserModel> toCollection(Collection<User> users) {
        List<UserModel> list = users.stream().map(this::toModel).toList();
        CollectionModel<UserModel> collection = CollectionModel.of(list);

        collection.add(links.linkToUsers("users"));

        return collection;
    }

    public CollectionModel<UserModel> toCollectionRefRestaurant(UUID restaurantId, Collection<User> users) {
        CollectionModel<UserModel> collection = toCollection(users);

        collection.forEach(user ->
                user.add(links.linkToRestaurantManagersDissociation(restaurantId, user.getId(), "disassociate"))
        );

        return collection.removeLinks()
                .add(links.linkToRestaurantManagers(restaurantId))
                .add(links.linkToRestaurantManagersAssociation(restaurantId, "associate"));
    }
}

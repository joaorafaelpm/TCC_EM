package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.UserMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.UserModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.controller.UserController;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
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
    private PendezzaLinks pendezzaLinks;

    @Autowired
    private PendezzaPizzaSecurity pendezzaPizzaSecurity;

    public UserModelAssembler() {
        super(UserController.class, UserModel.class);
    }

    @Override
    public UserModel toModel(User entity) {
        UserModel userModel = userMapper.toModel(entity);

        if (pendezzaPizzaSecurity.canConsultUsersGroupsPermissions()) {
            userModel.add(pendezzaLinks.linkToUser(userModel.getId()));
            userModel.add(pendezzaLinks.linkToUsers());
            userModel.add(pendezzaLinks.linkToUserGroups(userModel.getId(), "userGroups"));
        }

        return userModel;
    }

    @Override
    public CollectionModel<UserModel> toCollectionModel(Iterable<? extends User> entities) {
        CollectionModel<UserModel> userModels = super.toCollectionModel(entities);

        if (pendezzaPizzaSecurity.canConsultUsersGroupsPermissions()) {
            userModels.add(pendezzaLinks.linkToUsers("users"));
        }

        return userModels;
    }

    public CollectionModel<UserModel> toCollectionRefRestaurant(UUID restaurantId, Collection<User> users) {
        List<UserModel> userList = users.stream().map(this::toModel).toList();
        CollectionModel<UserModel> userCollectionModel = CollectionModel.of(userList);

        if (pendezzaPizzaSecurity.canManageRestaurantRegistrations()) {
            userCollectionModel.forEach(userModel ->
                    userModel.add(pendezzaLinks.
                            linkToRestaurantManagersDissociation(restaurantId, userModel.getId(), "disassociate")));

            userCollectionModel.removeLinks()
                    .add(pendezzaLinks.linkToRestaurantManagers(restaurantId))
                    .add(pendezzaLinks.linkToRestaurantManagersAssociation(restaurantId, "associate"));
        }
        return userCollectionModel;
    }
}
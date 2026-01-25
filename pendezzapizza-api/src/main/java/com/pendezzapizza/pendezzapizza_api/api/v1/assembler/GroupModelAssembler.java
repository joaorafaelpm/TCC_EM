package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.GroupMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.controller.GroupController;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.GroupModel;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.UUID;

@Component
public class GroupModelAssembler extends RepresentationModelAssemblerSupport<Group, GroupModel> {

    @Autowired
    private PendezzaLinks pendezzaLinks;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private PendezzaPizzaSecurity pendezzaPizzaSecurity;

    public GroupModelAssembler() {
        super(GroupController.class, GroupModel.class);
    }

    @Override
    public GroupModel toModel(Group group) {
        GroupModel groupModel = groupMapper.toModel(group);

        if (pendezzaPizzaSecurity.canConsultUsersGroupsPermissions()) {
            groupModel.add(pendezzaLinks.linkToGroups());
            groupModel.add(pendezzaLinks.linkToGroup(group.getId()));
            groupModel.add(pendezzaLinks.linkToGroupPermissions(group.getId(), "permissions"));
        }

        return groupModel;
    }

    @Override
    public CollectionModel<GroupModel> toCollectionModel(Iterable<? extends Group> entities) {
        CollectionModel<GroupModel> groupsCollectionModel = super.toCollectionModel(entities);

        if (pendezzaPizzaSecurity.canConsultUsersGroupsPermissions()) {
            groupsCollectionModel.add(pendezzaLinks.linkToGroups("groups"));
        }

        return groupsCollectionModel;
    }

    public CollectionModel<GroupModel> toCollectionRefUser(UUID userId, Collection<Group> groups) {
        CollectionModel<GroupModel> groupsCollectionModel = toCollectionModel(groups);

        if (pendezzaPizzaSecurity.canEditUsersGroupsPermissions()) {
            groupsCollectionModel.forEach(groupModel ->
                    groupModel.add(pendezzaLinks.linkToUserGroupDissociation(userId, groupModel.getId(), "disassociate")));

            groupsCollectionModel.add(pendezzaLinks.linkToUserGroupAssociation(userId, null , "associate")); // Ajustei para linkToGroupUserAssociation, assumindo que existe
        }

        if (pendezzaPizzaSecurity.canConsultUsersGroupsPermissions()) {
            groupsCollectionModel.add(pendezzaLinks.linkToUserGroups(userId));

        }

        return groupsCollectionModel;
    }
}
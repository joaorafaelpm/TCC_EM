package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaPizzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.GroupMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.GroupModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.UUID;

@Component
public class GroupAssembler extends RepresentationModelAssemblerSupport<Group, GroupModel> {

    @Autowired
    private PendezzaPizzaLinks links;

    @Autowired
    private GroupMapper groupMapper;

    public GroupAssembler() {
        super(Group.class, GroupModel.class);
    }

    @Override
    public GroupModel toModel(Group entity) {
        GroupModel model = groupMapper.toModel(entity);

        model.add(links.linkToGroups());
        model.add(links.linkToGroup(entity.getId()));
        model.add(links.linkToGroupPermissions(entity.getId(), "permissions"));

        return model;
    }

    public CollectionModel<GroupModel> toCollection(Collection<Group> groups) {
        var models = groups.stream().map(this::toModel).toList();
        CollectionModel<GroupModel> collection = CollectionModel.of(models);
        collection.add(links.linkToGroups("groups"));
        return collection;
    }

    public CollectionModel<GroupModel> toCollectionRefUser(UUID userId, Collection<Group> groups) {
        var models = groups.stream().map(this::toModel).toList();
        CollectionModel<GroupModel> collection = CollectionModel.of(models);

        collection.forEach(groupModel ->
                groupModel.add(links.linkToUserGroupDissociation(userId, groupModel.getId(), "disassociate"))
        );

        collection.add(links.linkToGroups("groups"));
        collection.add(links.linkToUserGroupAssociation(userId, null, "associate"));

        return collection;
    }
}

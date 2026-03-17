package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.GroupMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.GroupModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.Group;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@AllArgsConstructor
public class GroupModelAssembler{

    private GroupMapper groupMapper;

    public GroupModel toModel(Group group) {
        return groupMapper.toModel(group);
    }

    public Collection<GroupModel> toCollectionModel(Collection<Group> entities) {
        return entities.stream().map((this::toModel)).toList();
    }

}
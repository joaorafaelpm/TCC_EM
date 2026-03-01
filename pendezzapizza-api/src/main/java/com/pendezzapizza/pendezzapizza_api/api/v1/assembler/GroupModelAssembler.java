package com.pendezzapizza.pendezzapizza_api.api.v1.assembler;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.mapper.GroupMapper;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.GroupModel;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class GroupModelAssembler{


    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private PendezzaPizzaSecurity pendezzaPizzaSecurity;

    public GroupModel toModel(Group group) {
        return groupMapper.toModel(group);
    }

    public Collection<GroupModel> toCollectionModel(Collection<Group> entities) {
        return entities.stream().map((this::toModel)).toList();
    }

}
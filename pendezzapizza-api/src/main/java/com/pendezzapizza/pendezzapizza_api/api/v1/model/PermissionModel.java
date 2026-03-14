package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.UUID;

@Relation(collectionRelation = "permissions")
@Getter
@Setter
@AllArgsConstructor
public class PermissionModel extends RepresentationModel<PermissionModel> {

    private UUID id;
    private String name;
    private String description;

}

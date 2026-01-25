package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.UUID;

@Relation(collectionRelation = "states")
@Getter
@Setter
@AllArgsConstructor
public class StateModel extends RepresentationModel<StateModel> {

    @Schema(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48")
    private UUID id;
    @Schema(example = "São Paulo")
    private String name;
}
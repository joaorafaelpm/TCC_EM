package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.UUID;

@Relation(collectionRelation = "users")
@Getter
@Setter
@AllArgsConstructor
public class UserModel extends RepresentationModel<UserModel> {

    @Schema(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48")
    private UUID id;
    @Schema(example = "Roberto")
    private String name;
    @Schema(example = "roberto@gmail.com")
    private String email;
}

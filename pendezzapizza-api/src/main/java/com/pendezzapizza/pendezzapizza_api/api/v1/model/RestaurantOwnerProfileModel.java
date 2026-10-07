package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import com.pendezzapizza.pendezzapizza_api.domain.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class RestaurantOwnerProfileModel {
    @Schema(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48")
    private UUID id;

    private User user;
}

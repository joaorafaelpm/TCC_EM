package com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserIdDTO {

    @NotNull
    private UUID id;

}

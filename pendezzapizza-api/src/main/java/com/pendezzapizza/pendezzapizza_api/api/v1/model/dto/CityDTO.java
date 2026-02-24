package com.pendezzapizza.pendezzapizza_api.api.v1.model.dto;

import com.pendezzapizza.pendezzapizza_api.core.validation.ValidationName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CityDTO {

    @Schema(example = "Campinas")
    @ValidationName
    private String name;

    @Valid
    @NotNull
    private StateIdDTO stateId;
}
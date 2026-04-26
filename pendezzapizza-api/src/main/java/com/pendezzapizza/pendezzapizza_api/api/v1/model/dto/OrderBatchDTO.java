package com.pendezzapizza.pendezzapizza_api.api.v1.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderBatchDTO(
    @NotEmpty
    @Valid
    List<OrderDTO> orders
) {}
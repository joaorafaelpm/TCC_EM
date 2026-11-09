package com.pendezzapizza.pendezzapizza_api.api.v1.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class StockEntryDTO {

    @NotNull
    @Positive
    private Long quantity;
}
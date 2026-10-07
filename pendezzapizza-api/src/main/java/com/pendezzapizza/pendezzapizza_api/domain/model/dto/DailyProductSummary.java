package com.pendezzapizza.pendezzapizza_api.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class DailyProductSummary {
    private UUID productId;
    private String productName;
    private Long totalQuantity;

    // Construtor para native query (recebe String do BIN_TO_UUID)
    public DailyProductSummary(String productId, String productName, Long totalQuantity) {
        this.productId = UUID.fromString(productId);
        this.productName = productName;
        this.totalQuantity = totalQuantity;
    }
}
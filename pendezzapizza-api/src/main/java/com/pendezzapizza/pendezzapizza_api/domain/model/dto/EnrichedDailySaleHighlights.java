package com.pendezzapizza.pendezzapizza_api.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EnrichedDailySaleHighlights {
    private DailyProductSummary topProduct;
    private DailyCustomerSummary topCustomer;
}
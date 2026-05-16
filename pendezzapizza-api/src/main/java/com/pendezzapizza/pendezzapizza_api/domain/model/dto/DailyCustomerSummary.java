package com.pendezzapizza.pendezzapizza_api.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class DailyCustomerSummary {
    private UUID customerId;
    private String customerName;
    private Long totalOrders;
}

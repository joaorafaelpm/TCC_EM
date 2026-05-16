package com.pendezzapizza.pendezzapizza_api.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DailyBreakdown {
    private Date date;
    private Long totalSales;
    private BigDecimal totalBilled;
    private BigDecimal averageTicket;
    private List<DailyProductSummary> products;
    private List<DailyCustomerSummary> customers;

    public DailyBreakdown(Date date, Long totalSales, BigDecimal totalBilled) {
        this.date = date;
        this.totalSales = totalSales;
        this.totalBilled = totalBilled;
        this.averageTicket = totalSales > 0
                ? totalBilled.divide(BigDecimal.valueOf(totalSales), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
    }
}
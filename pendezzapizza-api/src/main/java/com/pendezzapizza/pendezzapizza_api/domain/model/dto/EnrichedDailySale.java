package com.pendezzapizza.pendezzapizza_api.domain.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class EnrichedDailySale {

    private Date startDate;
    private Date endDate;
    private Long totalSales;
    private BigDecimal totalBilled;
    private BigDecimal averageTicket;
    private Date peakDay;
    private EnrichedDailySaleHighlights highlights;

    // null = não solicitado via ?include=
    private List<DailyProductSummary> products;
    private List<DailyCustomerSummary> customers;
    private List<DailyBreakdown> dailyBreakdown;

    public EnrichedDailySale(Date startDate, Date endDate, Long totalSales, BigDecimal totalBilled) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalSales = totalSales;
        this.totalBilled = totalBilled;
        this.averageTicket = totalSales > 0
                ? totalBilled.divide(BigDecimal.valueOf(totalSales), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
    }
}
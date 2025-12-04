package com.pendezzapizza.pendezzapizza_api.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
public class DailySale {

    private Date date;
    private Long totalSales ;
    private BigDecimal totalBilled ;

}

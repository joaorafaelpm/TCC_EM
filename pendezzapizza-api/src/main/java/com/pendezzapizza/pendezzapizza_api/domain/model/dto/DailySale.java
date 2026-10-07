package com.pendezzapizza.pendezzapizza_api.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Objeto de DTO auxiliar criado para servir de filtro para o endpoint de vendas
 *
 * <p>Acompanha só os parâmetros para filtragem</p>
 */
@AllArgsConstructor
@Setter
@Getter
public class DailySale {

    private Date date;
    private Long totalSales ;
    private BigDecimal totalBilled ;

}

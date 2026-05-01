package com.pendezzapizza.pendezzapizza_api.domain.filter;


import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Objeto de filtro de vendas
 *
 * <p>Usado para servir de filtro e para gerar o relatório em pdf</p>
 */
@Getter
@Setter
public class DailySalesFilter {

    private UUID restaurantId ;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime startCreationDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime endCreationDate;



}
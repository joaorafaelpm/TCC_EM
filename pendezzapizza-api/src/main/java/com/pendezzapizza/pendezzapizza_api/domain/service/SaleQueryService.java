package com.pendezzapizza.pendezzapizza_api.domain.service;


import com.pendezzapizza.pendezzapizza_api.domain.filter.DailySalesFilter;
import com.pendezzapizza.pendezzapizza_api.domain.model.dto.DailySale;
import com.pendezzapizza.pendezzapizza_api.domain.model.dto.EnrichedDailySale;
import com.pendezzapizza.pendezzapizza_api.domain.model.enuns.SaleIncludeField;

import java.util.List;
import java.util.Set;

public interface SaleQueryService {

    List<DailySale> viewDailySales(DailySalesFilter dailySalesFilter, String timeOffSet);

    EnrichedDailySale viewEnrichedDailySales(
            DailySalesFilter filter,
            String timeOffset,
            Set<SaleIncludeField> include
    );
}

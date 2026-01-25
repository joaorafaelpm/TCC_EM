package com.pendezzapizza.pendezzapizza_api.domain.service;


import com.pendezzapizza.pendezzapizza_api.domain.filter.DailySalesFilter;
import com.pendezzapizza.pendezzapizza_api.domain.model.dto.DailySale;

import java.util.List;

public interface SaleQueryService {

    List<DailySale> viewDailySales(DailySalesFilter dailySalesFilter, String timeOffSet);

}

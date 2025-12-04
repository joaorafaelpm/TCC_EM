package com.pendezzapizza.pendezzapizza_api.domain.service;


import com.pendezzapizza.pendezzapizza_api.domain.filter.DailySaleFilter;
import com.pendezzapizza.pendezzapizza_api.domain.model.dto.DailySale;

import java.util.List;

public interface SaleQueryService {

    List<DailySale> viewDailySales(DailySaleFilter dailySaleFilter, String timeOffSet);

}

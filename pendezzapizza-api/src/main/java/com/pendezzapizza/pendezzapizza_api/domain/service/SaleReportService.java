package com.pendezzapizza.pendezzapizza_api.domain.service;


import com.pendezzapizza.pendezzapizza_api.domain.filter.DailySalesFilter;

public interface SaleReportService {

    byte[] issueDailySales (DailySalesFilter dailySalesFilter, String timeOffSet);

}

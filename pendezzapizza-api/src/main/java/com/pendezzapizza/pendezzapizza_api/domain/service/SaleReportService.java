package com.pendezzapizza.pendezzapizza_api.domain.service;


import com.pendezzapizza.pendezzapizza_api.domain.filter.DailySaleFilter;

public interface SaleReportService {

    byte[] issueDailySales (DailySaleFilter dailySaleFilter , String timeOffSet);

}

package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.StatisticsModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.StatisticsControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.filter.DailySalesFilter;
import com.pendezzapizza.pendezzapizza_api.domain.model.dto.DailySale;
import com.pendezzapizza.pendezzapizza_api.domain.service.SaleQueryService;
import com.pendezzapizza.pendezzapizza_api.domain.service.SaleReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/statistics")
@AllArgsConstructor
public class StatisticsController implements StatisticsControllerOpenApi {

    private final SaleQueryService saleQueryService;
    private final SaleReportService saleReportService;

    @CheckSecurity.Statistics.CanConsult
    @GetMapping
    public StatisticsModel statistics() {
        StatisticsModel statisticsModel = new StatisticsModel();
        return statisticsModel;
    }

    @CheckSecurity.Statistics.CanConsult
    @GetMapping(path = "/daily-sales", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DailySale> consultDailySales(DailySalesFilter filter,
                                             @RequestParam(required = false, defaultValue = "+00:00") String timeOffset) {
        return saleQueryService.viewDailySales(filter, timeOffset);
    }

    @CheckSecurity.Statistics.CanConsult
    @GetMapping(path = "/daily-sales", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> consultDailySalesPdf(DailySalesFilter filter,
                                                       @RequestParam(required = false, defaultValue = "+00:00") String timeOffset) {

        byte[] bytesPdf = saleReportService.issueDailySales(filter, timeOffset);

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=daily-sales.pdf");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .headers(headers)
                .body(bytesPdf);
    }
}
package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaPizzaLinks;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.StatisticsModel;
import com.pendezzapizza.pendezzapizza_api.domain.filter.DailySaleFilter;
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
public class StatisticsController {

    private PendezzaPizzaLinks links;
    private SaleQueryService saleQueryService;
    private SaleReportService saleReportService;

    @GetMapping
    public StatisticsModel exposeLinks() {
        StatisticsModel statisticsModel = new StatisticsModel();
        statisticsModel.add(links.linkToDailySalesStatistics("daily-sales"));
        return statisticsModel;
    }

    @GetMapping(path = "/daily-sales", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DailySale> getDailySalesJson(
            DailySaleFilter filter,
            @RequestParam(required = false, defaultValue = "+00:00") String timeOffset) {
        return saleQueryService.viewDailySales(filter, timeOffset);
    }

    @GetMapping(path = "/daily-sales", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getDailySalesPdf(
            DailySaleFilter filter,
            @RequestParam(required = false, defaultValue = "+00:00") String timeOffset) {

        byte[] pdfBytes = saleReportService.issueDailySales(filter, timeOffset);

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=daily-sales.pdf");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .headers(headers)
                .body(pdfBytes);
    }
}

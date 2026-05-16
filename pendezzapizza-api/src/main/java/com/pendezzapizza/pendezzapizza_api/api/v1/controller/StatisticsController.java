package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.StatisticsControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.filter.DailySalesFilter;
import com.pendezzapizza.pendezzapizza_api.domain.model.dto.DailySale;
import com.pendezzapizza.pendezzapizza_api.domain.model.dto.EnrichedDailySale;
import com.pendezzapizza.pendezzapizza_api.domain.model.enuns.SaleIncludeField;
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
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/statistics")
@AllArgsConstructor
public class StatisticsController implements StatisticsControllerOpenApi {

    private final SaleQueryService saleQueryService;
    private final SaleReportService saleReportService;

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

    @CheckSecurity.Statistics.CanConsult
    @GetMapping(path = "/daily-sales/enriched", produces = MediaType.APPLICATION_JSON_VALUE)
    public EnrichedDailySale consultEnrichedDailySales(
            DailySalesFilter filter,
            @RequestParam(required = false, defaultValue = "+00:00") String timeOffset,
            @RequestParam(required = false, defaultValue = "") List<String> include) {

        // Converte ["products", "customers"] → Set<SaleIncludeField>
        // Valores inválidos são silenciosamente ignorados para não quebrar o front
        Set<SaleIncludeField> includeFields = include.stream()
                .filter(s -> !s.isBlank())
                .flatMap(s -> {
                    try {
                        return java.util.stream.Stream.of(SaleIncludeField.fromString(s));
                    } catch (IllegalArgumentException e) {
                        return java.util.stream.Stream.empty(); // ignora campo desconhecido
                    }
                })
                .collect(Collectors.toSet());

        return saleQueryService.viewEnrichedDailySales(filter, timeOffset, includeFields);
    }

}
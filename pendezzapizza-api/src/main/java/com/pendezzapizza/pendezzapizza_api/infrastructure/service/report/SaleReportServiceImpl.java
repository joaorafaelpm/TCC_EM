package com.pendezzapizza.pendezzapizza_api.infrastructure.service.report;

import com.pendezzapizza.pendezzapizza_api.domain.filter.DailySaleFilter;
import com.pendezzapizza.pendezzapizza_api.domain.service.SaleQueryService;
import com.pendezzapizza.pendezzapizza_api.domain.service.SaleReportService;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;

@Service
public class SaleReportServiceImpl implements SaleReportService {

    @Autowired
    private SaleQueryService vendaQueryService;

    @Override
    public byte[] issueDailySales(DailySaleFilter dailySaleFilter, String timeOffSet){
        try {
            var inputStream = this.getClass().getResourceAsStream("/relatorios/vendas-diarias.jasper");

            var params = new HashMap<String , Object>();
            params.put("REPORT_LOCALE" , Locale.of("pt" , "BR"));

            var dailySales = vendaQueryService.viewDailySales(dailySaleFilter , timeOffSet);
            var dataSource = new JRBeanCollectionDataSource(dailySales);

            var jasperPrint = JasperFillManager.fillReport(inputStream , params , dataSource);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            throw new ReportException("Não foi possivel emitir relatório de vendas diárias." , e);
        }
    }
}

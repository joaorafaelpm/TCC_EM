/**
 * @summary     Implementação do serviço de emissão de relatórios de vendas, gerando PDFs
 *              a partir de templates JasperReports compilados e dados consultados em tempo real.
 * @difficulty  Medium
 * @depends-on  None
 */
package com.pendezzapizza.pendezzapizza_api.infrastructure.service.report;

import com.pendezzapizza.pendezzapizza_api.domain.filter.DailySalesFilter;
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

    /**
     * Emite o relatório de vendas diárias em formato PDF.
     * Carrega o template Jasper pré-compilado (.jasper) do classpath, injeta os dados
     * de vendas filtrados e retorna o PDF serializado como array de bytes,
     * pronto para ser enviado como resposta HTTP ou salvo em disco.
     *
     * @param dailySalesFilter critérios de filtragem das vendas (ex: intervalo de datas)
     * @param timeOffSet       fuso horário do cliente, usado para ajustar os horários das vendas
     *                         antes de popular o relatório
     * @return array de bytes contendo o PDF gerado
     * @throws ReportException se ocorrer qualquer falha ao carregar o template,
     *                         preencher os dados ou exportar o PDF
     */
    @Override
    public byte[] issueDailySales(DailySalesFilter dailySalesFilter, String timeOffSet) {
        try {
            // O arquivo .jasper é o template já compilado pelo JasperReports;
            // nunca referenciar o .jrxml em produção, pois exigiria compilação em tempo de execução.
            var inputStream = this.getClass().getResourceAsStream("/relatorios/pendezzapizza.jasper");

            var params = new HashMap<String, Object>();
            // Força localidade pt-BR para garantir formatação correta de datas,
            // números e moeda dentro do template Jasper, independente do locale da JVM.
            params.put("REPORT_LOCALE", Locale.of("pt", "BR"));

            var dailySales = vendaQueryService.viewDailySales(dailySalesFilter, timeOffSet);
            // JRBeanCollectionDataSource expõe a lista de objetos Java como fonte de dados
            // navegável pelo motor do JasperReports durante o preenchimento do template.
            var dataSource = new JRBeanCollectionDataSource(dailySales);

            var jasperPrint = JasperFillManager.fillReport(inputStream, params, dataSource);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            throw new ReportException("Não foi possivel emitir relatório de vendas diárias.", e);
        }
    }
}
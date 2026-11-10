import React, { useRef, useState } from 'react';
import api from '../../../../services/api';
import { formatQty, getErrorMessage } from '../stockUtils';
import {
  formatBRL,
  formatDateBR,
  formatDateTimeBR,
  formatSigned,
  periodParams,
} from './fullReportUtils';
import { exportStockReportPDF } from './stockPdfExporter';
import './FullReport.css';

/* ------------------------------------------------------------------ */
/* Conteúdo do relatório (o que aparece na prévia e vai para o PDF).   */
/* Cada seção com data-pdf-block vira um bloco paginável no PDF.       */
/* Dados de clientes NÃO entram neste relatório.                       */
/* ------------------------------------------------------------------ */
const FullReport = ({ data }) => {
  const { restaurantName, start, end, generatedAt, sales, salesFailed, summary, capacity } = data;

  const lowCount = summary.filter((l) => l.currentStock <= l.lowStockThreshold).length;
  const toBuy = summary
    .filter((l) => l.consumed > 0 || l.suggestedPurchase > 0)
    .sort((a, b) => b.suggestedPurchase - a.suggestedPurchase || b.consumed - a.consumed);
  const products = [...(sales?.products ?? [])]
    .sort((a, b) => (b.totalQuantity ?? 0) - (a.totalQuantity ?? 0));
  const days = [...(sales?.dailyBreakdown ?? [])]
    .sort((a, b) => String(a.date).localeCompare(String(b.date)));

  return (
    <>
      {/* ---------- Cabeçalho ---------- */}
      <header className="fr-block fr-header" data-pdf-block="true">
        <h1>Relatório completo</h1>
        <p className="fr-header__sub">Vendas e estoque</p>
        <div className="fr-header__meta">
          <span><strong>Restaurante:</strong> {restaurantName}</span>
          <span><strong>Período:</strong> {formatDateBR(start)} a {formatDateBR(end)}</span>
          <span><strong>Gerado em:</strong> {formatDateTimeBR(generatedAt)}</span>
        </div>
      </header>

      {/* ---------- Resumo ---------- */}
      <section className="fr-block" data-pdf-block="true">
        <h2>Resumo do período</h2>
        <div className="fr-cards">
          <div className="fr-card">
            <span>Total faturado</span>
            <strong>{sales ? formatBRL(sales.totalBilled) : '—'}</strong>
          </div>
          <div className="fr-card">
            <span>Pedidos</span>
            <strong>{sales ? (sales.totalSales ?? 0) : '—'}</strong>
          </div>
          <div className="fr-card">
            <span>Ticket médio</span>
            <strong>{sales ? formatBRL(sales.averageTicket) : '—'}</strong>
          </div>
          <div className="fr-card">
            <span>Estoque baixo</span>
            <strong>{lowCount} {lowCount === 1 ? 'item' : 'itens'}</strong>
          </div>
          <div className="fr-card">
            <span>A repor</span>
            <strong>{toBuy.length} {toBuy.length === 1 ? 'item' : 'itens'}</strong>
          </div>
        </div>
        {salesFailed && (
          <p className="fr-warn">
            Não foi possível carregar os dados de vendas deste período. As seções de estoque estão completas.
          </p>
        )}
      </section>

      {/* ---------- Vendas ---------- */}
      {sales && (
        <>
          <section className="fr-block" data-pdf-block="true">
            <h2>Produtos vendidos</h2>
            {products.length === 0 ? (
              <p className="fr-muted">Nenhuma venda no período.</p>
            ) : (
              <table className="fr-table">
                <thead>
                  <tr><th>Produto</th><th className="fr-num">Quantidade</th></tr>
                </thead>
                <tbody>
                  {products.map((p) => (
                    <tr key={p.productName}>
                      <td>{p.productName ?? '—'}</td>
                      <td className="fr-num">{(p.totalQuantity ?? 0).toLocaleString('pt-BR')} un.</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </section>

          <section className="fr-block" data-pdf-block="true">
            <h2>Faturamento por dia</h2>
            {days.length === 0 ? (
              <p className="fr-muted">Nenhum pedido encontrado no período.</p>
            ) : (
              <table className="fr-table">
                <thead>
                  <tr>
                    <th>Data</th>
                    <th className="fr-num">Pedidos</th>
                    <th className="fr-num">Faturado</th>
                    <th className="fr-num">Ticket médio</th>
                  </tr>
                </thead>
                <tbody>
                  {days.map((d) => (
                    <tr key={d.date}>
                      <td>{formatDateBR(d.date)}</td>
                      <td className="fr-num">{d.totalSales ?? 0}</td>
                      <td className="fr-num">{formatBRL(d.totalBilled)}</td>
                      <td className="fr-num">{formatBRL(d.averageTicket)}</td>
                    </tr>
                  ))}
                </tbody>
                <tfoot>
                  <tr>
                    <td>Total</td>
                    <td className="fr-num">{sales.totalSales ?? 0}</td>
                    <td className="fr-num">{formatBRL(sales.totalBilled)}</td>
                    <td className="fr-num">{formatBRL(sales.averageTicket)}</td>
                  </tr>
                </tfoot>
              </table>
            )}
            {sales.peakDay && <p className="fr-muted">Dia de pico: {formatDateBR(sales.peakDay)}</p>}
          </section>
        </>
      )}

      {/* ---------- Estoque no momento ---------- */}
      <section className="fr-block" data-pdf-block="true">
        <h2>Estoque no momento</h2>
        {summary.length === 0 ? (
          <p className="fr-muted">Nenhum ingrediente cadastrado.</p>
        ) : (
          <table className="fr-table">
            <thead>
              <tr>
                <th>Ingrediente</th>
                <th className="fr-num">Saldo atual</th>
                <th className="fr-num">Mínimo</th>
                <th>Situação</th>
              </tr>
            </thead>
            <tbody>
              {summary.map((l) => {
                const low = l.currentStock <= l.lowStockThreshold;
                return (
                  <tr key={l.ingredientId} className={low ? 'fr-low' : ''}>
                    <td>{l.name}</td>
                    <td className="fr-num fr-strong">{formatQty(l.currentStock, l.unit)}</td>
                    <td className="fr-num">{formatQty(l.lowStockThreshold, l.unit)}</td>
                    <td>{low ? 'Estoque baixo' : 'OK'}</td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        )}
      </section>

      <section className="fr-block" data-pdf-block="true">
        <h2>Capacidade de produção (com o estoque atual)</h2>
        {capacity.length === 0 ? (
          <p className="fr-muted">Nenhum produto com receita cadastrada.</p>
        ) : (
          <table className="fr-table">
            <thead>
              <tr><th>Produto</th><th className="fr-num">Unidades possíveis</th></tr>
            </thead>
            <tbody>
              {capacity.map((c) => (
                <tr key={c.productId} className={c.portions === 0 ? 'fr-low' : ''}>
                  <td>{c.productName}</td>
                  <td className="fr-num">{c.portions === 0 ? 'Esgotado' : c.portions.toLocaleString('pt-BR')}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
        <p className="fr-muted">Cada produto é calculado isoladamente; ingredientes compartilhados não são divididos.</p>
      </section>

      {/* ---------- Antes e depois ---------- */}
      <section className="fr-block" data-pdf-block="true">
        <h2>Antes e depois (movimentação do período)</h2>
        {summary.length === 0 ? (
          <p className="fr-muted">Nenhum ingrediente cadastrado.</p>
        ) : (
          <table className="fr-table fr-table--compact">
            <thead>
              <tr>
                <th>Ingrediente</th>
                <th className="fr-num">Saldo inicial (antes)</th>
                <th className="fr-num">Compras (+)</th>
                <th className="fr-num">Consumo (−)</th>
                <th className="fr-num">Ajustes (±)</th>
                <th className="fr-num">Saldo final (depois)</th>
                <th>Contagem física</th>
              </tr>
            </thead>
            <tbody>
              {summary.map((l) => (
                <tr key={l.ingredientId}>
                  <td>{l.name}</td>
                  <td className="fr-num">{formatQty(l.openingBalance, l.unit)}</td>
                  <td className="fr-num">{formatQty(l.entries, l.unit)}</td>
                  <td className="fr-num">{formatQty(l.consumed, l.unit)}</td>
                  <td className="fr-num">{formatSigned(l.adjustments, l.unit)}</td>
                  <td className="fr-num fr-strong">{formatQty(l.closingBalance, l.unit)}</td>
                  <td className="fr-blank" />
                </tr>
              ))}
            </tbody>
          </table>
        )}
        <p className="fr-muted">
          Saldo final = saldo inicial + compras − consumo ± ajustes. Use a coluna "Contagem física"
          para conferir com o estoque real.
        </p>
      </section>

      {/* ---------- Reposição ---------- */}
      <section className="fr-block" data-pdf-block="true">
        <h2>Reposição (o que comprar)</h2>
        {toBuy.length === 0 ? (
          <p className="fr-muted">Nada a repor neste período.</p>
        ) : (
          <table className="fr-table">
            <thead>
              <tr>
                <th>Ingrediente</th>
                <th className="fr-num">Repor (consumido no período)</th>
                <th className="fr-num">Sugestão de compra</th>
                <th className="fr-num">Saldo atual</th>
              </tr>
            </thead>
            <tbody>
              {toBuy.map((l) => (
                <tr key={l.ingredientId}>
                  <td>{l.name}</td>
                  <td className="fr-num fr-strong">{formatQty(Math.max(0, l.consumed), l.unit)}</td>
                  <td className="fr-num">{formatQty(l.suggestedPurchase, l.unit)}</td>
                  <td className="fr-num">{formatQty(l.currentStock, l.unit)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
        <p className="fr-muted">
          "Repor" é a quantidade que saiu e precisa ser comprada para voltar ao nível anterior.
          A sugestão considera também o estoque mínimo.
        </p>
      </section>

      {/* ---------- Notas ---------- */}
      <section className="fr-block" data-pdf-block="true">
        <h2>Notas</h2>
        <ul className="fr-notes">
          <li>O consumo de ingredientes é registrado quando o pedido é criado e devolvido se for cancelado.</li>
          <li>Vendas seguem o relatório de vendas do sistema; ambos usam o mesmo recorte de datas.</li>
          <li>Este relatório não contém dados de clientes.</li>
        </ul>
      </section>
    </>
  );
};

/* ------------------------------------------------------------------ */
/* Painel: botão, busca dos dados, prévia e download                   */
/* ------------------------------------------------------------------ */
const FullReportPanel = ({ restaurantId, start, end }) => {
  const base = `/v1/restaurants/${restaurantId}/stock`;
  const reportRef = useRef(null);
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [exporting, setExporting] = useState(false);
  const [error, setError] = useState('');

  const generate = async () => {
    if (!start || !end || start > end) {
      setError('Período inválido: a data inicial deve ser anterior (ou igual) à final.');
      return;
    }
    setLoading(true);
    setError('');
    setData(null);

    // Vendas: mesmo endpoint e mesmo formato de datas do relatório de vendas (sem clientes)
    const salesQuery = new URLSearchParams({
      restaurantId,
      startCreationDate: `${start}T00:00:00+00:00`,
      endCreationDate: `${end}T23:59:59+00:00`,
    });
    salesQuery.append('include', 'products');

    try {
      const [salesRes, summaryRes, capacityRes, restaurantRes] = await Promise.allSettled([
        api.get(`/v1/statistics/daily-sales/enriched?${salesQuery}`),
        api.get(`${base}/report/summary`, { params: periodParams(start, end) }),
        api.get(`${base}/capacity`),
        api.get(`/v1/restaurants/${restaurantId}`),
      ]);

      // Sem o resumo de estoque não há relatório; vendas e demais dados são opcionais
      if (summaryRes.status === 'rejected') {
        setError(getErrorMessage(summaryRes.reason, 'Não foi possível gerar o resumo de estoque.'));
        return;
      }

      setData({
        restaurantName: restaurantRes.status === 'fulfilled'
          ? (restaurantRes.value.data?.name ?? 'Restaurante')
          : 'Restaurante',
        start,
        end,
        generatedAt: new Date(),
        sales: salesRes.status === 'fulfilled' ? salesRes.value.data : null,
        salesFailed: salesRes.status === 'rejected',
        summary: Array.isArray(summaryRes.value.data) ? summaryRes.value.data : [],
        capacity: capacityRes.status === 'fulfilled' && Array.isArray(capacityRes.value.data)
          ? capacityRes.value.data
          : [],
      });
    } finally {
      setLoading(false);
    }
  };

  const handleDownload = async () => {
    setExporting(true);
    try {
      await exportStockReportPDF({
        container: reportRef.current,
        restaurantName: data.restaurantName,
        startDate: data.start,
        endDate: data.end,
      });
    } catch (err) {
      console.error('Erro ao exportar PDF:', err);
      setError('Não foi possível gerar o PDF.');
    } finally {
      setExporting(false);
    }
  };

  return (
    <div className="stock-card">
      <h3>Relatório completo (vendas + estoque)</h3>
      <p className="stock-hint">
        Junta vendas, estoque atual, antes e depois e reposição do período selecionado acima, em um único PDF.
        Não inclui dados de clientes.
      </p>

      {error && <p className="stock-banner stock-banner--error">⚠️ {error}</p>}

      <div className="fr-actions">
        <button className="stock-btn stock-btn--primary" onClick={generate} disabled={loading}>
          {loading ? 'Gerando...' : 'Gerar relatório completo'}
        </button>
        {data && (
          <>
            <button className="stock-btn stock-btn--ghost" onClick={handleDownload} disabled={exporting}>
              {exporting ? 'Exportando...' : 'Baixar PDF'}
            </button>
            <button className="stock-btn stock-btn--ghost" onClick={() => setData(null)} disabled={exporting}>
              Fechar
            </button>
          </>
        )}
      </div>

      {data && (
        <div className="fr-scroll">
          <div className="fr-root" ref={reportRef}>
            <FullReport data={data} />
          </div>
        </div>
      )}
    </div>
  );
};

export default FullReportPanel;

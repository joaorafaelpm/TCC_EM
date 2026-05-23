import React, { useState, useEffect, useCallback, useRef } from 'react';

import { fmt, fmtDate, today, daysAgo } from './utils/formatter';
import { exportReportPDF } from './utils/pdfExporter';

import FilterBar     from './components/FilterBar';
import LoadingState  from './components/LoadingState';
import MetricCard    from './components/MetricCard';
import SectionCard   from './components/SectionCard';
import HighlightCard from './components/HighlightCard';
import RevenueChart  from './components/RevenueChart';
import ProductsChart from './components/ProductChart';
import DailyTable    from './components/DailyTable';

import './SalesReport.css';

// Converte "2026-05-16" → "2026-05-16T00:00:00+00:00" que o DailySalesFilter espera
const toISOParam = (dateStr, endOfDay = false) => {
  const time = endOfDay ? 'T23:59:59+00:00' : 'T00:00:00+00:00';
  return `${dateStr}${time}`;
};

const SalesReport = ({ userId }) => {
  const [restaurants, setRestaurants]               = useState([]);
  const [restaurantsLoading, setRestaurantsLoading] = useState(true);
  const [selectedId, setSelectedId]                 = useState('');
  const [startDate, setStartDate]                   = useState(daysAgo(7));
  const [endDate, setEndDate]                       = useState(today());
  const [includeProducts, setIncludeProducts]       = useState(true);
  const [includeCustomers, setIncludeCustomers]     = useState(true);
  const [report, setReport]                         = useState(null);
  const [loading, setLoading]                       = useState(false);
  const [error, setError]                           = useState(null);
  const [exporting, setExporting]                   = useState(false);
  const reportRef = useRef(null);

  // ── load restaurants ──────────────────────────────────────────────────────

  useEffect(() => {
    if (!userId) return;
    fetch(`/v1/users/${userId}/restaurants`, { credentials: 'include' })
      .then((r) => { if (!r.ok) throw new Error('Erro ao carregar restaurantes.'); return r.json(); })
      .then((data) => {
        const list = data.content ?? data ?? [];
        setRestaurants(list);
        if (list.length > 0) setSelectedId(list[0].id);
      })
      .catch((e) => setError(e.message))
      .finally(() => setRestaurantsLoading(false));
  }, [userId]);

  // ── fetch report ──────────────────────────────────────────────────────────

  const fetchReport = useCallback(() => {
    if (!selectedId) return;
    setLoading(true);
    setError(null);

    const params = new URLSearchParams({
      restaurantId:        selectedId,
      startCreationDate:   toISOParam(startDate, false),
      endCreationDate:     toISOParam(endDate, true),
    });

    if (includeProducts)  params.append('include', 'products');
    if (includeCustomers) params.append('include', 'customers');

    fetch(`/v1/statistics/daily-sales/enriched?${params}`, { credentials: 'include' })
      .then((r) => { if (!r.ok) throw new Error('Erro ao carregar relatório.'); return r.json(); })
      .then(setReport)
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false));
  }, [selectedId, startDate, endDate, includeProducts, includeCustomers]);

  useEffect(() => {
    if (selectedId) fetchReport();
  }, [selectedId]); // eslint-disable-line react-hooks/exhaustive-deps

  // ── export PDF ────────────────────────────────────────────────────────────

  const handleExport = async () => {
    setExporting(true);
    try {
      const restaurantName = restaurants.find((r) => r.id === selectedId)?.name ?? 'restaurante';
      await exportReportPDF({ ref: reportRef, restaurantName, startDate, endDate });
    } catch (e) {
      console.error('Erro ao exportar PDF:', e);
    }
    setExporting(false);
  };

  // ── derived chart data (null-safe) ────────────────────────────────────────

  const dailyData = (report?.dailyBreakdown ?? [])
    .slice()
    .sort((a, b) => a.date.localeCompare(b.date))
    .map((d) => ({
      date:           fmtDate(d.date),
      Faturado:       d.totalBilled       ?? 0,
      'Ticket médio': d.averageTicket     ?? 0,
    }));

  const productData = (report?.products ?? [])
    .slice()
    .sort((a, b) => (b.totalQuantity ?? 0) - (a.totalQuantity ?? 0))
    .map((p) => ({ name: p.productName ?? '—', Quantidade: p.totalQuantity ?? 0 }));

  const selectedName = restaurants.find((r) => r.id === selectedId)?.name ?? '';

  // ── metrics (null-safe) ───────────────────────────────────────────────────

  const metrics = report
    ? [
        { label: 'Total faturado', value: fmt(report.totalBilled  ?? 0) },
        { label: 'Pedidos',        value: report.totalSales        ?? 0  },
        { label: 'Ticket médio',   value: fmt(report.averageTicket ?? 0) },
        { label: 'Dia de pico',    value: report.peakDay ? fmtDate(report.peakDay) : '—' },
      ]
    : [];

  // ── early returns ─────────────────────────────────────────────────────────

  if (restaurantsLoading) return <LoadingState message="Carregando restaurantes…" />;

  if (restaurants.length === 0 && !error) {
    return <div className="sr-empty"><p>Você ainda não possui restaurantes cadastrados.</p></div>;
  }

  // ── render ────────────────────────────────────────────────────────────────

  return (
    <div className="sr-wrapper">

      <div className="sr-header">
        <div>
          <h2 className="sr-header__title">Relatório de vendas</h2>
          {selectedName && <p className="sr-header__sub">{selectedName}</p>}
        </div>
        {report && (
          <button className="sr-btn sr-btn--export" onClick={handleExport} disabled={exporting}>
            <svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 24 24"
              fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round"
              strokeLinejoin="round" aria-hidden="true">
              <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
              <polyline points="7 10 12 15 17 10" />
              <line x1="12" y1="15" x2="12" y2="3" />
            </svg>
            {exporting ? 'Exportando…' : 'Exportar PDF'}
          </button>
        )}
      </div>

      <FilterBar
        restaurants={restaurants}
        selectedId={selectedId}
        onSelectId={setSelectedId}
        startDate={startDate}
        endDate={endDate}
        onStartDate={setStartDate}
        onEndDate={setEndDate}
        includeProducts={includeProducts}
        onIncludeProducts={setIncludeProducts}
        includeCustomers={includeCustomers}
        onIncludeCustomers={setIncludeCustomers}
        onApply={fetchReport}
        loading={loading}
      />

      {error && <p className="sr-error">⚠️ {error}</p>}
      {loading && <LoadingState message="Carregando relatório…" />}

      {report && !loading && (
        <div className="sr-body" ref={reportRef}>

          {/* métricas */}
          {metrics.length > 0 && (
            <div className="sr-metrics">
              {metrics.map(({ label, value }) => (
                <MetricCard key={label} label={label} value={value} />
              ))}
            </div>
          )}

          {/* sem pedidos */}
          {(report.totalSales ?? 0) === 0 && (
            <p className="sr-muted">Nenhum pedido encontrado para o período selecionado.</p>
          )}

          {/* gráfico de receita — só aparece se houver dados */}
          {dailyData.length > 0 && (
            <SectionCard title="Receita e ticket médio por dia">
              <RevenueChart data={dailyData} />
            </SectionCard>
          )}

          {/* produtos — só aparece se incluído e com dados */}
          {includeProducts && productData.length > 0 && (
            <SectionCard title="Produtos mais vendidos">
              <ProductsChart data={productData} />
            </SectionCard>
          )}

          {/* destaques */}
          {(report.highlights?.topProduct || report.highlights?.topCustomer) && (
            <div className="sr-highlights">
              {includeProducts && report.highlights?.topProduct && (
                <HighlightCard
                  icon="🏆"
                  label="Produto destaque"
                  name={report.highlights.topProduct.productName ?? '—'}
                  sub={`${report.highlights.topProduct.totalQuantity ?? 0} unidades vendidas`}
                />
              )}
              {includeCustomers && report.highlights?.topCustomer && (
                <HighlightCard
                  icon="⭐"
                  label="Cliente frequente"
                  name={report.highlights.topCustomer.customerName ?? '—'}
                  sub={`${report.highlights.topCustomer.totalOrders ?? 0} pedido(s) no período`}
                />
              )}
            </div>
          )}

          {/* detalhamento diário */}
          {(report.dailyBreakdown ?? []).length > 0 && (
            <SectionCard title="Detalhamento diário">
              <DailyTable breakdown={report.dailyBreakdown} />
            </SectionCard>
          )}

        </div>
      )}

      {!report && !loading && !error && (
        <p className="sr-muted">Selecione um restaurante e clique em Aplicar.</p>
      )}

    </div>
  );
};

export default SalesReport;
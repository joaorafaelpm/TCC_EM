import React, { useState, useEffect, useCallback, useRef } from 'react';

import { fmt, fmtDate, today, daysAgo } from './utils/formatter';
import { exportReportPDF } from './utils/pdfExporter';

import FilterBar    from './components/FilterBar';
import LoadingState from './components/LoadingState';
import MetricCard   from './components/MetricCard';
import SectionCard  from './components/SectionCard';
import HighlightCard from './components/HighlightCard';
import RevenueChart from './components/RevenueChart';
import ProductsChart from './components/ProductChart';
import DailyTable   from './components/DailyTable';

import './SalesReport.css';

const SalesReport = ({ userId }) => {
  const [restaurants, setRestaurants]           = useState([]);
  const [restaurantsLoading, setRestaurantsLoading] = useState(true);
  const [selectedId, setSelectedId]             = useState('');
  const [startDate, setStartDate]               = useState(daysAgo(7));
  const [endDate, setEndDate]                   = useState(today());
  const [report, setReport]                     = useState(null);
  const [loading, setLoading]                   = useState(false);
  const [error, setError]                       = useState(null);
  const [exporting, setExporting]               = useState(false);
  const reportRef = useRef(null);

  // ── load restaurants ────────────────────────────────────────────────────────

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

  // ── fetch report ────────────────────────────────────────────────────────────

  const fetchReport = useCallback(() => {
    if (!selectedId) return;
    setLoading(true);
    setError(null);

    const params = new URLSearchParams({ restaurantId: selectedId, startDate, endDate });
    params.append('include', 'products');
    params.append('include', 'customers');

    fetch(`/v1/statistics/daily-sales/enriched?${params}`, { credentials: 'include' })
      .then((r) => { if (!r.ok) throw new Error('Erro ao carregar relatório.'); return r.json(); })
      .then(setReport)
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false));
  }, [selectedId, startDate, endDate]);

  useEffect(() => {
    if (selectedId) fetchReport();
  }, [selectedId]); // eslint-disable-line react-hooks/exhaustive-deps

  // ── export PDF ──────────────────────────────────────────────────────────────

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

  // ── derived chart data ──────────────────────────────────────────────────────

  const dailyData = (report?.dailyBreakdown ?? [])
    .slice()
    .sort((a, b) => a.date.localeCompare(b.date))
    .map((d) => ({ date: fmtDate(d.date), Faturado: d.totalBilled, 'Ticket médio': d.averageTicket }));

  const productData = (report?.products ?? [])
    .slice()
    .sort((a, b) => b.totalQuantity - a.totalQuantity)
    .map((p) => ({ name: p.productName, Quantidade: p.totalQuantity }));

  const selectedName = restaurants.find((r) => r.id === selectedId)?.name ?? '';

  // ── early returns ───────────────────────────────────────────────────────────

  if (restaurantsLoading) return <LoadingState message="Carregando restaurantes…" />;

  if (restaurants.length === 0 && !error) {
    return <div className="sr-empty"><p>Você ainda não possui restaurantes cadastrados.</p></div>;
  }

  // ── render ──────────────────────────────────────────────────────────────────

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
        onApply={fetchReport}
        loading={loading}
      />

      {error && <p className="sr-error">⚠️ {error}</p>}
      {loading && <LoadingState message="Carregando relatório…" />}

      {report && !loading && (
        <div className="sr-body" ref={reportRef}>

          <div className="sr-metrics">
            {[
              { label: 'Total faturado', value: fmt(report.totalBilled) },
              { label: 'Pedidos',        value: report.totalSales },
              { label: 'Ticket médio',   value: fmt(report.averageTicket) },
              { label: 'Dia de pico',    value: fmtDate(report.peakDay) },
            ].map(({ label, value }) => (
              <MetricCard key={label} label={label} value={value} />
            ))}
          </div>

          <SectionCard title="Receita e ticket médio por dia">
            <RevenueChart data={dailyData} />
          </SectionCard>

          {productData.length > 0 && (
            <SectionCard title="Produtos mais vendidos">
              <ProductsChart data={productData} />
            </SectionCard>
          )}

          <div className="sr-highlights">
            {report.highlights?.topProduct && (
              <HighlightCard
                icon="🏆"
                label="Produto destaque"
                name={report.highlights.topProduct.productName}
                sub={`${report.highlights.topProduct.totalQuantity} unidades vendidas`}
              />
            )}
            {report.highlights?.topCustomer && (
              <HighlightCard
                icon="⭐"
                label="Cliente frequente"
                name={report.highlights.topCustomer.customerName}
                sub={`${report.highlights.topCustomer.totalOrders} pedido(s) no período`}
              />
            )}
          </div>

          {report.dailyBreakdown?.length > 0 && (
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
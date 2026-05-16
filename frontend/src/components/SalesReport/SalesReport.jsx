import React, { useState, useEffect, useCallback, useRef } from 'react';
import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip,
  ResponsiveContainer, Legend,
} from 'recharts';
import './SalesReport.css';

// ── helpers ───────────────────────────────────────────────────────────────────

const fmt = (n) =>
  new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(n);

const fmtDate = (iso) => {
  const [, m, d] = iso.split('-');
  return `${d}/${m}`;
};

const toInputDate = (date) => date.toISOString().split('T')[0];
const today = () => toInputDate(new Date());
const daysAgo = (n) => {
  const d = new Date();
  d.setDate(d.getDate() - n);
  return toInputDate(d);
};

const loadScript = (src) =>
  new Promise((resolve, reject) => {
    const s = document.createElement('script');
    s.src = src;
    s.onload = resolve;
    s.onerror = reject;
    document.head.appendChild(s);
  });

// ── sub-components ────────────────────────────────────────────────────────────

const CustomTooltip = ({ active, payload, label }) => {
  if (!active || !payload?.length) return null;
  return (
    <div className="sr-tooltip">
      <p className="sr-tooltip__label">{label}</p>
      {payload.map((p) => (
        <p key={p.dataKey} className="sr-tooltip__item">
          <span className="sr-tooltip__dot" style={{ background: p.color }} />
          {p.name}: <strong>{p.dataKey === 'Quantidade' ? `${p.value} un.` : fmt(p.value)}</strong>
        </p>
      ))}
    </div>
  );
};

const LoadingState = ({ message }) => (
  <div className="sr-loading">
    <div className="sr-loading__spinner" />
    <p>{message}</p>
  </div>
);

const MetricCard = ({ label, value }) => (
  <div className="sr-metric-card">
    <span className="sr-metric-card__label">{label}</span>
    <span className="sr-metric-card__value">{value}</span>
  </div>
);

const SectionCard = ({ title, children }) => (
  <div className="sr-card">
    <p className="sr-card__title">{title}</p>
    {children}
  </div>
);

const HighlightCard = ({ icon, label, name, sub, mono }) => (
  <div className="sr-highlight-card">
    <span className="sr-highlight-card__icon" aria-hidden="true">{icon}</span>
    <div>
      <p className="sr-highlight-card__label">{label}</p>
      <p className={`sr-highlight-card__name${mono ? ' sr-highlight-card__name--mono' : ''}`}>{name}</p>
      <p className="sr-highlight-card__sub">{sub}</p>
    </div>
  </div>
);

const FilterBar = ({ restaurants, selectedId, onSelectId, startDate, endDate, onStartDate, onEndDate, onApply, loading }) => (
  <div className="sr-filters">
    <div className="sr-filters__group">
      <label className="sr-filters__label">Restaurante</label>
      <select className="sr-filters__select" value={selectedId} onChange={(e) => onSelectId(e.target.value)}>
        {restaurants.map((r) => (
          <option key={r.id} value={r.id}>{r.name}</option>
        ))}
      </select>
    </div>
    <div className="sr-filters__group">
      <label className="sr-filters__label">De</label>
      <input type="date" className="sr-filters__input" value={startDate} max={endDate} onChange={(e) => onStartDate(e.target.value)} />
    </div>
    <div className="sr-filters__group">
      <label className="sr-filters__label">Até</label>
      <input type="date" className="sr-filters__input" value={endDate} min={startDate} max={today()} onChange={(e) => onEndDate(e.target.value)} />
    </div>
    <button className="sr-btn sr-btn--apply" onClick={onApply} disabled={loading}>
      {loading ? 'Buscando…' : 'Aplicar'}
    </button>
  </div>
);

const RevenueChart = ({ data }) => (
  <ResponsiveContainer width="100%" height={220}>
    <BarChart data={data} margin={{ top: 4, right: 16, left: 0, bottom: 0 }}>
      <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
      <XAxis dataKey="date" tick={{ fontSize: 12 }} />
      <YAxis tick={{ fontSize: 12 }} tickFormatter={(v) => `R$${v}`} />
      <Tooltip content={<CustomTooltip />} />
      <Legend wrapperStyle={{ fontSize: 12 }} />
      <Bar dataKey="Faturado"     fill="#c0392b" radius={[4, 4, 0, 0]} />
      <Bar dataKey="Ticket médio" fill="#e67e22" radius={[4, 4, 0, 0]} />
    </BarChart>
  </ResponsiveContainer>
);

const ProductsChart = ({ data }) => (
  <ResponsiveContainer width="100%" height={Math.max(160, data.length * 44 + 60)}>
    <BarChart layout="vertical" data={data} margin={{ top: 4, right: 24, left: 8, bottom: 0 }}>
      <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" horizontal={false} />
      <XAxis type="number" tick={{ fontSize: 12 }} allowDecimals={false} />
      <YAxis type="category" dataKey="name" tick={{ fontSize: 12 }} width={160} />
      <Tooltip content={<CustomTooltip />} />
      <Bar dataKey="Quantidade" fill="#2c3e50" radius={[0, 4, 4, 0]} />
    </BarChart>
  </ResponsiveContainer>
);

const DailyTable = ({ breakdown }) => (
  <div className="sr-table-wrapper">
    <table className="sr-table">
      <thead>
        <tr>
          {['Data', 'Pedidos', 'Faturado', 'Ticket médio'].map((h) => (
            <th key={h} className="sr-table__th">{h}</th>
          ))}
        </tr>
      </thead>
      <tbody>
        {breakdown
          .slice()
          .sort((a, b) => a.date.localeCompare(b.date))
          .map((d) => (
            <tr key={d.date} className="sr-table__row">
              <td className="sr-table__td">{fmtDate(d.date)}</td>
              <td className="sr-table__td">{d.totalSales}</td>
              <td className="sr-table__td">{fmt(d.totalBilled)}</td>
              <td className="sr-table__td">{fmt(d.averageTicket)}</td>
            </tr>
          ))}
      </tbody>
    </table>
  </div>
);

// ── main component ─────────────────────────────────────────────────────────────

const SalesReport = ({ userId }) => {
  const [restaurants, setRestaurants] = useState([]);
  const [restaurantsLoading, setRestaurantsLoading] = useState(true);
  const [selectedId, setSelectedId] = useState('');
  const [startDate, setStartDate] = useState(daysAgo(7));
  const [endDate, setEndDate] = useState(today());
  const [report, setReport] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [exporting, setExporting] = useState(false);
  const reportRef = useRef(null);

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

  const exportPDF = async () => {
    setExporting(true);
    try {
      if (!globalThis.html2canvas)
        await loadScript('https://cdnjs.cloudflare.com/ajax/libs/html2canvas/1.4.1/html2canvas.min.js');
      if (!globalThis.jspdf)
        await loadScript('https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js');

      const canvas = await globalThis.html2canvas(reportRef.current, { scale: 2, useCORS: true, backgroundColor: '#ffffff' });
      const { jsPDF } = globalThis.jspdf;
      const pdf = new jsPDF({ orientation: 'portrait', unit: 'px', format: 'a4' });
      const pdfW = pdf.internal.pageSize.getWidth();
      pdf.addImage(canvas.toDataURL('image/png'), 'PNG', 0, 0, pdfW, (canvas.height * pdfW) / canvas.width);
      const name = restaurants.find((r) => r.id === selectedId)?.name ?? 'restaurante';
      pdf.save(`relatorio-${name}-${startDate}-${endDate}.pdf`);
    } catch (e) {
      console.error('Erro ao exportar PDF:', e);
    }
    setExporting(false);
  };

  const dailyData = (report?.dailyBreakdown ?? [])
    .slice()
    .sort((a, b) => a.date.localeCompare(b.date))
    .map((d) => ({ date: fmtDate(d.date), Faturado: d.totalBilled, 'Ticket médio': d.averageTicket }));

  const productData = (report?.products ?? [])
    .slice()
    .sort((a, b) => b.totalQuantity - a.totalQuantity)
    .map((p) => ({ name: p.productName, Quantidade: p.totalQuantity }));

  const selectedName = restaurants.find((r) => r.id === selectedId)?.name ?? '';

  if (restaurantsLoading) return <LoadingState message="Carregando restaurantes…" />;

  if (restaurants.length === 0 && !error) {
    return <div className="sr-empty"><p>Você ainda não possui restaurantes cadastrados.</p></div>;
  }

  return (
    <div className="sr-wrapper">

      <div className="sr-header">
        <div>
          <h2 className="sr-header__title">Relatório de vendas</h2>
          {selectedName && <p className="sr-header__sub">{selectedName}</p>}
        </div>
        {report && (
          <button className="sr-btn sr-btn--export" onClick={exportPDF} disabled={exporting}>
            <svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 24 24"
              fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
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
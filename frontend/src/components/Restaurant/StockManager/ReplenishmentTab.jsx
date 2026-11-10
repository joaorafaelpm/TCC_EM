import React, { useCallback, useEffect, useState } from 'react';
import api from '../../../services/api';
import { formatQty, getErrorMessage, toInputDate } from './stockUtils';

const DAY_MS = 24 * 60 * 60 * 1000;

const ReplenishmentTab = ({ restaurantId }) => {
  const base = `/v1/restaurants/${restaurantId}/stock`;

  const [start, setStart] = useState(() => toInputDate(new Date(Date.now() - 7 * DAY_MS)));
  const [end, setEnd] = useState(() => toInputDate(new Date()));
  const [lines, setLines] = useState(null);
  const [capacity, setCapacity] = useState([]);
  const [onlyConsumed, setOnlyConsumed] = useState(true);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const generate = useCallback(async (s, e) => {
    if (!s || !e || s > e) {
      setError('Período inválido: a data inicial deve ser anterior (ou igual) à final.');
      return;
    }
    setLoading(true);
    setError('');
    try {
      const startIso = new Date(`${s}T00:00:00`).toISOString();
      const endIso = new Date(`${e}T23:59:59.999`).toISOString();
      const { data } = await api.get(`${base}/report/replenishment`, {
        params: { start: startIso, end: endIso },
      });
      setLines(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(getErrorMessage(err, 'Não foi possível gerar o relatório.'));
    } finally {
      setLoading(false);
    }
  }, [base]);

  const loadCapacity = useCallback(async () => {
    try {
      const { data } = await api.get(`${base}/capacity`);
      setCapacity(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(getErrorMessage(err, 'Não foi possível carregar a capacidade de produção.'));
    }
  }, [base]);

  useEffect(() => {
    generate(start, end);
    loadCapacity();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const applyPreset = (days) => {
    const s = toInputDate(new Date(Date.now() - days * DAY_MS));
    const e = toInputDate(new Date());
    setStart(s);
    setEnd(e);
    generate(s, e);
  };

  const visibleLines = (lines ?? []).filter((l) => !onlyConsumed || l.consumed > 0);

  return (
    <div>
      <p className="stock-hint">
        Mostra o que foi consumido pelos pedidos no período. A coluna <strong>"Repor"</strong> é a quantidade
        que você precisa comprar para voltar ao nível anterior.
      </p>

      {error && <p className="stock-banner stock-banner--error">⚠️ {error}</p>}

      <div className="stock-card">
        <div className="stock-filters">
          <div className="stock-field">
            <label>De</label>
            <input type="date" value={start} onChange={(e) => setStart(e.target.value)} />
          </div>
          <div className="stock-field">
            <label>Até</label>
            <input type="date" value={end} onChange={(e) => setEnd(e.target.value)} />
          </div>
          <button className="stock-btn stock-btn--primary" onClick={() => generate(start, end)} disabled={loading}>
            {loading ? 'Gerando...' : 'Gerar relatório'}
          </button>
        </div>

        <div className="stock-presets">
          <button className="stock-btn stock-btn--small stock-btn--ghost" onClick={() => applyPreset(0)}>Hoje</button>
          <button className="stock-btn stock-btn--small stock-btn--ghost" onClick={() => applyPreset(7)}>7 dias</button>
          <button className="stock-btn stock-btn--small stock-btn--ghost" onClick={() => applyPreset(30)}>30 dias</button>
          <label className="stock-check">
            <input type="checkbox" checked={onlyConsumed} onChange={(e) => setOnlyConsumed(e.target.checked)} />
            Mostrar só ingredientes com consumo
          </label>
        </div>
      </div>

      {lines !== null && (
        visibleLines.length === 0 ? (
          <div className="stock-empty">Nenhum consumo registrado neste período.</div>
        ) : (
          <div className="stock-table-wrap">
            <table className="stock-table">
              <thead>
                <tr>
                  <th>Ingrediente</th>
                  <th>Repor (consumido no período)</th>
                  <th>Saldo atual</th>
                  <th>Mínimo</th>
                  <th>Sugestão de compra</th>
                </tr>
              </thead>
              <tbody>
                {visibleLines.map((l) => (
                  <tr key={l.ingredientId} className={l.stock <= l.lowStockThreshold ? 'is-low' : ''}>
                    <td data-label="Ingrediente"><strong>{l.name}</strong></td>
                    <td data-label="Repor" className="stock-strong">{formatQty(l.consumed, l.unit)}</td>
                    <td data-label="Saldo atual">{formatQty(l.stock, l.unit)}</td>
                    <td data-label="Mínimo">{formatQty(l.lowStockThreshold, l.unit)}</td>
                    <td data-label="Sugestão de compra">{formatQty(l.suggestedPurchase, l.unit)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )
      )}

      <div className="stock-card">
        <h3>Capacidade de produção (com o estoque atual)</h3>
        {capacity.length === 0 ? (
          <p className="stock-muted">Nenhum produto com receita cadastrada.</p>
        ) : (
          <div className="stock-table-wrap">
            <table className="stock-table">
              <thead>
                <tr>
                  <th>Produto</th>
                  <th>Unidades possíveis</th>
                </tr>
              </thead>
              <tbody>
                {capacity.map((c) => (
                  <tr key={c.productId} className={c.portions === 0 ? 'is-low' : ''}>
                    <td data-label="Produto"><strong>{c.productName}</strong></td>
                    <td data-label="Unidades possíveis">
                      {c.portions === 0 ? 'Esgotado' : c.portions.toLocaleString('pt-BR')}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
        <p className="stock-muted">
          Cada produto é calculado isoladamente: ingredientes compartilhados entre produtos não são divididos.
        </p>
      </div>
    </div>
  );
};

export default ReplenishmentTab;

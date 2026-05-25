import React, { useEffect, useState, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../../components/context/AuthProvider.jsx';
import './OrderHandler.css';
import OrderCard from '../../components/OrderCard/OrderCard.jsx';

const STATUS_CONFIG = {
  CREATED:     { label: 'Criado',     color: 'status--created',     order: 0 },
  CONFIRMED: { label: 'Confirmado', color: 'status--confirmed', order: 1 },
  DELIVERED:   { label: 'Entregue',   color: 'status--delivered',   order: 2 },
  CANCELED:  { label: 'Cancelado',  color: 'status--canceled',  order: 3 },
};

const COLUMNS = ['CREATED', 'CONFIRMED', 'DELIVERED', 'CANCELED'];

export default function OrderHandler() {
  const { id: restaurantId } = useParams();
  const { user: tokenData } = useAuth();
  const navigate = useNavigate();

  const [orders, setOrders]     = useState([]);
  const [loading, setLoading]   = useState(true);
  const [error, setError]       = useState(null);
  const [acting, setActing]     = useState(null); // orderId em ação
  const [expanded, setExpanded] = useState(null); // orderId expandido

  const hasAuthority = tokenData?.authorities?.includes('EDITAR_LOGICA_RESTAURANTES');

  const fetchOrders = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const now   = new Date();
      const start = new Date(now.getTime() - 24 * 60 * 60 * 1000).toISOString();
      const end   = new Date(now.getTime() + 24 * 60 * 60 * 1000).toISOString();

      const params = new URLSearchParams({
        restaurantId,
        startCreationDate: start,
        endCreationDate: end,
      });

      const res = await fetch(`/v1/orders/restaurant?${params}`, { credentials: 'include' });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      const data = await res.json();
      setOrders(data.content ?? data ?? []);
    } catch (e) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  }, [restaurantId]);

  useEffect(() => { fetchOrders(); }, [fetchOrders]);

  const doAction = async (orderId, action) => {
    // action: 'confirm' | 'deliver' | 'cancel'
    setActing(orderId);
    try {
      const res = await fetch(`/v1/orders/${orderId}/${action}`, {
        method: 'PUT',
        credentials: 'include',
      });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      await fetchOrders();
    } catch (e) {
      alert(`Erro ao executar ação: ${e.message}`);
    } finally {
      setActing(null);
    }
  };

  const byStatus = COLUMNS.reduce((acc, s) => {
    acc[s] = orders
      .filter(o => o.status === s)
      .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
    return acc;
  }, {});

  if (!hasAuthority) {
    return (
      <div className="oh-gate">
        <div className="oh-gate__box">
          <span className="oh-gate__icon">🔒</span>
          <h2>Acesso restrito</h2>
          <p>Você não tem permissão para acessar esta página.</p>
          <button className="oh-btn oh-btn--ghost" onClick={() => navigate(-1)}>Voltar</button>
        </div>
      </div>
    );
  }

  return (
    <div className="oh-page">
      {/* ── Header ── */}
      <header className="oh-header">
        <button className="oh-back" onClick={() => navigate(`/restaurant/${restaurantId}`)} aria-label="Voltar">
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24"
            fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
            <polyline points="15 18 9 12 15 6"/>
          </svg>
        </button>
        <div className="oh-header__title">
          <h1>Pedidos</h1>
          <span className="oh-header__sub">Últimas 24 horas</span>
        </div>
        <button className="oh-refresh" onClick={fetchOrders} disabled={loading} aria-label="Atualizar">
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24"
            fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round"
            className={loading ? 'oh-spin' : ''}>
            <polyline points="23 4 23 10 17 10"/>
            <path d="M20.49 15a9 9 0 1 1-2.12-9.36L23 10"/>
          </svg>
        </button>
      </header>

      {/* ── Estado de erro ── */}
      {error && (
        <div className="oh-error" role="alert">
          <span>⚠️ Erro ao carregar pedidos: {error}</span>
          <button onClick={fetchOrders}>Tentar novamente</button>
        </div>
      )}

      {/* ── Kanban ── */}
      <div className="oh-board">
        {COLUMNS.map(status => {
          const cfg  = STATUS_CONFIG[status];
          const list = byStatus[status];
          return (
            <div key={status} className={`oh-column oh-column--${status.toLowerCase()}`}>
              <div className="oh-column__header">
                <span className={`oh-dot oh-dot--${status.toLowerCase()}`} />
                <h2>{cfg.label}</h2>
                <span className="oh-count">{list.length}</span>
              </div>

              <div className="oh-column__body">
                {loading && list.length === 0 && (
                  <div className="oh-skeleton">
                    {[1,2].map(i => <div key={i} className="oh-skeleton__card" />)}
                  </div>
                )}

                {!loading && list.length === 0 && (
                  <div className="oh-empty">Nenhum pedido</div>
                )}

                {list.map(order => (
                  <OrderCard
                    key={order.id}
                    order={order}
                    acting={acting === order.id}
                    expanded={expanded === order.id}
                    onToggle={() => setExpanded(prev => prev === order.id ? null : order.id)}
                    onAction={doAction}
                  />
                ))}
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}

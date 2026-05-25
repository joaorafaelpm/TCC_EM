import React, { useState, useEffect, useCallback } from 'react';
import './MyOrders.css';
import { OrderCard } from './OrderCard/OrderCard';

/* ── Componente principal ── */
const MyOrders = ({ userId }) => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [expandedId, setExpandedId] = useState(null);
  const [detailCache, setDetailCache] = useState({});
  const [loadingDetailId, setLoadingDetailId] = useState(null);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);

  const fetchOrders = useCallback(async (pageNum = 0) => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch(`/v1/orders/user?page=${pageNum}&size=10&userId=${userId}`, { credentials: 'include' });
      if (!res.ok) throw new Error('Erro ao carregar pedidos.');
      const data = await res.json();
      setOrders(data.content ?? []);
      setTotalPages(data.totalPages ?? 1);
      setPage(pageNum);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => { fetchOrders(0); }, [fetchOrders]);

  const fetchDetail = useCallback(async (orderId) => {
    if (detailCache[orderId] !== undefined) return;
    setLoadingDetailId(orderId);
    try {
      const res = await fetch(`/v1/orders/${orderId}`, { credentials: 'include' });
      if (!res.ok) throw new Error();
      const data = await res.json();
      console.log('Detalhes do pedido:', data);
      setDetailCache((prev) => ({ ...prev, [orderId]: data }));
    } catch {
      setDetailCache((prev) => ({ ...prev, [orderId]: null }));
    } finally {
      setLoadingDetailId(null);
    }

  }, [detailCache]);

  const handleToggle = (orderId) => {
    if (expandedId === orderId) {
      setExpandedId(null);
    } else {
      setExpandedId(orderId);
      fetchDetail(orderId);
    }
  };

  return (
    <section className="orders">
      <div className="orders__header">
        <h2 className="orders__title">Meus pedidos</h2>
        <span className="orders__count">{orders.length} pedido{orders.length !== 1 ? 's' : ''}</span>
      </div>

      {loading && (
        <div className="orders__loading">
          <div className="orders__spinner" />
          <span>Carregando pedidos…</span>
        </div>
      )}

      {error && !loading && (
        <div className="orders__error">
          <span>⚠️ {error}</span>
          <button className="orders__retry" onClick={() => fetchOrders(page)}>Tentar novamente</button>
        </div>
      )}

      {!loading && !error && orders.length === 0 && (
        <div className="orders__empty">
          <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24"
            fill="none" stroke="#ccc" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
            <path d="M6 2L3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z" />
            <line x1="3" y1="6" x2="21" y2="6" />
            <path d="M16 10a4 4 0 0 1-8 0" />
          </svg>
          <p>Você ainda não tem pedidos.</p>
        </div>
      )}

      {!loading && !error && orders.length > 0 && (
        <>
          <div className="orders__list">
            {orders.map((order) => (
              <OrderCard
                key={order.id}
                order={order}
                isExpanded={expandedId === order.id}
                onToggle={() => handleToggle(order.id)}
                detail={detailCache[order.id]}
                loadingDetail={loadingDetailId === order.id}
              />
            ))}
          </div>

          {totalPages > 1 && (
            <div className="orders__pagination">
              <button
                className="orders__page-btn"
                disabled={page === 0}
                onClick={() => fetchOrders(page - 1)}
              >
                ← Anterior
              </button>
              <span className="orders__page-info">Página {page + 1} de {totalPages}</span>
              <button
                className="orders__page-btn"
                disabled={page >= totalPages - 1}
                onClick={() => fetchOrders(page + 1)}
              >
                Próxima →
              </button>
            </div>
          )}
        </>
      )}
    </section>
  );
};

export default MyOrders;
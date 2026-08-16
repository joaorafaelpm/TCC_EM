import React, { useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import './OrderConfirmation.css';

/**
 * Tela exibida após o envio do pedido.
 * Recebe o resultado da API (/v1/orders/batch) e exibe sucesso/erro
 * de forma clara e bonita.
 *
 * @param {{ created: Order[], errors: OrderError[] }} result
 */
export default function OrderConfirmation({ result }) {
  const navigate = useNavigate();
  const hasSuccess = result.created.length > 0;
  const hasErrors  = result.errors.length > 0;

  // Anima os cards ao montar
  const listRef = useRef(null);
  useEffect(() => {
    const cards = listRef.current?.querySelectorAll('.oc-card');
    cards?.forEach((card, i) => {
      card.style.animationDelay = `${i * 90}ms`;
    });
  }, []);

  return (
    <div className="oc-root">
      {/* ── Cabeçalho ── */}
      <div className={`oc-hero ${hasErrors && !hasSuccess ? 'oc-hero--warn' : ''}`}>
        <div className="oc-hero-icon">
          {hasSuccess ? (
            <svg viewBox="0 0 56 56" fill="none">
              <circle cx="28" cy="28" r="28" className="oc-circle" />
              <polyline points="15,29 24,38 41,20" className="oc-check" />
            </svg>
          ) : (
            <svg viewBox="0 0 56 56" fill="none">
              <circle cx="28" cy="28" r="28" className="oc-circle oc-circle--warn" />
              <line x1="28" y1="17" x2="28" y2="31" className="oc-check oc-check--warn" />
              <circle cx="28" cy="38" r="2" className="oc-dot--warn" />
            </svg>
          )}
        </div>

        <h1 className="oc-title">
          {hasSuccess && !hasErrors && 'Pedido confirmado!'}
          {hasSuccess && hasErrors  && 'Pedido parcialmente confirmado'}
          {!hasSuccess && hasErrors && 'Não foi possível confirmar'}
        </h1>

        <p className="oc-subtitle">
          {hasSuccess && !hasErrors &&
            `${result.created.length} pedido${result.created.length > 1 ? 's' : ''} em preparação`}
          {hasSuccess && hasErrors &&
            `${result.created.length} confirmado${result.created.length > 1 ? 's' : ''}, ${result.errors.length} com problema`}
          {!hasSuccess && hasErrors &&
            'Verifique os detalhes abaixo e tente novamente'}
        </p>
      </div>

      {/* ── Pedidos confirmados ── */}
      {hasSuccess && (
        <section className="oc-section" ref={listRef}>
          <h2 className="oc-section-title">
            <span className="oc-section-dot oc-section-dot--green" />
            Confirmados
          </h2>

          <div className="oc-cards">
            {result.created.map((order) => (
              <div className="oc-card oc-card--success" key={order.id}>
                <div className="oc-card-top">
                  <span className="oc-restaurant">{order.restaurant.name}</span>
                  <span className="oc-status">{formatStatus(order.status)}</span>
                </div>

                <ul className="oc-items">
                  {order.items?.map((item, i) => (
                    <li key={i} className="oc-item">
                      <span className="oc-item-qty">{item.quantity}×</span>
                      <span className="oc-item-name">{item.productName ?? item.product?.name}</span>
                    </li>
                  ))}
                </ul>

                <div className="oc-card-bottom">
                  <span className="oc-id">#{order.id?.slice(0, 8).toUpperCase()}</span>
                  <span className="oc-total">
                    R$ {order.totalValue.toFixed(2).replace('.', ',')}
                  </span>
                </div>
              </div>
            ))}
          </div>
        </section>
      )}

      {/* ── Pedidos com erro ── */}
      {hasErrors && (
        <section className="oc-section">
          <h2 className="oc-section-title">
            <span className="oc-section-dot oc-section-dot--red" />
            Com problema
          </h2>

          <div className="oc-cards">
            {result.errors.map((err) => (
              <div className="oc-card oc-card--error" key={err.index}>
                <div className="oc-card-top">
                  <span className="oc-restaurant">Pedido {err.index + 1}</span>
                </div>
                <p className="oc-error-msg">{err.message}</p>
              </div>
            ))}
          </div>
        </section>
      )}

      {/* ── Ações ── */}
      <div className="oc-actions">
        {hasSuccess && (
          <button className="oc-btn oc-btn--primary" onClick={() => navigate('/my-account')}>
            Ver meus pedidos
          </button>
        )}
        <button className="oc-btn oc-btn--ghost" onClick={() => navigate('/')}>
          Voltar ao início
        </button>
      </div>
    </div>
  );
}

function formatStatus(status) {
  const map = {
    CREATED:    'Criado',
    CONFIRMED:  'Confirmado',
    DELIVERED:  'Entregue',
    CANCELLED:  'Cancelado',
  };
  return map[status] ?? status;
}
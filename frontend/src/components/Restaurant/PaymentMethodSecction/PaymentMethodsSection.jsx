import React, { useEffect, useState } from 'react';
import ConfirmTogglePopup from '../ConfirmTogglePopup/ConfirmTogglePopup.jsx';
import './PaymentMethodsSection.css';

/**
 * Exibe e gerencia formas de pagamento de um restaurante.
 * Visível apenas para usuários com permissão GERENCIAR_RESTAURANTE.
 *
 * @param {string|number} restaurantId
 * @param {boolean}       hasAuthority  — deve ser passado pelo pai para garantir
 *                                        que o fetch só ocorra com o token pronto
 */
const PaymentMethodsSection = ({ restaurantId, hasAuthority }) => {
  const [allMethods, setAllMethods] = useState([]);
  const [linkedIds, setLinkedIds] = useState(new Set());
  const [loading, setLoading] = useState(true);
  const [actionLoading, setActionLoading] = useState(null);
  const [actionError, setActionError] = useState(null);
  const [pendingConfirm, setPendingConfirm] = useState(null);

  useEffect(() => {
    if (hasAuthority === undefined || hasAuthority === null) return;

    let cancelled = false;
    setLoading(true);

    const fetchData = async () => {
      try {
        const [allRes, linkedRes] = await Promise.all([
          fetch(`/v1/payment-methods`, { credentials: 'include', cache: 'no-store' }),
          fetch(`/v1/restaurants/${restaurantId}/payment-methods`, { credentials: 'include', cache: 'no-store' }),
        ]);

        const allData = await allRes.json();
        const linkedData = await linkedRes.json();

        if (cancelled) return;
        setAllMethods(allData?.content ?? allData ?? []);
        const ids = new Set((linkedData?.content ?? linkedData ?? []).map(m => m.id));
        setLinkedIds(ids);
      } catch (err) {
        console.error('Erro ao buscar formas de pagamento:', err);
      } finally {
        if (!cancelled) setLoading(false);
      }
    };

    fetchData();
    return () => { cancelled = true; };
  }, [restaurantId, hasAuthority]);

  const handleAdd = (method) => {
    setActionError(null);
    setPendingConfirm({ methodId: method.id, methodName: method.description, action: 'add' });
  };

  const handleRemove = (method) => {
    setActionError(null);
    setPendingConfirm({ methodId: method.id, methodName: method.description, action: 'remove' });
  };

  const handleConfirm = async () => {
    if (!pendingConfirm) return;
    const { methodId, action } = pendingConfirm;
    setPendingConfirm(null);
    setActionLoading(methodId);
    setActionError(null);

    try {
      const url = `/v1/restaurants/${restaurantId}/payment-methods/${methodId}`;
      const res = await fetch(url, {
        method: action === 'add' ? 'PUT' : 'DELETE',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        }
      });

      if (res.ok) {
        setLinkedIds(prev => {
          const next = new Set(prev);
          action === 'add' ? next.add(methodId) : next.delete(methodId);
          return next;
        });
      } else {
        const text = await res.text().catch(() => '');
        const label = action === 'add' ? 'adicionar' : 'remover';
        setActionError(
          `Erro ao ${label} forma de pagamento (HTTP ${res.status})${text ? ': ' + text : '.'}`
        );
        console.error(`Erro ${res.status} ao ${label} forma de pagamento:`, text);
      }
    } catch (err) {
      setActionError(`Falha na requisição: ${err.message}`);
      console.error('Erro na requisição:', err);
    } finally {
      setActionLoading(null);
    }
  };

  if (loading) {
    return (
      <section className="payment-section">
        <div className="payment-section__header">
          <h2>Formas de pagamento</h2>
        </div>
        <p className="payment-section__loading">Carregando...</p>
      </section>
    );
  }

  const linked = allMethods.filter(m => linkedIds.has(m.id));
  const available = allMethods.filter(m => !linkedIds.has(m.id));
  return (
    <>
      <section className="payment-section">
        <div className="payment-section__header">
          <h2>Formas de pagamento</h2>
          <span className="payment-section__badge">{linkedIds.size} ativas</span>
        </div>

        <div className="payment-methods-grid">
          {linked.map(method => (
            <div key={method.id} className="payment-method-card payment-method-card--linked">
              <div className="payment-method-card__info">
                <span className="payment-method-card__icon" aria-hidden="true">
                  {getPaymentIcon(method.description)}
                </span>
                <span className="payment-method-card__name">{method.description}</span>
                <span className="payment-method-card__status">Ativo</span>
              </div>
              <button
                className="payment-method-card__btn payment-method-card__btn--remove"
                onClick={() => handleRemove(method)}
                disabled={actionLoading === method.id}
                aria-label={`Remover ${method.description}`}
              >
                {actionLoading === method.id ? (
                  <span className="payment-method-card__spinner" />
                ) : (
                  <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24"
                    fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round"
                    aria-hidden="true">
                    <polyline points="3 6 5 6 21 6" />
                    <path d="M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6" />
                    <path d="M10 11v6M14 11v6" />
                    <path d="M9 6V4h6v2" />
                  </svg>
                )}
                Remover
              </button>
            </div>
          ))}

          {available.map(method => (
            <div key={method.id} className="payment-method-card payment-method-card--available">
              <div className="payment-method-card__info">
                <span className="payment-method-card__icon" aria-hidden="true">
                  {getPaymentIcon(method.description ?? method.type)}
                </span>
                <span className="payment-method-card__name">{method.description}</span>
              </div>
              <button
                className="payment-method-card__btn payment-method-card__btn--add"
                onClick={() => handleAdd(method)}
                disabled={actionLoading === method.id}
                aria-label={`Adicionar ${method.description}`}
              >
                {actionLoading === method.id ? (
                  <span className="payment-method-card__spinner" />
                ) : (
                  <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24"
                    fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round"
                    aria-hidden="true">
                    <line x1="12" y1="5" x2="12" y2="19" />
                    <line x1="5" y1="12" x2="19" y2="12" />
                  </svg>
                )}
                Adicionar
              </button>
            </div>
          ))}

          {allMethods.length === 0 && (
            <p className="payment-section__empty">Nenhuma forma de pagamento encontrada.</p>
          )}
        </div>
      </section>

      {pendingConfirm && (
        <ConfirmTogglePopup
          title={pendingConfirm.action === 'add' ? 'Adicionar forma de pagamento?' : 'Remover forma de pagamento?'}
          message={
            <p className="confirm-popup__message">
              {pendingConfirm.action === 'add'
                ? <>Deseja adicionar <strong>"{pendingConfirm.methodName}"</strong> como forma de pagamento aceita neste restaurante?</>
                : <>Deseja remover <strong>"{pendingConfirm.methodName}"</strong> das formas de pagamento deste restaurante? Os clientes não poderão mais usá-la.</>
              }
            </p>
          }
          confirmLabel={pendingConfirm.action === 'add' ? 'Adicionar' : 'Remover'}
          confirmVariant={pendingConfirm.action === 'add' ? 'success' : 'danger'}
          icon={pendingConfirm.action === 'add' ? '💳' : '🗑️'}
          cooldownSeconds={5}
          onConfirm={handleConfirm}
          onCancel={() => setPendingConfirm(null)}
        />
      )}
    </>
  );
};

function getPaymentIcon(type) {
  if (!type) return '💳';
  const t = type.toString().toUpperCase();
  if (t.includes('CREDIT') || t.includes('CARTÃO DE CRÉDITO')) return '💳';
  if (t.includes('DEBIT') || t.includes('CARTÃO DE DÉBITO')) return '💳';
  if (t.includes('PIX')) return '❖';
  if (t.includes('CASH') || t.includes('DINHEIRO')) return '💵';
  if (t.includes('VOUCHER') || t.includes('VALE')) return '🎟️';
  if (t.includes('CRYPTO')) return '₿';
  return '💳';
}

export default PaymentMethodsSection;
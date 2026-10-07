function fmtTime(iso) {
    if (!iso) return '—';
    return new Date(iso).toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
}
  
function fmtDateTime(iso) {
if (!iso) return '—';
return new Date(iso).toLocaleString('pt-BR', {
    day: '2-digit', month: '2-digit',
    hour: '2-digit', minute: '2-digit',
});
}

function Spinner() {
  return <span className="oh-spinner" aria-hidden="true" />;
}

export default function OrderCard({ order, acting, expanded, onToggle, onAction }) {
  const fmt = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' });

  const shortId = order.id.toString().slice(0, 8).toUpperCase();

  return (
    <div className={`oh-card ${expanded ? 'oh-card--expanded' : ''}`}>
      {/* Cabeçalho do card */}
      <div className="oh-card__head" onClick={onToggle} role="button" tabIndex={0}
        onKeyDown={e => e.key === 'Enter' && onToggle()}>
        <div className="oh-card__id">#{shortId}</div>
        <div className="oh-card__meta">
          <span className="oh-card__customer">{order.customer?.name ?? '—'}</span>
          <span className="oh-card__time">{fmtTime(order.createdAt)}</span>
        </div>
        <div className="oh-card__total">{fmt.format(order.totalValue)}</div>
        <svg className={`oh-card__chevron ${expanded ? 'oh-card__chevron--open' : ''}`}
          xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24"
          fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
          <polyline points="6 9 12 15 18 9"/>
        </svg>
      </div>

      {/* Detalhe expansível */}
      {expanded && (
        <div className="oh-card__detail">
          {/* Itens */}
          <div className="oh-card__section">
            <p className="oh-card__section-title">Itens</p>
            {order.items?.map((item, i) => (
              <div key={i} className="oh-item">
                <span className="oh-item__qty">{item.quantity}×</span>
                <span className="oh-item__name">{item.productName}</span>
                <span className="oh-item__price">{fmt.format(item.totalPrice)}</span>
              </div>
            ))}
            {order.items?.some(i => i.note) && (
              <div className="oh-notes">
                {order.items.filter(i => i.note).map((item, i) => (
                  <p key={i} className="oh-note">
                    <strong>{item.productName}:</strong> {item.note}
                  </p>
                ))}
              </div>
            )}
          </div>

          {/* Totais */}
          <div className="oh-card__section oh-card__totals">
            <div className="oh-total-row">
              <span>Subtotal</span>
              <span>{fmt.format(order.subtotal)}</span>
            </div>
            <div className="oh-total-row">
              <span>Frete</span>
              <span>{fmt.format(order.shippingFee)}</span>
            </div>
            <div className="oh-total-row oh-total-row--final">
              <span>Total</span>
              <span>{fmt.format(order.totalValue)}</span>
            </div>
          </div>

          {/* Endereço */}
          {order.deliveryAddress && (
            <div className="oh-card__section">
              <p className="oh-card__section-title">Entrega</p>
              <p className="oh-address">
                {order.deliveryAddress.street}, {order.deliveryAddress.number}
                {order.deliveryAddress.complement ? ` — ${order.deliveryAddress.complement}` : ''}
                <br />
                {order.deliveryAddress.neighborhood} · {order.deliveryAddress.city?.name}
              </p>
            </div>
          )}

          {/* Pagamento */}
          <div className="oh-card__section">
            <p className="oh-card__section-title">Pagamento</p>
            <p className="oh-payment">{order.paymentMethod?.description ?? '—'}</p>
          </div>

          {/* Timestamps */}
          <div className="oh-card__section oh-timestamps">
            <span>Criado: {fmtDateTime(order.createdAt)}</span>
            {order.confirmedAt && <span>Confirmado: {fmtDateTime(order.confirmedAt)}</span>}
            {order.deliveredAt && <span>Entregue: {fmtDateTime(order.deliveredAt)}</span>}
            {order.canceledAt  && <span>Cancelado: {fmtDateTime(order.canceledAt)}</span>}
          </div>

          {/* Ações */}
          <div className="oh-card__actions">
            {order.status === 'CREATED' && (
              <>
                <button
                  className="oh-btn oh-btn--confirm"
                  onClick={() => onAction(order.id, 'confirm')}
                  disabled={acting}
                >
                  {acting ? <Spinner /> : '✓ Confirmar'}
                </button>
                <button
                  className="oh-btn oh-btn--cancel"
                  onClick={() => onAction(order.id, 'cancel')}
                  disabled={acting}
                >
                  {acting ? <Spinner /> : '✕ Cancelar'}
                </button>
              </>
            )}
            {order.status === 'CONFIRMED' && (
                <button
                  className="oh-btn oh-btn--deliver"
                  onClick={() => onAction(order.id, 'deliver')}
                  disabled={acting}
                >
                  {acting ? <Spinner /> : '🛵 Entregar'}
                </button>
            )}
          </div>
        </div>
      )}
    </div>
  );
}


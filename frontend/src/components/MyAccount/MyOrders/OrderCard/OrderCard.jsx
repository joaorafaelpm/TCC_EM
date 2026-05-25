import { OrderDetail } from "../OrderDetail/OrderDetail";
const formatCurrency = (value) =>
  new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value ?? 0);

const formatDate = (iso) => {
  if (!iso) return '—';
  return new Intl.DateTimeFormat('pt-BR', {
    day: '2-digit', month: '2-digit', year: 'numeric',
    hour: '2-digit', minute: '2-digit',
  }).format(new Date(iso));
};

const STATUS_CONFIG = {
  CREATED:   { label: 'Criado',     color: '#6c757d', bg: '#f0f0f0' },
  CONFIRMED: { label: 'Confirmado', color: '#1a6fc4', bg: '#e8f1fb' },
  DELIVERED: { label: 'Entregue',   color: '#1a7a3c', bg: '#e6f4ec' },
  CANCELED:  { label: 'Cancelado',  color: '#a8222e', bg: '#fdf0f0' },
  // fallback para valores em PT vindos do backend
  CRIADO:    { label: 'Criado',     color: '#6c757d', bg: '#f0f0f0' },
  CONFIRMADO:{ label: 'Confirmado', color: '#1a6fc4', bg: '#e8f1fb' },
  ENTREGUE:  { label: 'Entregue',   color: '#1a7a3c', bg: '#e6f4ec' },
  CANCELADO: { label: 'Cancelado',  color: '#a8222e', bg: '#fdf0f0' },
};


const StatusBadge = ({ status }) => {
  const cfg = STATUS_CONFIG[status] ?? { label: status, color: '#888', bg: '#f5f5f5' };
  return (
    <span className="orders__badge" style={{ color: cfg.color, backgroundColor: cfg.bg }}>
      {cfg.label}
    </span>
  );
};
export const OrderCard = ({ order, isExpanded, onToggle, detail, loadingDetail }) => (
  <div className={`orders__card ${isExpanded ? 'orders__card--expanded' : ''}`}>
    <button className="orders__card-header" onClick={onToggle} aria-expanded={isExpanded}>
      <div className="orders__card-header-left">
        <span className="orders__order-id">#{order.id.split('-')[0].toUpperCase()}</span>
        <span className="orders__restaurant">{order.restaurant?.name ?? '—'}</span>
      </div>
      <div className="orders__card-header-right">
        <StatusBadge status={order.status} />
        <span className="orders__total">{formatCurrency(order.totalValue)}</span>
        <span className="orders__date">{formatDate(order.createdAt)}</span>
        <svg
          className={`orders__chevron ${isExpanded ? 'orders__chevron--open' : ''}`}
          xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24"
          fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round"
        >
          <polyline points="6 9 12 15 18 9" />
        </svg>
      </div>
    </button>

    {isExpanded && (
      <div className="orders__card-detail">
        {loadingDetail ? (
          <div className="orders__detail-loading">
            <div className="orders__spinner" />
            <span>Carregando detalhes…</span>
          </div>
        ) : detail ? (
          <OrderDetail detail={detail} />
        ) : (
          <p className="orders__detail-error">Não foi possível carregar os detalhes.</p>
        )}
      </div>
    )}
  </div>
);
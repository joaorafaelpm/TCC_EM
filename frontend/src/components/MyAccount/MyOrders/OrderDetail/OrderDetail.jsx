const formatCurrency = (value) =>
  new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value ?? 0);
const formatDate = (iso) => {
  if (!iso) return '—';
  return new Intl.DateTimeFormat('pt-BR', {
    day: '2-digit', month: '2-digit', year: 'numeric',
    hour: '2-digit', minute: '2-digit',
  }).format(new Date(iso));
};
const TimelineStep = ({ label, date, done, canceled }) => (
  <div className={`orders__timeline-step ${done ? 'orders__timeline-step--done' : ''} ${canceled ? 'orders__timeline-step--canceled' : ''}`}>
    <div className="orders__timeline-dot" />
    <div className="orders__timeline-info">
      <span className="orders__timeline-label">{label}</span>
      <span className="orders__timeline-date">{formatDate(date)}</span>
    </div>
  </div>
);

export const OrderDetail  = ({ detail }) => (
  <div className="orders__detail">
    {/* Datas */}
    <div className="orders__detail-section">
      <h4 className="orders__detail-title">Linha do tempo</h4>
      <div className="orders__timeline">
        <TimelineStep label="Criado"     date={detail.createdAt}   done />
        <TimelineStep label="Confirmado" date={detail.confirmedAt} done={!!detail.confirmedAt} />
        <TimelineStep label="Entregue"   date={detail.deliveredAt} done={!!detail.deliveredAt} />
        {detail.canceledAt && (
        <TimelineStep label="Cancelado" date={detail.canceledAt} done canceled />
        )}
      </div>
    </div>
    
    <div className="orders__detail-grid">
      {detail.deliveryAddress && (
        <div className="orders__detail-section">
          <h4 className="orders__detail-title">Endereço de entrega</h4>
          <address className="orders__address">
            {detail.deliveryAddress.street}, {detail.deliveryAddress.number}
            {detail.deliveryAddress.complement ? ` — ${detail.deliveryAddress.complement}` : ' - Sem complemento'}
            <br />
            {detail.deliveryAddress.neighborhood} · {detail.deliveryAddress.city.name}/{detail.deliveryAddress.city.state}
            <br />
            CEP {detail.deliveryAddress.zipCode}
          </address>
        </div>
      )}

      {/* Pagamento */}
      {detail.paymentMethod && (
        <div className="orders__detail-section">
          <h4 className="orders__detail-title">Pagamento</h4>
          <p className="orders__payment">{detail.paymentMethod.description}</p>
        </div>
      )}
    </div>

    {/* Itens */}
    {detail.items?.length > 0 && (
      <div className="orders__detail-section">
        <h4 className="orders__detail-title">Itens do pedido</h4>
        <table className="orders__items-table">
          <thead>
            <tr>
              <th>Produto</th>
              <th className="orders__col-right">Qtd.</th>
              <th className="orders__col-right">Unit.</th>
              <th className="orders__col-right">Total</th>
            </tr>
          </thead>
          <tbody>
            {detail.items.map((item, i) => (
              <tr key={i}>
                <td>
                  {item.productName}
                  {item.observation && <span className="orders__item-obs"> — {item.observation}</span>}
                </td>
                <td className="orders__col-right">{item.quantity}</td>
                <td className="orders__col-right">{formatCurrency(item.unitPrice)}</td>
                <td className="orders__col-right">{formatCurrency(item.totalPrice)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    )}

    {/* Totais */}
    <div className="orders__totals">
      <div className="orders__totals-row">
        <span>Subtotal</span>
        <span>{formatCurrency(detail.subtotal)}</span>
      </div>
      <div className="orders__totals-row">
        <span>Taxa de entrega</span>
        <span>{formatCurrency(detail.shippingFee)}</span>
      </div>
      <div className="orders__totals-row orders__totals-row--total">
        <span>Total</span>
        <span>{formatCurrency(detail.totalValue)}</span>
      </div>
    </div>
  </div>
);

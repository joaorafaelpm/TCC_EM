const AlertIcon = () => (
  <svg
    width="16" height="16" viewBox="0 0 24 24"
    fill="none" stroke="currentColor" strokeWidth="2.25"
    strokeLinecap="round" strokeLinejoin="round"
    aria-hidden="true"
  >
    <circle cx="12" cy="12" r="10" />
    <line x1="12" y1="8" x2="12" y2="12" />
    <line x1="12" y1="16" x2="12.01" y2="16" />
  </svg>
);

const RestaurantPaymentGroup = ({ restaurantId, group, onPaymentChange, onRemoveItems, error }) => {
  const hasPaymentMethods = group.paymentMethods.length > 0;

  return (
    <div className="restaurant-order-group">
      <p className="restaurant-order-group__name">
        {group.restaurantName || `Restaurante #${restaurantId}`}
      </p>

      {hasPaymentMethods ? (
        <div className="field-wrapper">
          <label className="field-label" htmlFor={`payment-${restaurantId}`}>
            Forma de pagamento
          </label>
          <select
            id={`payment-${restaurantId}`}
            value={group.selectedPaymentMethod}
            onChange={e => onPaymentChange(restaurantId, e.target.value)}
            className={'field-input' + (error ? ' is-invalid' : '')}
          >
            <option value="" disabled>Selecione uma forma de pagamento</option>
            {group.paymentMethods.map(m => (
              <option key={m.id} value={m.id}>{m.description}</option>
            ))}
          </select>
          {error && <span className="global-field-error">{error}</span>}
        </div>
      ) : (
        <div className="payment-warning">
          <div className="payment-warning__message">
            <AlertIcon />
            <span>
              Este restaurante ainda não cadastrou uma forma de pagamento e não
              pode receber pedidos no momento.
            </span>
          </div>
          <button
            type="button"
            className="payment-warning__remove-btn"
            onClick={() => onRemoveItems(restaurantId)}
          >
            Remover itens deste restaurante
          </button>
        </div>
      )}
    </div>
  );
};

export default RestaurantPaymentGroup;
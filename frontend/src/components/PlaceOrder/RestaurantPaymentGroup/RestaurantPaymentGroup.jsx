const RestaurantPaymentGroup = ({ restaurantId, group, onPaymentChange }) => (
  <div className="restaurant-order-group">
    <p className='title' style={{ marginTop: '30px' }}>
      {group.restaurantName || `Restaurante #${restaurantId}`} — Pagamento
    </p>
    <div className='multi-fields'>
      <select
        value={group.selectedPaymentMethod}
        onChange={e => onPaymentChange(restaurantId, e.target.value)}
        required
        className="payment-select"
      >
        <option value="" disabled>Selecione uma forma de pagamento</option>
        {group.paymentMethods.map(m => (
          <option key={m.id} value={m.id}>{m.description}</option>
        ))}
      </select>
    </div>
  </div>
);

export default RestaurantPaymentGroup;
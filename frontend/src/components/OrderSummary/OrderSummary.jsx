const OrderSummary = ({ restaurantGroups, grandTotal }) => (
    <div className="cart-bottom-order">
    <div className='cart-total'>
      <h2>Resumo do Pedido</h2>
      {Object.entries(restaurantGroups).map(([rId, group]) => (
        <div key={rId} className="order-group-summary">
          <p><strong>{group.restaurantName || `Restaurante #${rId}`}</strong></p>
          <p>Subtotal: R$ {group.subtotal.toFixed(2)}</p>
          <p className="shipping-fee">🚚 Frete: R$ {group.shippingFee.toFixed(2)}</p>
        </div>
      ))}
      <div className="cart-bottom-total">
        <h3>Total: R$ {grandTotal.toFixed(2)}</h3>
      </div>
    </div>
  </div>
);

export default OrderSummary;
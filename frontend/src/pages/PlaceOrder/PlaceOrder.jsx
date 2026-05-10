  import React, { useState, useEffect, useContext, useCallback } from 'react';
  import './PlaceOrder.css';
  import { StoreContext } from '../../components/context/StoreContext';
  import DeliveryAddressForm from '../../components/DeliveryAddressForm/DeliveryAddressForm';
  import RestaurantPaymentGroup from '../../components/RestaurantPaymentGroup/RestaurantPaymentGroup';
  import OrderSummary from '../../components/OrderSummary/OrderSummary';


  const PlaceOrder = () => {
    const { cartItems, food_list, clearCart } = useContext(StoreContext);

    const [address, setAddress] = useState([]);
    const [restaurantGroups, setRestaurantGroups] = useState({});
    const [orderResult, setOrderResult] = useState(null);

    const handleAddressUpdate = useCallback((newAddress) => {
      setAddress(newAddress);
    }, []);

    useEffect(() => {
      const itemsInCart = food_list.filter(item => cartItems[item.id] > 0);
      if (itemsInCart.length === 0) return;

      const grouped = itemsInCart.reduce((acc, item) => {
        const rId = item.restaurantId;
        if (!acc[rId]) acc[rId] = { products: [], subtotal: 0, shippingFee: 0, paymentMethods: [], selectedPaymentMethod: '' };
        acc[rId].products.push({ ...item, quantity: cartItems[item.id] });
        acc[rId].subtotal += item.price * cartItems[item.id];
        return acc;
      }, {});

    const fetchRestaurantData = async () => {
    const enriched = { ...grouped };
    await Promise.all(
      Object.keys(grouped).map(async (rId) => {
        const [restaurantRes, paymentRes] = await Promise.all([
          fetch(`/v1/restaurants/${rId}`),
          fetch(`/v1/restaurants/${rId}/payment-methods`)
        ]);

        const restaurant = await restaurantRes.json();
        const paymentMethods = await paymentRes.json().then(data => data['content'] || []);

        enriched[rId].shippingFee = restaurant.shippingFee || 0;
        enriched[rId].restaurantName = restaurant.name;
        enriched[rId].paymentMethods = paymentMethods || [];
      })
    );
    setRestaurantGroups(enriched);
  };

      fetchRestaurantData();
    }, [cartItems, food_list]);

    const handlePaymentChange = (restaurantId, value) => {
      setRestaurantGroups(prev => ({
        ...prev,
        [restaurantId]: { ...prev[restaurantId], selectedPaymentMethod: value }
      }));
    };

    const handleSubmit = async (e) => {
      e.preventDefault();
      const orders = Object.entries(restaurantGroups).map(([restaurantId, group]) => ({
        restaurantId: { id: restaurantId },
        paymentMethodId: { id: group.selectedPaymentMethod },
        deliveryAddress: {
          zipCode: address.zipCode,
          street: address.street,
          number: address.number,
          complement: address.complement,
          neighborhood: address.neighborhood,
          city: { id: address.cityId }
        },
        items: group.products.map(p => ({
          productId: p.id,
          quantity: p.quantity,
          note: p.note || null
        }))
      }));

      try {
        const response = await fetch('/v1/orders/batch', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ orders })
        });
          const resp = await response.json();
          setOrderResult(resp);
          clearCart();
      } catch (err) {
        console.error('Erro ao finalizar pedidos:', err);
      }
    };

    const grandTotal = Object.values(restaurantGroups)
      .reduce((sum, g) => sum + g.subtotal + g.shippingFee, 0);
    if (orderResult) {
      return (
        <div className="order-result">
          {orderResult.created.length > 0 && (
            <div className="order-result-success">
              <h2>✅ {orderResult.created.length} pedido(s) realizado(s) com sucesso!</h2>
              {orderResult.created.map(order => (
                <p key={order.id}>{order.restaurant.name} — R$ {order.totalValue.toFixed(2)} — {order.status}</p>
              ))}
            </div>
          )}
          {orderResult.errors.length > 0 && (
            <div className="order-result-errors">
              <h2>⚠️ {orderResult.errors.length} pedido(s) com problema:</h2>
              {orderResult.errors.map(err => (
                <p key={err.index}>Pedido {err.index + 1}: {err.message}</p>
              ))}
            </div>
          )}
        </div>
      );
    }

    return (
      <form className='place-order' onSubmit={handleSubmit}>
        <div className="place-order-left">
          <DeliveryAddressForm onAddressUpdate={handleAddressUpdate} />
          {Object.entries(restaurantGroups).map(([restaurantId, group]) => (
            <RestaurantPaymentGroup
              key={restaurantId}
              restaurantId={restaurantId}
              group={group}
              onPaymentChange={handlePaymentChange}
            />
          ))}
        </div>
        <div className="place-order-right">
          <OrderSummary restaurantGroups={restaurantGroups} grandTotal={grandTotal} />
          <button type="submit" className='btn-cart-checkout'>Finalizar Pedido</button>
        </div>
      </form>
    );
  };

  export default PlaceOrder;
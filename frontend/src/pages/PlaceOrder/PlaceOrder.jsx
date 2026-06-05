import React, { useState, useEffect, useContext, useCallback, useRef } from 'react';
import './PlaceOrder.css';
import { StoreContext } from '../../components/context/StoreContext';
import DeliveryAddressForm from '../../components/DeliveryAddressForm/DeliveryAddressForm';
import RestaurantPaymentGroup from '../../components/RestaurantPaymentGroup/RestaurantPaymentGroup';
import OrderSummary from '../../components/OrderSummary/OrderSummary';
import api from '../../services/api';

const PlaceOrder = () => {
  const { cartItems, food_list, clearCart } = useContext(StoreContext);

  const [address, setAddress]               = useState({});
  const [restaurantGroups, setRestaurantGroups] = useState({});
  const [orderResult, setOrderResult]       = useState(null);
  const [isLoading, setIsLoading]           = useState(false);

  // ── Validação ────────────────────────────────────────────────
  const addressRef                          = useRef(null);
  const [paymentErrors, setPaymentErrors]   = useState({});  // { [restaurantId]: string }
  const [cartError, setCartError]           = useState(null);
  const [generalError, setGeneralError]     = useState(null);

  const handleAddressUpdate = useCallback((newAddress) => {
    setAddress(newAddress);
  }, []);

  useEffect(() => {
    const itemsInCart = food_list.filter(item => cartItems[item.id] > 0);
    if (itemsInCart.length === 0) return;

    const grouped = itemsInCart.reduce((acc, item) => {
      const rId = item.restaurantId;
      if (!acc[rId]) acc[rId] = {
        products: [], subtotal: 0, shippingFee: 0,
        paymentMethods: [], selectedPaymentMethod: ''
      };
      acc[rId].products.push({ ...item, quantity: cartItems[item.id] });
      acc[rId].subtotal += item.price * cartItems[item.id];
      return acc;
    }, {});

    const fetchRestaurantData = async () => {
      const enriched = { ...grouped };
      await Promise.all(
        Object.keys(grouped).map(async (rId) => {
          const [restaurantRes, paymentRes] = await Promise.all([
            api.get(`/v1/restaurants/${rId}`),
            api.get(`/v1/restaurants/${rId}/payment-methods`)
          ]);
          enriched[rId].shippingFee     = restaurantRes.data.shippingFee || 0;
          enriched[rId].restaurantName  = restaurantRes.data.name;
          enriched[rId].paymentMethods  = paymentRes.data['content'] || [];
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
    // Limpa o erro do restaurante assim que o usuário seleciona
    if (value) {
      setPaymentErrors(prev => ({ ...prev, [restaurantId]: null }));
    }
  };

  // ── Funções de validação ──────────────────────────────────────

  // Espelho de @Size(min=1) @NotNull na lista de items
  const validateCart = () => {
    const empty = Object.keys(restaurantGroups).length === 0;
    setCartError(empty ? 'Adicione pelo menos um item ao carrinho para continuar.' : null);
    return !empty;
  };

  // Espelho de @Valid @NotNull em paymentMethodId — um por restaurante
  const validatePaymentMethods = () => {
    const errors = {};
    Object.entries(restaurantGroups).forEach(([id, group]) => {
      if (!group.selectedPaymentMethod) {
        errors[id] = 'Selecione uma forma de pagamento para continuar.';
      }
    });
    setPaymentErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setGeneralError(null);

    // Dispara todas as validações ao mesmo tempo (sem short-circuit)
    // para o usuário ver todos os erros de uma vez
    const cartValid    = validateCart();
    const addressValid = addressRef.current?.validate();   // DeliveryAddressForm
    const paymentValid = validatePaymentMethods();

    if (!cartValid || !addressValid || !paymentValid) return;

    const orders = Object.entries(restaurantGroups).map(([restaurantId, group]) => ({
      restaurantId:    { id: restaurantId },
      paymentMethodId: { id: group.selectedPaymentMethod },
      deliveryAddress: {
        zipCode:      address.zipCode,
        street:       address.street,
        number:       address.number,
        complement:   address.complement || null,
        neighborhood: address.neighborhood,
        city:         { id: address.cityId }
      },
      items: group.products.map(p => ({
        productId: p.id,
        quantity:  p.quantity,
        note:      p.note || null
      }))
    }));

    setIsLoading(true);
    try {
      const { data } = await api.post('/v1/orders/batch', { orders });
      setOrderResult(data);
      clearCart();
    } catch (err) {
      const msg = err.response?.data?.message || 'Erro ao finalizar pedido. Tente novamente.';
      setGeneralError(msg);
    } finally {
      setIsLoading(false);
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
              <p key={order.id}>
                {order.restaurant.name} — R$ {order.totalValue.toFixed(2)} — {order.status}
              </p>
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

        {/* Erro geral do backend */}
        {generalError && (
          <div className="error-banner" role="alert">{generalError}</div>
        )}

        {/* @Size(min=1) — carrinho vazio */}
        {cartError && (
          <span className="global-field-error">{cartError}</span>
        )}

        {/* @Valid @NotNull deliveryAddress — validação interna exposta via ref */}
        <DeliveryAddressForm ref={addressRef} onAddressUpdate={handleAddressUpdate} />

        {/* @Valid @NotNull paymentMethodId — um por restaurante */}
        {Object.entries(restaurantGroups).map(([restaurantId, group]) => (
          <div key={restaurantId}>
            <RestaurantPaymentGroup
              restaurantId={restaurantId}
              group={group}
              onPaymentChange={handlePaymentChange}
            />
            {paymentErrors[restaurantId] && (
              <span className="global-field-error">
                {paymentErrors[restaurantId]}
              </span>
            )}
          </div>
        ))}

      </div>

      <div className="place-order-right">
        <OrderSummary restaurantGroups={restaurantGroups} grandTotal={grandTotal} />
        <button type="submit" className='btn-cart-checkout' disabled={isLoading}>
          {isLoading ? 'Finalizando...' : 'Finalizar Pedido'}
        </button>
      </div>
    </form>
  );
};

export default PlaceOrder;
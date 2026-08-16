import React, { useState, useCallback, useRef } from 'react';
import './PlaceOrder.css';
import { useContext } from 'react';
import { StoreContext } from '../../components/context/StoreContext';
import DeliveryAddressForm from '../../components/DeliveryAddressForm/DeliveryAddressForm';
import RestaurantPaymentGroup from '../../components/PlaceOrder/RestaurantPaymentGroup/RestaurantPaymentGroup';
import OrderSummary from '../../components/PlaceOrder/OrderSummary/OrderSummary';
import OrderConfirmation from '../../components/PlaceOrder/OrderConfirmation/OrderConfirmation';
import { useCartGroups } from '../../hooks/useCartGroups';
import api from '../../services/api';

const PlaceOrder = () => {
  const { cartItems, food_list, clearCart } = useContext(StoreContext);

  const [address, setAddress]           = useState({});
  const [orderResult, setOrderResult]   = useState(null);
  const [isLoading, setIsLoading]       = useState(false);

  // ── Validação ────────────────────────────────────────────
  const addressRef                      = useRef(null);
  const [paymentErrors, setPaymentErrors] = useState({});
  const [cartError, setCartError]       = useState(null);
  const [generalError, setGeneralError] = useState(null);

  // ── Dados do carrinho (lógica extraída para o hook) ──────
  const { restaurantGroups, isLoadingGroups, setPaymentMethod, grandTotal } =
    useCartGroups(cartItems, food_list);

  const handleAddressUpdate = useCallback((newAddress) => {
    setAddress(newAddress);
  }, []);

  const handlePaymentChange = (restaurantId, value) => {
    setPaymentMethod(restaurantId, value);
    if (value) setPaymentErrors(prev => ({ ...prev, [restaurantId]: null }));
  };

  // ── Validação ────────────────────────────────────────────

  // @Size(min=1) na lista de items
  const validateCart = () => {
    const empty = Object.keys(restaurantGroups).length === 0;
    setCartError(empty ? 'Adicione pelo menos um item ao carrinho para continuar.' : null);
    return !empty;
  };

  // @Valid @NotNull em paymentMethodId — um por restaurante
  const validatePaymentMethods = () => {
    const errors = {};
    Object.entries(restaurantGroups).forEach(([id, group]) => {
      if (!group.selectedPaymentMethod)
        errors[id] = 'Selecione uma forma de pagamento para continuar.';
    });
    setPaymentErrors(errors);
    return Object.keys(errors).length === 0;
  };

  // ── Submit ───────────────────────────────────────────────

  const handleSubmit = async (e) => {
    e.preventDefault();
    setGeneralError(null);

    // Todas as validações disparam juntas — sem short-circuit
    const cartValid    = validateCart();
    const addressValid = addressRef.current?.validate();
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
        city:         { id: address.cityId },
      },
      items: group.products.map(p => ({
        productId: p.id,
        quantity:  p.quantity,
        note:      p.note || null,
      })),
    }));

    setIsLoading(true);
    try {
      const { data } = await api.post('/v1/orders/batch', { orders });
      clearCart();
      setOrderResult(data);
    } catch (err) {
      const msg = err.response?.data?.message || 'Erro ao finalizar pedido. Tente novamente.';
      setGeneralError(msg);
    } finally {
      setIsLoading(false);
    }
  };

  // ── Tela de confirmação (substitui o form após pedido feito) ─
  if (orderResult) {
    return <OrderConfirmation result={orderResult} />;
  }

  // ── Formulário ───────────────────────────────────────────
  return (
    <form className='place-order' onSubmit={handleSubmit}>
      <div className="place-order-left">

        {cartError && (
          <span className="global-field-error">{cartError}</span>
        )}

        {/* @Valid @NotNull deliveryAddress */}
        <DeliveryAddressForm ref={addressRef} onAddressUpdate={handleAddressUpdate} />

        {/* @Valid @NotNull paymentMethodId — um por restaurante */}
        {isLoadingGroups
          ? <p className="loading-text">Carregando restaurantes...</p>
          : Object.entries(restaurantGroups).map(([restaurantId, group]) => (
              <div key={restaurantId}>
                <RestaurantPaymentGroup
                  restaurantId={restaurantId}
                  group={group}
                  onPaymentChange={handlePaymentChange}
                />
                {paymentErrors[restaurantId] && (
                  <span className="global-field-error">{paymentErrors[restaurantId]}</span>
                )}
              </div>
            ))
        }

      </div>

      <div className="place-order-right">
        <OrderSummary restaurantGroups={restaurantGroups} grandTotal={grandTotal} />
        <button type="submit" className="btn-cart-checkout" disabled={isLoading}>
          {isLoading ? 'Finalizando...' : 'Finalizar Pedido'}
        </button>
      </div>
    </form>
  );
};

export default PlaceOrder;
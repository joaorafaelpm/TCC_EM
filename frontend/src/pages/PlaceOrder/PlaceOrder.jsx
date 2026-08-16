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
  const { cartItems, food_list, clearCart, removeProducts } = useContext(StoreContext);

  const [address, setAddress]           = useState({});
  const [orderResult, setOrderResult]   = useState(null);
  const [isLoading, setIsLoading]       = useState(false);

  const addressRef                      = useRef(null);
  const [paymentErrors, setPaymentErrors] = useState({});
  const [cartError, setCartError]       = useState(null);
  const [generalError, setGeneralError] = useState(null);

  const { restaurantGroups, isLoadingGroups, setPaymentMethod, grandTotal } =
    useCartGroups(cartItems, food_list);

  const hasUnavailablePayment = Object.values(restaurantGroups).some(
    (group) => group.paymentMethods.length === 0
  );

  const handleAddressUpdate = useCallback((newAddress) => {
    setAddress(newAddress);
  }, []);

  const handlePaymentChange = (restaurantId, value) => {
    setPaymentMethod(restaurantId, value);
    if (value) setPaymentErrors(prev => ({ ...prev, [restaurantId]: null }));
  };

  const handleRemoveRestaurantItems = (restaurantId) => {
    const group = restaurantGroups[restaurantId];
    if (!group) return;
    removeProducts(group.products.map(p => p.id));
  };
  
  const validateCart = () => {
    const empty = Object.keys(restaurantGroups).length === 0;
    setCartError(empty ? 'Adicione pelo menos um item ao carrinho para continuar.' : null);
    return !empty;
  };

  const validatePaymentMethods = () => {
    const errors = {};
    let allValid = true;

    Object.entries(restaurantGroups).forEach(([id, group]) => {
      if (group.paymentMethods.length === 0) {
        allValid = false;
        return;
      }
      if (!group.selectedPaymentMethod) {
        errors[id] = 'Selecione uma forma de pagamento para continuar.';
        allValid = false;
      }
    });

    setPaymentErrors(errors);
    return allValid;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setGeneralError(null);

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

  if (orderResult) {
    return <OrderConfirmation result={orderResult} />;
  }

  return (
    <form className='place-order' onSubmit={handleSubmit} noValidate>

      <div className="po-top">
        {/* ── Canto superior esquerdo: Endereço ─────────────── */}
        <div className="po-card po-address">
          {cartError && (
            <span className="global-field-error">{cartError}</span>
          )}
          <DeliveryAddressForm ref={addressRef} onAddressUpdate={handleAddressUpdate} />
        </div>

        {/* ── Canto superior direito: Pagamento ─────────────── */}
        <div className="po-card po-payments">
          <p className="title">Pagamento</p>

          {isLoadingGroups && (
            <p className="loading-text">Carregando restaurantes...</p>
          )}
          {!isLoadingGroups && Object.keys(restaurantGroups).length === 0 && (
            <p className="loading-text">Seu carrinho está vazio.</p>
          )}
          {!isLoadingGroups && Object.keys(restaurantGroups).length > 0 && (
            <div className="po-payments__list">
              {Object.entries(restaurantGroups).map(([restaurantId, group]) => (
                <RestaurantPaymentGroup
                  key={restaurantId}
                  restaurantId={restaurantId}
                  group={group}
                  onPaymentChange={handlePaymentChange}
                  onRemoveItems={handleRemoveRestaurantItems}
                  error={paymentErrors[restaurantId]}
                />
              ))}
            </div>
          )}
        </div>
      </div>

      {/* ── Embaixo, ocupando a largura toda: Resumo ────────── */}
      <div className="po-card po-card--summary">
        <OrderSummary restaurantGroups={restaurantGroups} grandTotal={grandTotal} />

        {hasUnavailablePayment && (
          <p className="po-block-note">
            Remova os itens do restaurante sem forma de pagamento cadastrada
            para poder finalizar o pedido.
          </p>
        )}

        {generalError && (
          <span className="global-field-error">{generalError}</span>
        )}

        <button
          type="submit"
          className="btn-cart-checkout"
          disabled={isLoading || hasUnavailablePayment}
        >
          {isLoading ? 'Finalizando...' : 'Finalizar Pedido'}
        </button>
      </div>
    </form>
  );
};

export default PlaceOrder;
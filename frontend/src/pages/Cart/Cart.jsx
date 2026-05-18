import React, { useContext } from 'react';
import './Cart.css';
import { StoreContext } from '../../components/context/StoreContext';
import { useNavigate } from 'react-router-dom';
import CartItem from '../../components/CartItem/CartItem';

const Cart = () => {
  const { cartItems, food_list, removeFromCart } = useContext(StoreContext);
  const navigate = useNavigate();

  const totalCart = food_list.reduce((total, item) => {
    return total + item.price * (cartItems[item.id] || 0);
  }, 0);

  return (
    <div className='cart'>
      <div className="cart-itens">
        <div className="cart-itens-title">
          <p>Itens</p>
          <p>Título</p>
          <p>Preço</p>
          <p>Quantidade</p>
          <p>Total</p>
          <p>Remover</p>
        </div>
        <hr />

        {food_list.map((item) => {
          const quantity = cartItems[item.id];
          
          if (quantity > 0) {
            return (
              <CartItem
                key={item.id}
                item={item} 
                quantity={quantity} 
                onRemove={removeFromCart} 
              />
            );
          }
          return null;
        })}
      </div>

      <div className="cart-bottom">
        <div className='cart-total'>
          <h2>Total do Carrinho</h2>
          <div className="cart-bottom-total">
            <h3>R$ {totalCart.toFixed(2)}</h3>
          </div>
        </div>

        <div>
          <button 
            onClick={() => navigate('/order')} 
            className='btn-cart-checkout'
          >
            Finalizar Pedido
          </button>
        </div>
      </div>
    </div>
  );
};

export default Cart;
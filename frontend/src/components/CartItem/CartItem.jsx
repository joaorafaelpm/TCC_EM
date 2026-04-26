import React from 'react';
import semImagemPng from '../../assets/sem-foto.png';
import './CartItem.css';

const CartItem = ({ item, quantity, onRemove }) => {
  const imageUrl = `http://localhost/v1/restaurants/${item.restaurantId}/products/${item.id}/photo`;
  return (
    <div className='item-cart-container'>
      <div className="cart-itens-item cart-itens-title">
        <img 
            src={imageUrl} 
            alt={`Foto de ${item.name}`}
            className='cart-item-image'
            loading="lazy"
            onError={(e) => {
                e.target.onerror = null;
                e.target.src = semImagemPng;
            }}
        />
        <p className='cart-item-name'>{item.name}</p>
        <p className='cart-item-price'>R$ {item.price.toFixed(2)}</p>
        <p className='cart-item-quantity'>{quantity}</p>
        <p className='cart-item-total'>R$ {(item.price * quantity).toFixed(2)}</p>
        
        {/* O SVG gigante agora fica escondido aqui dentro! */}
        <svg
          onClick={() => onRemove(item.id)}
          className="close-icon-cart"
          xmlns="http://www.w3.org/2000/svg"
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
            >
          <path stroke="none" d="M0 0h24v24H0z" fill="none" />
          <path d="M18 6l-12 12" />
          <path d="M6 6l12 12" />
        </svg>
      </div>
      <hr />
    </div>
  );
};

export default CartItem;
import React, { useContext } from 'react';
import semImagemPng from '../../../assets/sem-foto.png';
import './ProductCardDisplay.css';
import { Link } from 'react-router-dom';

import addToCartIcon from '../../../assets/add_icon_white.png';
import menos from '../../../assets/menos.png';
import { StoreContext } from '../../context/StoreContext';

const ProductCardDisplay = ({ product }) => {

  const { cartItems, addToCart, removeFromCart } = useContext(StoreContext);
  const quantity = cartItems[product.id] || 0;

  return (
    <div className="product-card">
      <Link to={`/restaurant/${product.restaurantId}`} className="product-link">
      <div className="product-image-container">
        <img 
          src={`http://localhost/v1/restaurants/${product.restaurantId}/products/${product.id}/photo`} 
          alt={`Foto de ${product.name}`}
          className="product-image"
          loading="lazy"
          onError={(e) => {
            e.target.onerror = null; 
            e.target.src = semImagemPng;
          }}
        />
      </div>
      
      <div className="product-info">
        <h3>{product.name}</h3>
        <p className="description">{product.description}</p>
        <span className="price">
          {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(product.price)}
        </span>
      </div>
      </Link>
      <div className="product-add">
      {quantity > 0 && (
          <>
            <button className="btn-add-product" onClick={() => removeFromCart(product.id)}>
              <img src={menos} alt="Remover" />
            </button>
            <span className="product-quantity">{quantity}</span>
          </>
        )}
        <button className="btn-add-product" onClick={() => addToCart(product.id)}>
          <img src={addToCartIcon} alt="Adicionar" />
        </button>
    </div>
    </div>
  );
};

export default ProductCardDisplay;
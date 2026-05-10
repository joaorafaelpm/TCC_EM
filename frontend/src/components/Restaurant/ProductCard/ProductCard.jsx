import React, { useContext } from 'react';
import semImagemPng from '../../../assets/sem-foto.png';
import addToCartIcon from '../../../assets/add_icon_white.png';
import menos from '../../../assets/menos.png';
import './ProductCard.css';
import { StoreContext } from '../../context/StoreContext';

const ProductCard = ({ product}) => {
  // A URL agora utiliza o ID do restaurante e do produto conforme sua API
  const imageUrl = `http://localhost/v1/restaurants/${product.restaurantId}/products/${product.id}/photo`;
  const { cartItems, addToCart, removeFromCart } = useContext(StoreContext);
  const quantity = cartItems[product.id] || 0;
  return (
    <div className="product-card">
      <img 
        src={imageUrl} 
        alt={`Foto de ${product.name}`}
        className="product-image"
        loading="lazy"
        onError={(e) => {
          e.target.onerror = null;
          e.target.src = semImagemPng;
        }}
      />
      <div className="product-info">
        <h3>{product.name}</h3>
        <p className="description">{product.description}</p>
        <span className="price">
          {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(product.price)}
        </span>
      </div>

       <div className="product-add">
          {/* Mostra o botão de MENOS e a QUANTIDADE apenas se já tiver no carrinho */}
          {quantity > 0 && (
            <>
              <button
                className="btn-add-product"
                onClick={() => removeFromCart(product.id)}
              >
                <img src={menos} alt="Remover" />
              </button>
              
              <span className="product-quantity">{quantity}</span>
            </>
          )}

          {/* O botão de MAIS fica sempre visível */}
          <button
            className="btn-add-product"
            onClick={() => addToCart(product.id)}
          >
            <img src={addToCartIcon} alt="Adicionar" />
          </button>
        </div>

    </div>
  );
};

export default ProductCard;
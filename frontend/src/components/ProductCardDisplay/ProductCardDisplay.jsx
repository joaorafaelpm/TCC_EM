import React from 'react';
import semImagemPng from '../../assets/sem-foto.png';
import './ProductCardDisplay.css';
import { Link } from 'react-router-dom';

const ProductCardDisplay = ({ product }) => {
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
    </div>
  );
};

export default ProductCardDisplay;
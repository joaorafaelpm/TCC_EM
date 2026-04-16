import React from 'react';
import semImagemPng from '../../../assets/sem-foto.png';
import './ProductCard.css';

const ProductCard = ({ product, restaurantId }) => {
  // A URL agora utiliza o ID do restaurante e do produto conforme sua API
  const imageUrl = `http://localhost/v1/restaurants/${restaurantId}/products/${product.id}/photo`;

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
    </div>
  );
};

export default ProductCard;
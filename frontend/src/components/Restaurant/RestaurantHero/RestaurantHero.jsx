import React from 'react';
import './RestaurantHero.css';
import semImagemPng from '../../../assets/sem-foto.png';

const RestaurantHero = ({ restaurant }) => {
  const { name, open, address, shippingFee } = restaurant;

  const formattedShipping = shippingFee > 0 
    ? new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(shippingFee) 
    : 'Grátis';

  return (
    <section className="restaurant-hero">
      <div className="hero-content">
        <div className="img-wrapper">
          <img 
            src={`http://localhost/v1/restaurants/${restaurant.id}/photo`} 
            alt={name} 
            onError={(e) => {
              e.target.onerror = null; 
              e.target.src = semImagemPng;
            }}
            className={`status-image-restaurant ${open ? 'open' : 'closed'}`}
          />
        </div>
        
        <div className="info-main">
          <div className="title-group">
            <h1>{name}</h1>
            <span className={`status-badge ${open ? 'open' : 'closed'}`}>
              {open ? 'Aberto' : 'Fechado'}
            </span>
          </div>
          
          <p className="address">
            {address?.street}, {address?.number} - {address?.neighborhood}
            <br />
            {address?.city?.name} / {address?.city?.state}
          </p>
        </div>

        <div className="delivery-info">
          <div className="info-item">
            <span className="label">Frete: </span>
            <span className="value"><strong>{formattedShipping}</strong></span>
          </div>
        </div>
      </div>
    </section>
  );
};

export default RestaurantHero;
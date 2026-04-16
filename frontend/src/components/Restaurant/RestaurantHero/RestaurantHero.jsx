import React from 'react';
import './RestaurantHero.css';

const RestaurantHero = ({ restaurant }) => {
  const { name, open, address, shippingFee } = restaurant;

  const formattedShipping = shippingFee > 0 
    ? new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(shippingFee) 
    : 'Grátis';

  return (
    <section className="restaurant-hero">
      <div className="hero-content">
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
            <span className="label">Frete</span>
            <span className="value">{formattedShipping}</span>
          </div>
        </div>
      </div>
    </section>
  );
};

export default RestaurantHero;
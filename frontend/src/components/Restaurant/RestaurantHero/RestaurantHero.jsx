import React from 'react';
import './RestaurantHero.css';
import semImagemPng from '../../../assets/sem-foto.png';

const RestaurantHero = ({ restaurant, canEdit, onEditClick }) => {
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

          {canEdit && (
            <button className="hero-edit-btn" onClick={onEditClick}>
              <svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 24 24"
                fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" />
                <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
              </svg>
              Editar restaurante
            </button>
          )}
        </div>
      </div>
    </section>
  );
};

export default RestaurantHero;
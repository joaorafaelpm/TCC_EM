import React from 'react';

const RestaurantCard = ({ restaurant }) => {
  const { id, name, description, active, open } = restaurant;

  return (
    <a
      href={`http://localhost:5173/restaurant/${id}`}
      className="restaurant-card"
    >
      <div className="restaurant-card__header">
        <div className="restaurant-card__icon">🍴</div>
        <div className="restaurant-card__badges">
          <span className={`restaurant-card__badge restaurant-card__badge--${active ? 'active' : 'inactive'}`}>
            {active ? 'Ativo' : 'Inativo'}
          </span>
          <span className={`restaurant-card__badge restaurant-card__badge--${open ? 'open' : 'closed'}`}>
            {open ? 'Aberto' : 'Fechado'}
          </span>
        </div>
      </div>

      <div className="restaurant-card__body">
        <h3 className="restaurant-card__name">{name}</h3>
        <p className="restaurant-card__description">
          {description || 'Sem descrição'}
        </p>
      </div>

      <div className="restaurant-card__footer">
        <span className="restaurant-card__link">
          Ver restaurante →
        </span>
      </div>
    </a>
  );
};

export default RestaurantCard;

import React, { useEffect, useState } from 'react';
import RestaurantCard from './RestaurantCard.jsx';
import './MyRestaurants.css';

const MyRestaurants = ({ userId }) => {
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchRestaurants = async () => {
      try {
        const res = await fetch(`/v1/users/${userId}/restaurants`, {
          credentials: 'include',
        });

        if (!res.ok) throw new Error('Erro ao buscar restaurantes.');

        const data = await res.json();
        setRestaurants(data.content || []);
        console.log('Restaurantes carregados:', data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchRestaurants();
  }, [userId]);

  return (
    <section className="my-restaurants">
      <div className="my-restaurants__header">
        <div>
          <h1 className="my-restaurants__title">Meus restaurantes</h1>
          <p className="my-restaurants__subtitle">Gerencie os seus estabelecimentos</p>
        </div>
      </div>

      {loading && (
        <div className="my-restaurants__loading">
          <div className="my-restaurants__spinner" />
          <p>Carregando restaurantes...</p>
        </div>
      )}

      {error && (
        <div className="my-restaurants__error">
          ⚠️ {error}
        </div>
      )}

      {!loading && !error && restaurants.length === 0 && (
        <div className="my-restaurants__empty">
          <span className="my-restaurants__empty-icon">🍽️</span>
          <p>Você ainda não possui restaurantes cadastrados.</p>
        </div>
      )}

      {!loading && !error && restaurants.length > 0 && (
        <div className="my-restaurants__grid">
          {restaurants.map(restaurant => (
            <RestaurantCard key={restaurant.id} restaurant={restaurant} />
          ))}
        </div>
      )}
    </section>
  );
};

export default MyRestaurants;

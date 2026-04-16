import React, { useEffect, useState } from 'react';
import './ExploreMenu.css';
import ExploreMenuItem from './ExploreMenuItem/ExploreMenuItem.jsx';

const ExploreMenu = ({ category, setCategory }) => {
  const [restaurants, setRestaurants] = useState([]);

  useEffect(() => {
    const fetchRestaurants = async () => {
      try {
        const response = await fetch("http://localhost/v1/restaurants", { credentials: 'include' });
        const data = await response.json();
        setRestaurants(data.content || []);
      } catch (err) {
        console.error("Erro ao carregar restaurantes:", err);
      }
    };
    fetchRestaurants();
  }, []);

  return (
    <section className='explore-menu' id='explore-menu'>
      <header className="explore-menu-header">
        <div className="titulos-wrapper">
          <h1 className='title-primary'>Especiais</h1>
          <h1 className='title-secondary'>para você!</h1>
        </div>
        <p className='section-subtitle'>Nossos Restaurantes</p>
      </header>

      <div className='explore-menu-grid'>
        {restaurants.map((item) => (
          <ExploreMenuItem 
            key={item.id}
            id={item.id}
            name={item.name}
            category={item.category} // Se tiver categoria no futuro
            activeCategory={category}
          />
        ))}
      </div>
      <hr className="explore-hr" />
    </section>
  );
};

export default ExploreMenu;
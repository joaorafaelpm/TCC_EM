import React from 'react';
import './ExploreMenuItem.css';
import semImagemPng from '../../../assets/sem-foto.png';

const ExploreMenuItem = ({ id, name, category, activeCategory }) => {
  return (
    <div className={`explore-menu-item ${activeCategory === category ? "active" : ""}`}>
      <a href={`/restaurant/${id}`} className="explore-menu-link">
        <div className="img-wrapper">
           {/* Assumindo que a foto do restaurante segue o padrão de id */}
          <img 
          src={`http://localhost/v1/restaurants/${id}/photo`} 
          alt={name} 
          onError={(e) => {
            // Se a imagem falhar (ex: 404 do backend), troca a fonte para o placeholder
            e.target.onerror = null; // Previne loop infinito se o placeholder também falhar
            e.target.src = semImagemPng;
            }}/>
        </div>
        <p>{name}</p>
      </a>
    </div>
  );
};

export default ExploreMenuItem;
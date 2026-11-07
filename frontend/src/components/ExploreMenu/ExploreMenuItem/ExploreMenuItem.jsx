import React from 'react';
import './ExploreMenuItem.css';
import semImagemPng from '../../../assets/sem-foto.png';
import { Link } from 'react-router-dom';
import { API_URL } from '../../../config/api';

const ExploreMenuItem = ({ id, name}) => {
  return (
    <div className={`explore-menu-item`}>
      <Link to={`/restaurant/${id}`} className="explore-menu-link">
        <div className="img-wrapper">
          <img 
          src={`${API_URL}/v1/restaurants/${id}/photo`} 
          alt={name} 
          onError={(e) => {
            e.target.onerror = null;
            e.target.src = semImagemPng;
            }}/>
        </div>
        <p>{name}</p>
      </Link>
    </div>
  );
};

export default ExploreMenuItem;

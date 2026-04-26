import React from 'react'
import './Banner.css'
import { assets } from '../../assets/assets'
import { Link } from 'react-router-dom';

const Banner = () => {
  return (
    <section className="banner">
      <div className="banner-container">
        <div className="banner-logo">
          <img src={assets.logo3} alt="Logo da pizzaria" />
        </div>

        <div className="banner-content">
          <h2 className="banner-title">
            Rápido, Fácil, Saboroso <br /> 
            <span>e do lado do povo. Do seu lado!</span>
          </h2>
        </div>

        <div className="banner-info">
          <p className="support-label">Contate o suporte</p>
          <Link to="tel:19997468594" className="phone-number">(19) 99746-8594</Link>
        </div>
      </div>
    </section>
  )
}

export default Banner
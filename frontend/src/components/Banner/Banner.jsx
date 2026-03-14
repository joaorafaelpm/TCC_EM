import React from 'react'
import './Banner.css'
import { assets } from '../../assets/assets'

const Banner = () => {
  return (
    <div className="banner">
 
  <div className="banner-logo">
    <img src={assets.logo3} alt="Logo da pizzaria" />
  </div>

  
  <div className="banner-text">
  

    <h2>
      Peça agora a melhor <br />
      pizza da cidade!
    </h2>
  </div>

  
  <div className="banner-phone">
    (19) 99746-8594
  </div>
</div>
  )
}

export default Banner

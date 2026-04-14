import React, { useEffect, useState } from 'react'
import './ExploreMenu.css'

const ExploreMenu = () => {
  const [exploreMenuItems, setExploreMenuItems] = useState([])
  useEffect(() => {
    (async () => {
      const response = await fetch("http://localhost/v1/restaurants", { credentials: 'include' });
      const data = await response.json();
      setExploreMenuItems(data.content);
    })();
  }, [])

  return (
    
    <div className='explore-menu' id='explore-menu'>
      
      <div className="titulos-explore-menu">
        <h1 className='explore-menu-title'>Especiais</h1>
        <h1 className='explore-menu-title-2'>para você!</h1>
      </div>

      <div className='explore-menu-p'>
        <p className='explore-menu-text'>Nossos Restaurantes</p>
      </div>
 
      <div className='explore-menu-items'>
        {exploreMenuItems.map((item, index) => (
          <div key={index} className={`explore-menu-item`}>
            <a href={`http://localhost:5173/restaurant/${item.id}`} target="_blank" rel="noopener noreferrer">
              <p>{item.name}</p>
            </a>
          </div>
        ))}
      </div>

      <hr />
    </div>
  )
}

export default ExploreMenu

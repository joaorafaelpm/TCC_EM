import React from 'react'
import './ExploreMenu.css'
import { category_list } from '../../assets/assets'

const ExploreMenu = ({ category, setCategory }) => {
  return (
    
    <div className='explore-menu' id='explore-menu'>
      
      <div className="titulos-explore-menu">
        <h1 className='explore-menu-title'>Especiais</h1>
        <h1 className='explore-menu-title-2'>para você!</h1>
      </div>

      <div className='explore-menu-p'>
        <p className='explore-menu-text'>Nosso Cardápio</p>
      </div>
 
      <div className='explore-menu-items'>
        {category_list.map((item, index) => (
          <div
            key={index}
            className={`explore-menu-item-list ${category === item.category ? 'active' : ''}`}
            onClick={() => setCategory(item.category)}
            
          >
            <img src={item.image} alt={item.name} />
            <p>{item.name}</p>
          </div>
        ))}
      </div>

      <hr />
    </div>
  )
}

export default ExploreMenu

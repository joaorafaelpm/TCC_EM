import React from 'react'
import './FoodItem.css'
import { assets } from '../../assets/assets'
import { useContext } from 'react'
import { StoreContext } from '../context/StoreContext'


const FoodItem = ({id,name,description,price,image}) => {

    const {cartItems, addToCart, removeFromCart} = useContext(StoreContext);

  return (
    <div className='food-item' >
      <div className="food-item-img-container">
        <img className="food-item-img" src={image} alt='' />
        {!cartItems[id] 
          ?<img className="add" onClick={() => addToCart(id)} src={assets.add_icon_white}  alt=''/>
            : <div className='food-item-container'>
                <img className="add-btn" onClick={() => removeFromCart(id)} src={assets.menos}  alt=''/>
              <p>{cartItems[id]}</p>
                <img  className="add-btn" onClick={() => addToCart(id)} src={assets.mais}  alt=''/>
              </div>
          }
      </div>
      <div className="food-item-info">
          <p className='food-item-name-rating'>{name}</p>
      </div>
      <p className='food-item-desc'>{description}</p>
      <p className='food-item-price'>R$ {price}</p>
    </div>
  )
}

export default FoodItem

import React, { useContext, useEffect, useState } from 'react'
import './FoodDisplay.css'
import { StoreContext } from '../context/StoreContext.jsx'
import FoodItem from '../FoodItem/FoodItem.jsx'

const FoodDisplay = ({ category }) => {
  const { food_list } = useContext(StoreContext)
  const [showAll, setShowAll] = useState(false)

  // 🔹 Reseta ao trocar categoria
  useEffect(() => {
    setShowAll(false)
  }, [category])

  // 🔹 Filtra
  const filteredList = food_list.filter(item =>
    category === 'all' || item.category === category
  )

  // 🔹 Quantidade visível
  const visibleItems = showAll
    ? filteredList
    : filteredList.slice(0, 5)

  return (
    <div className="food-display" id="food-display">

      {/* 🔹 TÍTULO VINDO DA CATEGORY LIST */}
      {category !== 'all' && (
        <h2 className="food-display-title">
          {category}
        </h2>
      )}

      <div className="food-display-list">
        {visibleItems.map(item => (
          <FoodItem
            key={item._id}
            id={item._id}
            name={item.name}
            description={item.description}
            price={item.price}
            image={item.image}
          />
        ))}
      </div>

      {/* 🔹 BOTÃO */}
      {filteredList.length > 5 && (
        <div className="show-more-container">
          <button
            className="show-more-btn"
            onClick={() => setShowAll(!showAll)}
          >
            {showAll ? 'Mostrar menos' : 'Ver todo o cardápio'}
          </button>
        </div>
      )}

    </div>
  )
}

export default FoodDisplay

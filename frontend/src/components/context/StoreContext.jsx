import React, { createContext, useState } from 'react';

export const StoreContext = createContext(null);

const StoreContextProvider = ({ children }) => {
  const [cartItems, setCartItems] = useState({});
  const [food_list, setFoodList] = useState([]);

  const addToCart = (productId) => {
    setCartItems(prev => ({
      ...prev,
      [productId]: (prev[productId] || 0) + 1
    }));
  };

  const removeFromCart = (productId) => {
    setCartItems(prev => ({
      ...prev,
      [productId]: Math.max((prev[productId] || 0) - 1, 0)
    }));
  };
  const clearCart = () => setCartItems({});
  return (
    <StoreContext.Provider value={{ cartItems, food_list, setFoodList, addToCart, removeFromCart, clearCart }}>
      {children}
    </StoreContext.Provider>
  );
};

export default StoreContextProvider;
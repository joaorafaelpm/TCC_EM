import React, { createContext, useState } from 'react';

export const StoreContext = createContext(null);

const StoreContextProvider = ({ children }) => {
  const [cartItems, setCartItems] = useState({});

  const [productMap, setProductMap] = useState({});

  const registerProducts = (products ) => {
    setProductMap(prev => {
      const updated = { ...prev };
      Array.from(products).forEach(product => { updated[product.id] = product; });
      return updated;
    });
  };

  const food_list = Object.values(productMap);

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
    <StoreContext.Provider value={{
      cartItems,
      food_list,
      productMap,
      registerProducts,
      setFoodList: registerProducts,
      addToCart,
      removeFromCart,
      clearCart
    }}>
      {children}
    </StoreContext.Provider>
  );
};

export default StoreContextProvider;
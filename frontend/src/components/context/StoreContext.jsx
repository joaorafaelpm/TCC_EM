import { createContext, useState, useEffect } from 'react';
import { food_list } from '../../assets/assets';

export const StoreContext = createContext(null);

const StoreContextProvidor = (prop) => {

    const [cartItems, setCartItems] = useState({});

    const addToCart = (itemId) => {
        if (cartItems[itemId]) {
            setCartItems((prev) => ({ ...prev, [itemId]: prev[itemId] + 1 }));
        } else {
            setCartItems((prev) => ({ ...prev, [itemId]: 1 }));
        }
    };

    const removeFromCart = (itemId) => {
        setCartItems((prev) => {
            const current = prev[itemId] || 0;
            const updated = current - 1;
            if (updated > 0) return { ...prev, [itemId]: updated };
            const { [itemId]: _, ...rest } = prev;
            return rest;
        });
    };

    useEffect(() => {
        console.log(cartItems);
    }, [cartItems]);

    

 

 const contextValue = {

    food_list,
    cartItems,
    addToCart,
    removeFromCart

};

    return (
        <StoreContext.Provider value={contextValue}>
            {prop.children}
        </StoreContext.Provider>
    )
}

export default StoreContextProvidor;  
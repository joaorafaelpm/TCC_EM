import React, { createContext, useState, useEffect, useRef } from 'react';

export const StoreContext = createContext(null);

const CART_STORAGE_KEY = 'pendezzapizza_cart';

const loadCartFromStorage = () => {
  try {
    const saved = localStorage.getItem(CART_STORAGE_KEY);
    return saved ? JSON.parse(saved) : {};
  } catch (err) {
    console.error('Erro ao ler carrinho salvo:', err);
    return {};
  }
};

const StoreContextProvider = ({ children }) => {
  const [cartItems, setCartItems] = useState(loadCartFromStorage);
  const [productMap, setProductMap] = useState({});

  // Evita disparar fetch duplicado pro mesmo produto enquanto a
  // requisição anterior ainda está em andamento.
  const fetchingIds = useRef(new Set());

  // Persiste o carrinho a cada mudança
  useEffect(() => {
    try {
      localStorage.setItem(CART_STORAGE_KEY, JSON.stringify(cartItems));
    } catch (err) {
      console.error('Erro ao salvar carrinho:', err);
    }
  }, [cartItems]);

  const registerProducts = (products) => {
    setProductMap(prev => {
      const updated = { ...prev };
      Array.from(products).forEach(product => { updated[product.id] = product; });
      return updated;
    });
  };

  // Garante que todo produto presente no carrinho tenha seus dados
  // carregados, mesmo que o usuário nunca tenha passado pela página
  // que os buscaria normalmente (ex: F5 direto no /cart).
  useEffect(() => {
    const missingIds = Object.keys(cartItems).filter(
      id => cartItems[id] > 0 && !productMap[id] && !fetchingIds.current.has(id)
    );
    if (missingIds.length === 0) return;

    missingIds.forEach(id => fetchingIds.current.add(id));

    Promise.all(
      missingIds.map(id =>
        fetch(`/v1/products/${id}`, { credentials: 'include' })
          .then(r => (r.ok ? r.json() : null))
          .catch(err => {
            console.error(`Erro ao buscar produto ${id}:`, err);
            return null;
          })
          .finally(() => fetchingIds.current.delete(id))
      )
    ).then(results => {
      const found = results.filter(Boolean);
      if (found.length > 0) registerProducts(found);
    });
  }, [cartItems, productMap]);

  const food_list = Object.values(productMap);

  const addToCart = (productId) => {
    setCartItems(prev => ({ ...prev, [productId]: (prev[productId] || 0) + 1 }));
  };

  const removeFromCart = (productId) => {
    setCartItems(prev => ({
      ...prev,
      [productId]: Math.max((prev[productId] || 0) - 1, 0)
    }));
  };

  // Remove uma lista de produtos do carrinho de uma vez só (zera a
  // quantidade de cada um). Usado, por exemplo, quando o usuário
  // decide tirar do carrinho todos os itens de um restaurante que
  // não pode receber o pedido (sem forma de pagamento cadastrada).
  const removeProducts = (productIds) => {
    setCartItems(prev => {
      const updated = { ...prev };
      productIds.forEach(id => { updated[id] = 0; });
      return updated;
    });
  };

  const clearCart = () => setCartItems({});


  return (
    <StoreContext.Provider value={{
      cartItems, food_list, productMap,
      registerProducts, setFoodList: registerProducts,
      addToCart, removeFromCart, removeProducts, clearCart
    }}>
      {children}
    </StoreContext.Provider>
  );
};

export default StoreContextProvider;
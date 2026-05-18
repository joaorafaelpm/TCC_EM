import React, { useContext, useEffect, useState } from 'react';
import './FoodDisplay.css';
import ProductCardDisplay from './ProductCardDisplay/ProductCardDisplay.jsx';
import { StoreContext } from '../context/StoreContext.jsx';

const FoodDisplay = ({ category }) => {
  const { registerProducts } = useContext(StoreContext);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showAll, setShowAll] = useState(false);

  // Reseta o botão "Mostrar mais" quando a categoria muda
  useEffect(() => {
    setShowAll(false);
  }, [category]);

  // Busca os produtos na API
   useEffect(() => {
    const fetchProducts = async () => {
      try {
        const response = await fetch('/v1/products', { credentials: 'include' });
        const data = await response.json();
        const list = data.content || data || [];
        setProducts(list);
        registerProducts(list); // ← registra no catálogo global
      } catch (error) {
        console.error("Erro ao buscar produtos do cardápio:", error);
      } finally {
        setLoading(false);
      }
    };
    fetchProducts();
  }, []);

  if (loading) {
    return <div className="loading">Preparando o cardápio...</div>;
  }

  const filteredList = Array.from(products).filter(product => {
    const isCategoryMatch = category === 'all' || product.category === category;
    return isCategoryMatch && product.active; 
  });

  const visibleItems = showAll ? filteredList : filteredList.slice(0, 5);

   return (
    <div className="food-display" id="food-display">
      
      {category !== 'all' && (
        <h2 className="food-display-title">
          {category}
        </h2>
      )}

      {/* Grid de Produtos */}
      <div className="food-display-list">
        {visibleItems.length > 0 ? (
          visibleItems.map(product => (
            <ProductCardDisplay 
              key={product.id} 
              product={product} 
            />
          ))
        ) : (
          <p className="placeholder-text">Nenhum produto encontrado para esta categoria.</p>
        )}
      </div>

      {/* Botão Mostrar Mais */}
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
  );
};

export default FoodDisplay;
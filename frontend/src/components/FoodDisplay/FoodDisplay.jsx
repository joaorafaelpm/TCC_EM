import React, { useContext, useEffect, useState } from 'react';
import './FoodDisplay.css';
import ProductCardDisplay from './ProductCardDisplay/ProductCardDisplay.jsx';
import { StoreContext } from '../context/StoreContext.jsx';

const PAGE_SIZE = 10;

const FoodDisplay = ({ category }) => {
  const { registerProducts } = useContext(StoreContext);
  const [products, setProducts] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(true);

  // Volta para página 0 quando muda de categoria
  useEffect(() => {
    setCurrentPage(0);
  }, [category]);

  useEffect(() => {
    setLoading(true);

    const params = new URLSearchParams({ currentPage, size: PAGE_SIZE });

    fetch(`/v1/products?${params}`, { credentials: 'include' })
      .then(r => r.json())
      .then(data => {
        const list = data.content || [];
        setProducts(list);
        setTotalPages(data.totalPage || 1);
        registerProducts(list);
      })
      .catch(err => console.error("Erro ao buscar produtos:", err))
      .finally(() => setLoading(false));
  }, [category, currentPage]);

  if (loading) return <div className="loading">Preparando o cardápio...</div>;

  return (
    <div className="food-display" id="food-display">

      {category !== 'all' && (
        <h2 className="food-display-title">{category}</h2>
      )}

      <div className="food-display-list">
        {products.length > 0 ? (
          products.map(product => (
            <ProductCardDisplay key={product.id} product={product} />
          ))
        ) : (
          <p className="placeholder-text">Nenhum produto encontrado para esta categoria.</p>
        )}
      </div>

      {totalPages > 1 && (
        <div className="food-pagination">
          <button
            className="page-btn"
            disabled={currentPage === 0}
            onClick={() => setCurrentPage(p => p - 1)}
          >
            ← Anterior
          </button>
          <span className="page-info">{currentPage + 1} de {totalPages}</span>
          <button
            className="page-btn"
            disabled={currentPage >= totalPages - 1}
            onClick={() => setCurrentPage(p => p + 1)}
          >
            Próxima →
          </button>
        </div>
      )}

    </div>
  );
};

export default FoodDisplay;
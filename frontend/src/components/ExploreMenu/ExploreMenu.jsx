import React, { useEffect, useState } from 'react';
import './ExploreMenu.css';
import ExploreMenuItem from './ExploreMenuItem/ExploreMenuItem.jsx';

const PAGE_SIZE = 5;

const ExploreMenu = () => {
  const [restaurants, setRestaurants] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);

    const params = new URLSearchParams({ currentPage, size: PAGE_SIZE });

    fetch(`/v1/restaurants?${params}`, { credentials: 'include' })
      .then(r => r.json())
      .then(data => {
        setRestaurants(data.content || []);
        setTotalPages(data.totalPage || 1);
      })
      .catch(err => console.error("Erro ao carregar restaurantes:", err))
      .finally(() => setLoading(false));
  }, [currentPage]);

  return (
    <section className='explore-menu' id='explore-menu'>
      <header className="explore-menu-header">
        <div className="titulos-wrapper">
          <h1 className='title-primary'>Especiais</h1>
          <h1 className='title-secondary'>para você!</h1>
        </div>
        <p className='section-subtitle'>Nossos Restaurantes</p>
      </header>

      {loading ? (
        <div className="loading">Carregando restaurantes...</div>
      ) : (
        <>
          <div className='explore-menu-grid'>
            {restaurants.map((item) => (
              <ExploreMenuItem key={item.id} id={item.id} name={item.name} />
            ))}
          </div>

          {totalPages > 1 && (
            <div className="explore-pagination">
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
        </>
      )}

      <hr className="explore-hr" />
    </section>
  );
};

export default ExploreMenu;
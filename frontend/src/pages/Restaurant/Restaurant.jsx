import React, { useContext, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import RestaurantHero from '../../components/Restaurant/RestaurantHero/RestaurantHero.jsx';
import ProductCard from '../../components/Restaurant/ProductCard/ProductCard.jsx';
import EditRestaurantModal from '../../components/Restaurant/EditRestaurantModal/EditRestaurantModal.jsx';
import AddProductModal from '../../components/Restaurant/AddProductModal/AddProductModal.jsx';
import './Restaurant.css';
import { StoreContext } from '../../components/context/StoreContext.jsx';
import { useAuth } from '../../components/context/AuthProvider.jsx';

const Restaurant = () => {
  const { id } = useParams();
  const { user: tokenData } = useAuth();

  const [restaurant, setRestaurant] = useState(null);
  const [loading, setLoading] = useState(true);
  const [canEdit, setCanEdit] = useState(false);

  const [showEditRestaurant, setShowEditRestaurant] = useState(false);
  const [showAddProduct, setShowAddProduct] = useState(false);

  const { food_list, setFoodList } = useContext(StoreContext);

  // Verifica se o usuário tem a authority e é responsável pelo restaurante
  const hasAuthority = tokenData?.authorities?.includes('EDITAR_RESTAURANTES');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const productsUrl = hasAuthority
          ? `http://localhost:80/v1/restaurants/${id}/products?includeInactives=true`
          : `http://localhost/v1/restaurants/${id}/products`;

        const [restaurantRes, productsRes] = await Promise.all([
          fetch(`http://localhost/v1/restaurants/${id}`, { credentials: 'include' }),
          fetch(productsUrl, { credentials: 'include' })
        ]);

        const restaurantData = await restaurantRes.json();
        const productsData = await productsRes.json();

        setRestaurant(restaurantData);
        setFoodList(productsData.content || []);
      } catch (error) {
        console.error("Erro ao buscar dados:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [id, setFoodList]);

  // Checa se o usuário logado é responsável por este restaurante
  useEffect(() => {
    if (!hasAuthority) return;

    const checkResponsible = async () => {
      try {
        const res = await fetch(`http://localhost:80/v1/restaurants/exists-responsible/${id}`, {
          credentials: 'include',
        });
        const data = await res.json();
        setCanEdit(data === true);
      } catch {
        setCanEdit(false);
      }
    };

    checkResponsible();
  }, [id, hasAuthority]);

  const handleProductToggle = (productId, newActive) => {
    setFoodList(prev =>
      prev.map(p => p.id === productId ? { ...p, active: newActive } : p)
    );
  };

  const handleProductUpdate = (updatedProduct) => {
    setFoodList(prev =>
      prev.map(p => p.id === updatedProduct.id ? { ...p, ...updatedProduct } : p)
    );
  };

  const handleProductAdded = (newProduct) => {
    setFoodList(prev => [...prev, newProduct]);
  };

  const handleRestaurantUpdate = (updatedRestaurant) => {
    setRestaurant(prev => ({ ...prev, ...updatedRestaurant }));
  };

  if (loading) return <div className="loading">Carregando detalhes...</div>;
  if (!restaurant) return <div className="error">Restaurante não encontrado.</div>;

  // Mostra todos os produtos para o dono, só os ativos para visitantes
  const displayedProducts = canEdit ? food_list : food_list.filter(p => p.active);

  return (
    <div className="restaurant-page">
      <RestaurantHero
        restaurant={restaurant}
        canEdit={canEdit}
        onEditClick={() => setShowEditRestaurant(true)}
      />

      <main className="products-container">
        <div className="products-header">
          <h2>Cardápio</h2>
          {canEdit && (
            <button
              className="btn-add-product-main"
              onClick={() => setShowAddProduct(true)}
            >
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24"
                fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
                <line x1="12" y1="5" x2="12" y2="19" /><line x1="5" y1="12" x2="19" y2="12" />
              </svg>
              Novo produto
            </button>
          )}
        </div>

        <div className="products-grid">
          {displayedProducts.length > 0 ? (
            displayedProducts.map(product => (
              <ProductCard
                key={product.id}
                product={product}
                canEdit={canEdit}
                restaurantId={id}
                onToggle={handleProductToggle}
                onUpdate={handleProductUpdate}
              />
            ))
          ) : (
            <p className="placeholder-text">Nenhum produto disponível no momento.</p>
          )}
        </div>
      </main>

      {showEditRestaurant && (
        <EditRestaurantModal
          restaurant={restaurant}
          onClose={() => setShowEditRestaurant(false)}
          onSave={handleRestaurantUpdate}
        />
      )}

      {showAddProduct && (
        <AddProductModal
          restaurantId={id}
          onClose={() => setShowAddProduct(false)}
          onProductAdded={handleProductAdded}
        />
      )}
    </div>
  );
};

export default Restaurant;
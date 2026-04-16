import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import Header from '../../components/Header/Header';
import RestaurantHero from '../../components/Restaurant/RestaurantHero/RestaurantHero.jsx';
import ProductCard from '../../components/Restaurant/ProductCard/ProductCard.jsx';
import './Restaurant.css';

const Restaurant = () => {
  const { id } = useParams();
  const [restaurant, setRestaurant] = useState(null);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [restaurantRes, productsRes] = await Promise.all([
          fetch(`http://localhost/v1/restaurants/${id}`, { credentials: 'include' }),
          fetch(`http://localhost/v1/restaurants/${id}/products`, { credentials: 'include' })
        ]);

        const restaurantData = await restaurantRes.json();
        const productsData = await productsRes.json();

        setRestaurant(restaurantData);
        setProducts(productsData.content || []);
      } catch (error) {
        console.error("Erro ao buscar dados:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [id]);

  if (loading) return <div className="loading">Carregando detalhes...</div>;
  if (!restaurant) return <div className="error">Restaurante não encontrado.</div>;

  return (
    <div className="restaurant-page">
      <RestaurantHero restaurant={restaurant} />

      <main className="products-container">
        <h2>Cardápio</h2>
        <div className="products-grid">
          {products.length > 0 ? (
            products
              .filter(p => p.active)
              .map(product => (
                <ProductCard 
                  key={product.id} 
                  product={product} 
                  restaurantId={id} 
                />
              ))
          ) : (
            <p className="placeholder-text">Nenhum produto disponível no momento.</p>
          )}
        </div>
      </main>
    </div>
  );
};

export default Restaurant;
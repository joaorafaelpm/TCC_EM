import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import Header from '../../components/Header/Header';
import './Restaurant.css';
import semImagemPng from '../../assets/sem-foto.png'; // Imagem genérica para produtos sem foto
const Restaurant = () => {
  const { id } = useParams();
  
  // Usamos null para um único objeto e [] para a lista de produtos
  const [restaurant, setRestaurant] = useState(null);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    const fetchData = async () => {
      try {
        // Promise.all executa os dois fetchs SIMULTANEAMENTE (muito mais rápido!)
        const [restaurantRes, productsRes] = await Promise.all([
          fetch(`http://localhost/v1/restaurants/${id}`, { credentials: 'include' }),
          fetch(`http://localhost/v1/restaurants/${id}/products`, { credentials: 'include' })
        ]);

        const restaurantData = await restaurantRes.json();
        const productsData = await productsRes.json();

        setRestaurant(restaurantData);
        setProducts(productsData.content || []); // Garante que pega o array de 'content'
      } catch (error) {
        console.error("Erro ao buscar dados da página:", error);
      } finally {
        setLoading(false); // Para de carregar independentemente de dar erro ou sucesso
      }
    };

    fetchData();
  }, [id]);

  if (loading) {
    return <div className="loading">Carregando detalhes do restaurante...</div>;
  }

  // Se o carregamento acabou e não tem restaurante, evita que a tela quebre
  if (!restaurant) {
    return <div className="error">Restaurante não encontrado.</div>;
  }

  return (
    <div className="restaurant-page">
      {/* Fachada / Hero Section */}
      <section className="restaurant-hero">
        <div className="hero-content">
          <div className="info-main">
            <div className="title-group">
              <h1>{restaurant.name}</h1>
              <span className={`status-badge ${restaurant.open ? 'open' : 'closed'}`}>
                {restaurant.open ? 'Aberto' : 'Fechado'}
              </span>
            </div>
            
            <p className="address">
              {restaurant.address?.street}, {restaurant.address?.number} - {restaurant.address?.neighborhood}
              <br />
              {restaurant.address?.city?.name} / {restaurant.address?.city?.state}
            </p>
          </div>

          <div className="delivery-info">
            <div className="info-item">
              <span className="label">Frete</span>
              <span className="value">
                {/* Formatação nativa de Moeda (melhor que toFixed) */}
                {restaurant.shippingFee > 0 
                  ? new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(restaurant.shippingFee) 
                  : 'Grátis'}
              </span>
            </div>
          </div>
        </div>
      </section>

      {/* Espaço para os Cards de Produtos */}
      <main className="products-container">
        <h2>Cardápio</h2>
        <div className="products-grid">
          {products.length > 0 ? (
            products
              .filter(product => product.active) // Filtra apenas os produtos ativos
              .map(product => (
                <div key={product.id} className="product-card">
                    <img 
                        src={`http://localhost/v1/restaurants/${id}/products/${product.id}/photo`} 
                        alt={`Foto de ${product.name}`}
                        className="product-image"
                        loading="lazy" /* MÁGICA: O navegador só baixa a imagem quando o usuário rolar a tela até ela! */
                        onError={(e) => {
                        // Se a imagem falhar (ex: 404 do backend), troca a fonte para o placeholder
                        e.target.onerror = null; // Previne loop infinito se o placeholder também falhar
                        e.target.src = semImagemPng;
                        }}
                    />
                  <div className="product-info">
                    <h3>{product.name}</h3>
                    <p className="description">{product.description}</p>
                    <span className="price">
                      {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(product.price)}
                    </span>
                  </div>
                </div>
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
import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import api from '../../services/api';
import { useAuth } from '../../components/context/AuthProvider';
import IngredientsTab from '../../components/Stock/IngredientsTab';
import RecipesTab from '../../components/Stock/RecipesTab';
import ReplenishmentTab from '../../components/Stock/ReplenishmentTab';
import '../../components/Stock/Stock.css';

const TABS = [
  { key: 'ingredients', label: 'Ingredientes' },
  { key: 'recipes', label: 'Receitas' },
  { key: 'report', label: 'Reposição' },
];

const RestaurantStock = () => {
  const { id: restaurantId } = useParams();
  const { user: tokenData } = useAuth();
  const userId = tokenData?.userId;
  const navigate = useNavigate();

  const [tab, setTab] = useState('ingredients');
  const [allowed, setAllowed] = useState(null); // null = verificando

  // Mesma checagem do RestaurantHero: só o responsável pelo restaurante acessa o estoque.
  useEffect(() => {
    let cancelled = false;
    if (!userId) {
      setAllowed(false);
      return undefined;
    }
    api.get(`/v1/restaurants/exists-responsible/${restaurantId}/${userId}`)
      .then((res) => { if (!cancelled) setAllowed(res.data === true); })
      .catch(() => { if (!cancelled) setAllowed(false); });
    return () => { cancelled = true; };
  }, [restaurantId, userId]);

  const backToRestaurant = () => navigate(`/restaurant/${restaurantId}`);

  if (allowed === null) {
    return <div className="stock-page"><p className="stock-muted">Verificando permissão...</p></div>;
  }

  if (!allowed) {
    return (
      <div className="stock-page">
        <div className="stock-empty">
          Você não tem permissão para gerenciar o estoque deste restaurante.
        </div>
        <button className="stock-btn stock-btn--ghost" onClick={backToRestaurant}>Voltar</button>
      </div>
    );
  }

  return (
    <div className="stock-page">
      <div className="stock-page__header">
        <h1>Estoque</h1>
        <button className="stock-btn stock-btn--ghost" onClick={backToRestaurant}>← Voltar ao restaurante</button>
      </div>

      <div className="stock-tabs" role="tablist">
        {TABS.map((t) => (
          <button
            key={t.key}
            role="tab"
            aria-selected={tab === t.key}
            className={`stock-tabs__btn ${tab === t.key ? 'is-active' : ''}`}
            onClick={() => setTab(t.key)}
          >
            {t.label}
          </button>
        ))}
      </div>

      {tab === 'ingredients' && <IngredientsTab restaurantId={restaurantId} />}
      {tab === 'recipes' && <RecipesTab restaurantId={restaurantId} />}
      {tab === 'report' && <ReplenishmentTab restaurantId={restaurantId} />}
    </div>
  );
};

export default RestaurantStock;

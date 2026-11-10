import React, { useCallback, useEffect, useState } from 'react';
import api from '../../../services/api';
import QuantityField from './QuantityField';
import { formatQty, getErrorMessage } from './stockUtils';

// ATENÇÃO: confirme o endpoint de listagem de produtos do restaurante e troque aqui se for outro.
const PRODUCTS_URL = (restaurantId) => `/v1/restaurants/${restaurantId}/products`;

const normalizeList = (data) => {
  if (Array.isArray(data)) return data;
  if (Array.isArray(data?.content)) return data.content; // resposta paginada
  return [];
};

const RecipesTab = ({ restaurantId }) => {
  const base = `/v1/restaurants/${restaurantId}/stock`;

  const [products, setProducts] = useState([]);
  const [ingredients, setIngredients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [productId, setProductId] = useState('');
  const [recipe, setRecipe] = useState([]);
  const [loadingRecipe, setLoadingRecipe] = useState(false);
  const [error, setError] = useState('');

  // formulário de item da receita
  const [ingredientId, setIngredientId] = useState('');
  const [quantity, setQuantity] = useState(0);
  const [initialQty, setInitialQty] = useState(0);
  const [formKey, setFormKey] = useState(0);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    let cancelled = false;
    Promise.all([
      api.get(PRODUCTS_URL(restaurantId)),
      api.get(`${base}/ingredients`),
    ])
      .then(([productsRes, ingredientsRes]) => {
        if (cancelled) return;
        setProducts(normalizeList(productsRes.data));
        setIngredients(normalizeList(ingredientsRes.data));
      })
      .catch((err) => {
        if (!cancelled) setError(getErrorMessage(err, 'Não foi possível carregar produtos e ingredientes.'));
      })
      .finally(() => { if (!cancelled) setLoading(false); });
    return () => { cancelled = true; };
  }, [restaurantId, base]);

  const loadRecipe = useCallback(async (pid) => {
    if (!pid) {
      setRecipe([]);
      return;
    }
    setLoadingRecipe(true);
    try {
      const { data } = await api.get(`${base}/products/${pid}/recipe`);
      setRecipe(Array.isArray(data) ? data : []);
      setError('');
    } catch (err) {
      setError(getErrorMessage(err, 'Não foi possível carregar a receita.'));
    } finally {
      setLoadingRecipe(false);
    }
  }, [base]);

  useEffect(() => { loadRecipe(productId); }, [productId, loadRecipe]);

  const selectedIngredient = ingredients.find((i) => i.id === ingredientId);

  const resetForm = () => {
    setIngredientId('');
    setQuantity(0);
    setInitialQty(0);
    setFormKey((k) => k + 1);
  };

  const handleSave = async () => {
    if (!productId || !ingredientId) return;
    if (!quantity || quantity <= 0) {
      setError('Informe uma quantidade maior que zero.');
      return;
    }
    setSaving(true);
    try {
      await api.put(`${base}/products/${productId}/recipe`, {
        ingredientId,
        quantityPerUnit: quantity,
      });
      resetForm();
      await loadRecipe(productId);
      setError('');
    } catch (err) {
      setError(getErrorMessage(err, 'Não foi possível salvar o item da receita.'));
    } finally {
      setSaving(false);
    }
  };

  const handleEdit = (item) => {
    setIngredientId(item.ingredientId);
    setQuantity(item.quantityPerUnit);
    setInitialQty(item.quantityPerUnit);
    setFormKey((k) => k + 1);
  };

  const handleRemove = async (item) => {
    if (!window.confirm(`Remover "${item.ingredientName}" desta receita?`)) return;
    try {
      await api.delete(`${base}/products/${productId}/recipe/${item.ingredientId}`);
      await loadRecipe(productId);
    } catch (err) {
      setError(getErrorMessage(err, 'Não foi possível remover o item.'));
    }
  };

  if (loading) return <p className="stock-muted">Carregando...</p>;

  return (
    <div>
      <p className="stock-hint">
        Defina quanto de cada ingrediente <strong>1 unidade</strong> do produto consome.
        Ex.: pizza de calabresa = 500 g de farinha + 500 g de calabresa.
        Produtos sem receita não descontam estoque.
      </p>

      {error && <p className="stock-banner stock-banner--error">⚠️ {error}</p>}

      <div className="stock-field stock-field--narrow">
        <label>Produto</label>
        <select
          value={productId}
          onChange={(e) => { setProductId(e.target.value); resetForm(); setError(''); }}
        >
          <option value="">Selecione um produto...</option>
          {products.map((p) => (
            <option key={p.id} value={p.id}>{p.name}</option>
          ))}
        </select>
      </div>

      {productId && (
        <>
          {loadingRecipe ? (
            <p className="stock-muted">Carregando receita...</p>
          ) : recipe.length === 0 ? (
            <div className="stock-empty">Este produto ainda não tem receita.</div>
          ) : (
            <div className="stock-table-wrap">
              <table className="stock-table">
                <thead>
                  <tr>
                    <th>Ingrediente</th>
                    <th>Consome por unidade</th>
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {recipe.map((item) => (
                    <tr key={item.ingredientId}>
                      <td data-label="Ingrediente"><strong>{item.ingredientName}</strong></td>
                      <td data-label="Consome por unidade">{formatQty(item.quantityPerUnit, item.unit)}</td>
                      <td data-label="Ações">
                        <div className="stock-actions">
                          <button className="stock-btn stock-btn--small stock-btn--ghost" onClick={() => handleEdit(item)}>
                            Editar
                          </button>
                          <button className="stock-btn stock-btn--small stock-btn--danger" onClick={() => handleRemove(item)}>
                            Remover
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}

          <div className="stock-card">
            <h3>Adicionar / atualizar ingrediente na receita</h3>

            {ingredients.length === 0 ? (
              <p className="stock-muted">Cadastre ingredientes primeiro, na aba "Ingredientes".</p>
            ) : (
              <>
                <div className="stock-field">
                  <label>Ingrediente</label>
                  <select
                    value={ingredientId}
                    onChange={(e) => { setIngredientId(e.target.value); setQuantity(0); setInitialQty(0); setFormKey((k) => k + 1); }}
                  >
                    <option value="">Selecione...</option>
                    {ingredients.map((i) => (
                      <option key={i.id} value={i.id}>{i.name}</option>
                    ))}
                  </select>
                </div>

                {selectedIngredient && (
                  <QuantityField
                    key={`${ingredientId}-${formKey}`}
                    label="Quantidade consumida por 1 unidade do produto"
                    unit={selectedIngredient.unit}
                    initialBase={initialQty}
                    onChange={setQuantity}
                  />
                )}

                <button
                  className="stock-btn stock-btn--primary"
                  onClick={handleSave}
                  disabled={saving || !ingredientId}
                >
                  {saving ? 'Salvando...' : 'Salvar na receita'}
                </button>
              </>
            )}
          </div>
        </>
      )}
    </div>
  );
};

export default RecipesTab;

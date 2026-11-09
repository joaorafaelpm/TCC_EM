import React, { useCallback, useEffect, useState } from 'react';
import api from '../../services/api';
import QuantityField from './QuantityField';
import { UNIT_OPTIONS, formatQty, getErrorMessage } from './stockUtils';

/* ------------------------------------------------------------------ */
/* Modal: criar / editar ingrediente                                   */
/* ------------------------------------------------------------------ */
const IngredientFormModal = ({ base, ingredient, onClose, onSaved }) => {
  const editing = Boolean(ingredient);
  const [name, setName] = useState(ingredient?.name ?? '');
  const [unit, setUnit] = useState(ingredient?.unit ?? 'G');
  const [threshold, setThreshold] = useState(ingredient?.lowStockThreshold ?? 0);
  const [initialQty, setInitialQty] = useState(0);
  const [errors, setErrors] = useState({});
  const [saving, setSaving] = useState(false);

  const validate = () => {
    const e = {};
    if (!name.trim()) e.name = 'Informe o nome do ingrediente.';
    if (threshold === null) e.threshold = 'Valor inválido.';
    if (initialQty === null) e.initialQty = 'Valor inválido.';
    setErrors(e);
    return Object.keys(e).length === 0;
  };

  const handleSave = async () => {
    if (!validate()) return;
    setSaving(true);
    try {
      if (editing) {
        await api.put(`${base}/ingredients/${ingredient.id}`, {
          name: name.trim(),
          lowStockThreshold: threshold,
        });
      } else {
        await api.post(`${base}/ingredients`, {
          name: name.trim(),
          unit,
          lowStockThreshold: threshold,
          initialQuantity: initialQty,
        });
      }
      onSaved();
    } catch (err) {
      setErrors({ general: getErrorMessage(err, 'Não foi possível salvar o ingrediente.') });
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="stock-overlay" onMouseDown={(e) => { if (e.target === e.currentTarget) onClose(); }}>
      <div className="stock-modal">
        <div className="stock-modal__header">
          <h2>{editing ? 'Editar ingrediente' : 'Novo ingrediente'}</h2>
          <button className="stock-modal__close" onClick={onClose} aria-label="Fechar">×</button>
        </div>

        <div className="stock-modal__body">
          {errors.general && <p className="stock-banner stock-banner--error">⚠️ {errors.general}</p>}

          <div className="stock-field">
            <label>Nome</label>
            <input
              type="text"
              maxLength={120}
              placeholder="Ex.: Farinha de trigo"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
            {errors.name && <span className="stock-field__error">{errors.name}</span>}
          </div>

          <div className="stock-field">
            <label>Tipo de medida</label>
            <select
              value={unit}
              disabled={editing}
              onChange={(e) => {
                setUnit(e.target.value);
                setThreshold(0);
                setInitialQty(0);
              }}
            >
              {UNIT_OPTIONS.map((o) => (
                <option key={o.value} value={o.value}>{o.label}</option>
              ))}
            </select>
            {editing && <small className="stock-muted">A unidade não pode ser alterada depois de criada.</small>}
          </div>

          <QuantityField
            key={`min-${unit}`}
            label="Estoque mínimo (alerta de estoque baixo)"
            unit={unit}
            initialBase={ingredient?.lowStockThreshold ?? 0}
            onChange={setThreshold}
            error={errors.threshold}
          />

          {!editing && (
            <QuantityField
              key={`ini-${unit}`}
              label="Quantidade inicial em estoque (opcional)"
              unit={unit}
              initialBase={0}
              onChange={setInitialQty}
              error={errors.initialQty}
            />
          )}
        </div>

        <div className="stock-modal__footer">
          <button className="stock-btn stock-btn--ghost" onClick={onClose} disabled={saving}>Cancelar</button>
          <button className="stock-btn stock-btn--primary" onClick={handleSave} disabled={saving}>
            {saving ? 'Salvando...' : 'Salvar'}
          </button>
        </div>
      </div>
    </div>
  );
};

/* ------------------------------------------------------------------ */
/* Modal: registrar compra / entrada                                   */
/* ------------------------------------------------------------------ */
const EntryModal = ({ base, ingredient, onClose, onSaved }) => {
  const [quantity, setQuantity] = useState(0);
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  const handleSave = async () => {
    if (!quantity || quantity <= 0) {
      setError('Informe uma quantidade maior que zero.');
      return;
    }
    setSaving(true);
    try {
      await api.post(`${base}/ingredients/${ingredient.id}/entries`, { quantity });
      onSaved();
    } catch (err) {
      setError(getErrorMessage(err, 'Não foi possível registrar a compra.'));
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="stock-overlay" onMouseDown={(e) => { if (e.target === e.currentTarget) onClose(); }}>
      <div className="stock-modal">
        <div className="stock-modal__header">
          <h2>Registrar compra</h2>
          <button className="stock-modal__close" onClick={onClose} aria-label="Fechar">×</button>
        </div>

        <div className="stock-modal__body">
          {error && <p className="stock-banner stock-banner--error">⚠️ {error}</p>}

          <p className="stock-hint">
            <strong>{ingredient.name}</strong> — saldo atual: {formatQty(ingredient.stockQuantity, ingredient.unit)}
          </p>

          <QuantityField
            label="Quantidade comprada"
            unit={ingredient.unit}
            initialBase={0}
            onChange={(v) => { setQuantity(v); setError(''); }}
          />

          <p className="stock-muted">
            Novo saldo: {formatQty(ingredient.stockQuantity + (quantity || 0), ingredient.unit)}
          </p>
        </div>

        <div className="stock-modal__footer">
          <button className="stock-btn stock-btn--ghost" onClick={onClose} disabled={saving}>Cancelar</button>
          <button className="stock-btn stock-btn--primary" onClick={handleSave} disabled={saving}>
            {saving ? 'Registrando...' : 'Registrar entrada'}
          </button>
        </div>
      </div>
    </div>
  );
};

/* ------------------------------------------------------------------ */
/* Aba                                                                 */
/* ------------------------------------------------------------------ */
const IngredientsTab = ({ restaurantId }) => {
  const base = `/v1/restaurants/${restaurantId}/stock`;
  const [ingredients, setIngredients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [modal, setModal] = useState(null); // { type: 'form' | 'entry', ingredient? }

  const load = useCallback(async () => {
    setLoading(true);
    try {
      const { data } = await api.get(`${base}/ingredients`);
      setIngredients(Array.isArray(data) ? data : []);
      setError('');
    } catch (err) {
      setError(getErrorMessage(err, 'Não foi possível carregar os ingredientes.'));
    } finally {
      setLoading(false);
    }
  }, [base]);

  useEffect(() => { load(); }, [load]);

  const handleDelete = async (ingredient) => {
    if (!window.confirm(`Excluir "${ingredient.name}"?`)) return;
    try {
      await api.delete(`${base}/ingredients/${ingredient.id}`);
      load();
    } catch (err) {
      setError(getErrorMessage(err, 'Não foi possível excluir. O ingrediente pode estar em uso em alguma receita.'));
    }
  };

  const closeAndReload = () => {
    setModal(null);
    load();
  };

  return (
    <div>
      <div className="stock-toolbar">
        <p className="stock-hint">
          Cadastre os ingredientes e registre as compras. Os pedidos descontam daqui automaticamente,
          conforme a receita de cada produto.
        </p>
        <button className="stock-btn stock-btn--primary" onClick={() => setModal({ type: 'form' })}>
          + Novo ingrediente
        </button>
      </div>

      {error && <p className="stock-banner stock-banner--error">⚠️ {error}</p>}

      {loading ? (
        <p className="stock-muted">Carregando...</p>
      ) : ingredients.length === 0 ? (
        <div className="stock-empty">Nenhum ingrediente cadastrado ainda.</div>
      ) : (
        <div className="stock-table-wrap">
          <table className="stock-table">
            <thead>
              <tr>
                <th>Ingrediente</th>
                <th>Saldo</th>
                <th>Mínimo</th>
                <th>Situação</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {ingredients.map((ing) => (
                <tr key={ing.id} className={ing.lowStock ? 'is-low' : ''}>
                  <td data-label="Ingrediente"><strong>{ing.name}</strong></td>
                  <td data-label="Saldo">{formatQty(ing.stockQuantity, ing.unit)}</td>
                  <td data-label="Mínimo">{formatQty(ing.lowStockThreshold, ing.unit)}</td>
                  <td data-label="Situação">
                    <span className={`stock-badge ${ing.lowStock ? 'stock-badge--low' : 'stock-badge--ok'}`}>
                      {ing.lowStock ? 'Estoque baixo' : 'OK'}
                    </span>
                  </td>
                  <td data-label="Ações">
                    <div className="stock-actions">
                      <button className="stock-btn stock-btn--small stock-btn--primary"
                        onClick={() => setModal({ type: 'entry', ingredient: ing })}>
                        Registrar compra
                      </button>
                      <button className="stock-btn stock-btn--small stock-btn--ghost"
                        onClick={() => setModal({ type: 'form', ingredient: ing })}>
                        Editar
                      </button>
                      <button className="stock-btn stock-btn--small stock-btn--danger"
                        onClick={() => handleDelete(ing)}>
                        Excluir
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {modal?.type === 'form' && (
        <IngredientFormModal
          base={base}
          ingredient={modal.ingredient}
          onClose={() => setModal(null)}
          onSaved={closeAndReload}
        />
      )}
      {modal?.type === 'entry' && (
        <EntryModal
          base={base}
          ingredient={modal.ingredient}
          onClose={() => setModal(null)}
          onSaved={closeAndReload}
        />
      )}
    </div>
  );
};

export default IngredientsTab;

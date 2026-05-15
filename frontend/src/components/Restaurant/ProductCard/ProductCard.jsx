import React, { useContext, useState } from 'react';
import semImagemPng from '../../../assets/sem-foto.png';
import addToCartIcon from '../../../assets/add_icon_white.png';
import menos from '../../../assets/menos.png';
import './ProductCard.css';
import { StoreContext } from '../../context/StoreContext';
import ConfirmTogglePopup from '../ConfirmTogglePopup/ConfirmTogglePopup.jsx';

const ProductCard = ({ product, canEdit, restaurantId, onToggle, onUpdate }) => {
  const imageUrl = `http://localhost/v1/restaurants/${product.restaurantId}/products/${product.id}/photo`;
  const { cartItems, addToCart, removeFromCart } = useContext(StoreContext);
  const quantity = cartItems[product.id] || 0;

  // Edit form state
  const [expanded, setExpanded] = useState(false);
  const [editForm, setEditForm] = useState({
    name: product.name,
    description: product.description || '',
    price: product.price,
  });
  const [saving, setSaving] = useState(false);
  const [saveError, setSaveError] = useState(null);

  // Toggle active/inactive state
  const [showConfirm, setShowConfirm] = useState(false);

  const handleEditChange = (e) => {
    setEditForm(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleEditSave = async () => {
    setSaving(true);
    setSaveError(null);
    try {
      const res = await fetch(
        `http://localhost/v1/restaurants/${restaurantId}/products/${product.id}`,
        {
          method: 'PUT',
          credentials: 'include',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            name: editForm.name,
            description: editForm.description,
            price: parseFloat(editForm.price),
          }),
        }
      );
      if (!res.ok) throw new Error('Erro ao salvar produto.');
      onUpdate?.({ ...product, ...editForm, price: parseFloat(editForm.price) });
      setExpanded(false);
    } catch (err) {
      setSaveError(err.message);
    } finally {
      setSaving(false);
    }
  };

  const handleToggleConfirm = async () => {
    const newActive = !product.active;
    const method = newActive ? 'PUT' : 'DELETE';

    try {
      const res = await fetch(
        `http://localhost/v1/restaurants/${restaurantId}/products/${product.id}/active`,
        { method, credentials: 'include' }
      );
      if (!res.ok) throw new Error('Erro ao alterar status.');
      onToggle?.(product.id, newActive);
    } catch (err) {
      console.error(err);
    } finally {
      setShowConfirm(false);
    }
  };

  const formattedPrice = new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
  }).format(product.price);

  return (
    <>
      <div className={`product-card ${expanded ? 'product-card--expanded' : ''} ${!product.active && canEdit ? 'product-card--inactive' : ''}`}>
        <img
          src={imageUrl}
          alt={`Foto de ${product.name}`}
          className="product-image"
          loading="lazy"
          onError={(e) => {
            e.target.onerror = null;
            e.target.src = semImagemPng;
          }}
        />

        <div className="product-info">
          <div className="product-info__top">
            <h3>{product.name}</h3>
            {canEdit && (
              <button
                className={`product-edit-btn ${expanded ? 'product-edit-btn--active' : ''}`}
                onClick={() => setExpanded(prev => !prev)}
                title="Editar produto"
              >
                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24"
                  fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" />
                  <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
                </svg>
              </button>
            )}
          </div>

          <p className="description">{product.description}</p>
          <span className="price">{formattedPrice}</span>

          {/* Inline edit form — dentro do product-info para não ser empurrado pelo flex-grow */}
          <div className={`product-edit-form ${expanded ? 'product-edit-form--open' : ''}`}>
            <div className="product-edit-form__inner">
              <div className="product-edit-form__field">
                <label>Nome</label>
                <input
                  name="name"
                  value={editForm.name}
                  onChange={handleEditChange}
                  placeholder="Nome do produto"
                />
              </div>
              <div className="product-edit-form__field">
                <label>Descrição</label>
                <textarea
                  name="description"
                  value={editForm.description}
                  onChange={handleEditChange}
                  placeholder="Descrição"
                  rows={2}
                />
              </div>
              <div className="product-edit-form__field">
                <label>Preço (R$)</label>
                <input
                  name="price"
                  type="number"
                  step="0.01"
                  min="0"
                  value={editForm.price}
                  onChange={handleEditChange}
                />
              </div>
              {saveError && <p className="product-edit-form__error">{saveError}</p>}
              <div className="product-edit-form__actions">
                <button
                  className="product-edit-form__btn product-edit-form__btn--cancel"
                  onClick={() => { setExpanded(false); setSaveError(null); }}
                  disabled={saving}
                >
                  Cancelar
                </button>
                <button
                  className="product-edit-form__btn product-edit-form__btn--save"
                  onClick={handleEditSave}
                  disabled={saving}
                >
                  {saving ? 'Salvando...' : 'Salvar'}
                </button>
              </div>
            </div>
          </div>
        </div>

        {/* Bottom row: toggle + cart */}
        <div className="product-add">
          {canEdit && (
            <button
              className={`product-toggle-btn ${product.active ? 'product-toggle-btn--active' : 'product-toggle-btn--inactive'}`}
              onClick={() => setShowConfirm(true)}
            >
              <span className={`product-toggle-dot ${product.active ? 'on' : 'off'}`} />
              {product.active ? 'Ativo' : 'Inativo'}
            </button>
          )}

          {!canEdit && (
            <>
              {quantity > 0 && (
                <>
                  <button className="btn-add-product" onClick={() => removeFromCart(product.id)}>
                    <img src={menos} alt="Remover" />
                  </button>
                  <span className="product-quantity">{quantity}</span>
                </>
              )}
              <button className="btn-add-product" onClick={() => addToCart(product.id)}>
                <img src={addToCartIcon} alt="Adicionar" />
              </button>
            </>
          )}
        </div>
      </div>

      {showConfirm && (
        <ConfirmTogglePopup
          currentActive={product.active}
          productName={product.name}
          onConfirm={handleToggleConfirm}
          onCancel={() => setShowConfirm(false)}
        />
      )}
    </>
  );
};

export default ProductCard;
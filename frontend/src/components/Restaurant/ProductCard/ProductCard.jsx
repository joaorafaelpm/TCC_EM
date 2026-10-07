import React, { useState, useContext } from 'react';
import semImagemPng from '../../../assets/sem-foto.png';
import addToCartIcon from '../../../assets/add_icon_white.png';
import menos from '../../../assets/menos.png';
import './ProductCard.css';
import { StoreContext } from '../../context/StoreContext';
import ConfirmTogglePopup from '../ConfirmTogglePopup/ConfirmTogglePopup.jsx';
import PhotoUploadTrigger from '../../PhotoHandler/PhotoUploadTrigger/PhotoUploadTrigger.jsx';
import { useFormValidation } from '../../../hooks/UserFormValidation';
import { notBlank, positiveOrZero } from '../../../utils/validator';
import api from '../../../services/api';
import Input from '../../Input/Input.jsx';

// Mesmo schema do AddProductModal — espelho do ProductDTO
const editSchema = {
  name:        [notBlank('Nome')],
  description: [notBlank('Descrição')],
  price:       [notBlank('Preço'), positiveOrZero('Preço')],
};

const ProductCard = ({ product, canEdit, restaurantId, onToggle, onUpdate }) => {
  const imageUrl = `/v1/restaurants/${product.restaurantId}/products/${product.id}/photo`;
  const { cartItems, addToCart, removeFromCart } = useContext(StoreContext);
  const quantity = cartItems[product.id] || 0;

  const [expanded, setExpanded]     = useState(false);
  const [editForm, setEditForm]     = useState({
    name:        product.name,
    description: product.description || '',
    price:       product.price,
  });
  const [saving, setSaving]         = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const [photoKey, setPhotoKey]     = useState(0);

  const { errors, validateAll, setBackendError, clearErrors } = useFormValidation(editSchema);

  const handleEditChange = (e) =>
    setEditForm(prev => ({ ...prev, [e.target.name]: e.target.value }));

  const handleEditSave = async () => {
    clearErrors();
    if (!validateAll(editForm)) return;

    setSaving(true);
    try {
      await api.put(
        `/v1/restaurants/${restaurantId}/products/${product.id}`,
        {
          name:        editForm.name,
          description: editForm.description,
          price:       parseFloat(editForm.price),
        }
      );
      onUpdate?.({ ...product, ...editForm, price: parseFloat(editForm.price) });
      setExpanded(false);
    } catch (err) {
      setBackendError(err);
    } finally {
      setSaving(false);
    }
  };

  const handleToggleConfirm = async () => {
    const newActive = !product.active;
    try {
      await api({ // api como instância axios — method dinâmico
        method:  newActive ? 'put' : 'delete',
        url:     `/v1/restaurants/${restaurantId}/products/${product.id}/active`,
      });
      onToggle?.(product.id, newActive);
    } catch (err) {
      console.error('Erro ao alterar status:', err);
    } finally {
      setShowConfirm(false);
    }
  };

  const formattedPrice = new Intl.NumberFormat('pt-BR', {
    style: 'currency', currency: 'BRL',
  }).format(product.price);

  return (
    <>
      <div className={`product-card ${expanded ? 'product-card--expanded' : ''} ${!product.active  && canEdit ? 'product-card--inactive' : ''}`}>

        {/* Foto */}
        {canEdit ? (
          <PhotoUploadTrigger
            uploadUrl={`/v1/restaurants/${restaurantId}/products/${product.id}/photo`}
            deleteUrl={`/v1/restaurants/${restaurantId}/products/${product.id}/photo`}
            triggerVariant="pencil-corner"
            cropShape="square"
            label="Alterar foto do produto"
            onSuccess={() => setPhotoKey(k => k + 1)}
          >
            <img
              key={photoKey}
              src={`${imageUrl}?v=${photoKey}`}
              alt={`Foto de ${product.name}`}
              className="product-image"
              loading="lazy"
              onError={(e) => { e.target.onerror = null; e.target.src = semImagemPng; }}
            />
          </PhotoUploadTrigger>
        ) : (
          <img
            src={imageUrl}
            alt={`Foto de ${product.name}`}
            className="product-image"
            loading="lazy"
            onError={(e) => { e.target.onerror = null; e.target.src = semImagemPng; }}
          />
        )}

        {/* Info */}
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

          {/* Formulário inline de edição */}
          <div className={`product-edit-form ${expanded ? 'product-edit-form--open' : ''}`}>
            <div className="product-edit-form__inner">

              {/* Erro geral do backend */}
              {errors.general && (
                <p className="product-edit-form__error">{errors.general}</p>
              )}

              <Input
                label="Nome"
                name="name"
                type="text"
                placeholder="Novo nome"
                value={editForm.name}
                onChange={handleEditChange}
                className={errors.name ? 'is-invalid' : ''}
                error={errors.name}
                maxLength={255}
              />

              <Input
                name="description"
                type="text"
                label="Descrição (Opcional)"
                placeholder="Nova descrição (Ainda opcional)"
                value={editForm.description}
                onChange={handleEditChange}
                maxLength={255}
                multiline
                rows={5}
              />

              {/* Preço — @NotNull @PositiveOrZero */}
              <div className="product-edit-form__field">
                <label>Preço (R$)</label>
                <input
                  name="price" type="number" step="0.01" min="0"
                  value={editForm.price} onChange={handleEditChange}
                  className={errors.price ? 'is-invalid' : ''}
                />
                {errors.price && <span className="prod-upd-field-error">{errors.price}</span>}
              </div>

              <div className="product-edit-form__actions">
                <button
                  className="product-edit-form__btn product-edit-form__btn--cancel"
                  onClick={() => { setExpanded(false); clearErrors(); }}
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

        {/* Controles de carrinho / toggle ativo */}
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
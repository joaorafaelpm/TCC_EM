import React, { useState, useRef } from 'react';
import './AddProductModal.css';
import { useFormValidation } from '../../../hooks/userFormValidation';
import { notBlank, positiveOrZero } from '../../../utils/validator';
import api from '../../../services/api';
import Input from '../../Input/Input';

const schema = {
  name:  [notBlank('Nome')],
  price: [notBlank('Preço'), positiveOrZero('Preço')],
};

const AddProductModal = ({ restaurantId, onClose, onProductAdded }) => {
  const [form, setForm] = useState({ name: '', description: '', price: '' });
  const [saving, setSaving] = useState(false);
  const mouseDownOnOverlay = useRef(false);

  const { errors, validateAll, setBackendError, clearErrors } = useFormValidation(schema);

  const handleChange = (e) =>
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));

  // Só fecha se o mousedown E o mouseup aconteceram no overlay em si —
  // evita fechar o modal quando o usuário seleciona texto (mousedown dentro,
  // arrasta, solta fora) num único gesto.
  const handleOverlayMouseDown = (e) => {
    mouseDownOnOverlay.current = e.target === e.currentTarget;
  };

  const handleOverlayMouseUp = (e) => {
    if (mouseDownOnOverlay.current && e.target === e.currentTarget) {
      onClose();
    }
    mouseDownOnOverlay.current = false;
  };

  const handleSave = async () => {
    clearErrors();
    if (!validateAll(form)) return;

    setSaving(true);
    try {
      const { data } = await api.post(
        `/v1/restaurants/${restaurantId}/products`,
        {
          name:        form.name,
          description: form.description || null,
          price:       parseFloat(form.price),
        }
      );
      onProductAdded?.(data);
      onClose();
    } catch (err) {
      setBackendError(err);
    } finally {
      setSaving(false);
    }
  };

  return (
    <div
      className="add-product-overlay"
      onMouseDown={handleOverlayMouseDown}
      onMouseUp={handleOverlayMouseUp}
    >
      <div className="add-product-modal" onClick={e => e.stopPropagation()}>

        <div className="add-product-modal__header">
          <h2>Novo produto</h2>
          <button className="add-product-modal__close" onClick={onClose}>
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24"
              fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <line x1="18" y1="6" x2="6" y2="18" /><line x1="6" y1="6" x2="18" y2="18" />
            </svg>
          </button>
        </div>

        <div className="add-product-modal__body">

          {errors.general && (
            <p className="add-product-modal__error">⚠️ {errors.general}</p>
          )}

          <Input
            name="name"
            label="Nome"
            type="text"
            maxLength={100}
            placeholder="Nome do produto"
            value={form.name}
            onChange={handleChange}
            error={errors.name}
          />

          <Input
            name="description"
            label="Descrição (opcional)"
            multiline
            rows={3}
            maxLength={500}
            placeholder="Descreva o produto..."
            value={form.description}
            onChange={handleChange}
          />

          <Input
            name="price"
            label="Preço (R$)"
            type="number"
            placeholder="0,00"
            value={form.price}
            onChange={handleChange}
            maxLength={6}
            error={errors.price}
          />

          <p className="add-product-modal__hint">
            📷 A foto do produto pode ser adicionada depois, na tela de edição.
          </p>

        </div>

        <div className="add-product-modal__footer">
          <button
            className="add-product-modal__btn add-product-modal__btn--cancel"
            onClick={onClose} disabled={saving}
          >
            Cancelar
          </button>
          <button
            className="add-product-modal__btn add-product-modal__btn--save"
            onClick={handleSave} disabled={saving}
          >
            {saving ? 'Criando...' : 'Criar produto'}
          </button>
        </div>

      </div>
    </div>
  );
};

export default AddProductModal;
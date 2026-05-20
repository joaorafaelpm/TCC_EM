import React, { useState } from 'react';
import './AddProductModal.css';

const AddProductModal = ({ restaurantId, onClose}) => {
  const [form, setForm] = useState({
    name: '',
    description: '',
    price: '',
  });
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSave = async () => {
    if (!form.name.trim() || !form.price) {
      setError('Nome e preço são obrigatórios.');
      return;
    }
    setSaving(true);
    setError(null);
    try {
      const res = await fetch(`http://localhost/v1/restaurants/${restaurantId}/products`, {
        method: 'POST',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          name: form.name,
          description: form.description,
          price: parseFloat(form.price),
        }),
      });

      if (!res.ok) throw new Error('Erro ao criar produto.');

      // const newProduct = await res.json();
      // onProductAdded(newProduct);
      onClose();
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="add-product-overlay" onClick={onClose}>
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
          {error && <p className="add-product-modal__error">⚠️ {error}</p>}

          <div className="add-product-modal__field">
            <label>Nome *</label>
            <input name="name" value={form.name} onChange={handleChange} placeholder="Nome do produto" />
          </div>

          <div className="add-product-modal__field">
            <label>Descrição</label>
            <textarea
              name="description"
              value={form.description}
              onChange={handleChange}
              placeholder="Descreva o produto..."
              rows={3}
            />
          </div>

          <div className="add-product-modal__field">
            <label>Preço (R$) *</label>
            <input
              name="price"
              type="number"
              step="0.01"
              min="0"
              value={form.price}
              onChange={handleChange}
              placeholder="0,00"
            />
          </div>
        </div>

        <div className="add-product-modal__footer">
          <button className="add-product-modal__btn add-product-modal__btn--cancel" onClick={onClose} disabled={saving}>
            Cancelar
          </button>
          <button className="add-product-modal__btn add-product-modal__btn--save" onClick={handleSave} disabled={saving}>
            {saving ? 'Criando...' : 'Criar produto'}
          </button>
        </div>
      </div>
    </div>
  );
};

export default AddProductModal;
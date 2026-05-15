import React, { useState } from 'react';
import './EditRestaurantModal.css';
import RestaurantAddressForm from '../RestaurantAddressForm/RestaurantAddressForm.jsx';

const EditRestaurantModal = ({ restaurant, onClose, onSave }) => {
  const [form, setForm] = useState({
    name: restaurant.name || '',
    description: restaurant.description || '',
    shippingFee: restaurant.shippingFee ?? '',
    averageDeliveryTimeMinutes: restaurant.averageDeliveryTimeMinutes ?? '',
    minimumOrderValue: restaurant.minimumOrderValue ?? '',
    address: {
      zipCode: restaurant.address?.zipCode || '',
      street: restaurant.address?.street || '',
      number: restaurant.address?.number || '',
      complement: restaurant.address?.complement || '',
      neighborhood: restaurant.address?.neighborhood || '',
      city: {
        id: restaurant.address?.city?.id || '',
        name: restaurant.address?.city?.name || '',
      },
    },
  });

  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleAddressChange = (updatedAddress) => {
    setForm(prev => ({ ...prev, address: updatedAddress }));
  };

  const handleSave = async () => {
    setSaving(true);
    setError(null);
    try {
      const res = await fetch(`http://localhost/v1/restaurants/${restaurant.id}`, {
        method: 'PUT',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          name: form.name,
          description: form.description,
          shippingFee: parseFloat(form.shippingFee),
          averageDeliveryTimeMinutes: parseInt(form.averageDeliveryTimeMinutes),
          minimumOrderValue: parseFloat(form.minimumOrderValue),
          address: {
            zipCode: form.address.zipCode,
            street: form.address.street,
            number: form.address.number,
            complement: form.address.complement,
            neighborhood: form.address.neighborhood,
            city: { id: form.address.city.id },
          },
        }),
      });

      if (!res.ok) throw new Error('Erro ao salvar restaurante.');
      onSave(form);
      onClose();
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="edit-restaurant-overlay" onClick={onClose}>
      <div className="edit-restaurant-modal" onClick={e => e.stopPropagation()}>
        <div className="edit-restaurant-modal__header">
          <h2>Editar restaurante</h2>
          <button className="edit-restaurant-modal__close" onClick={onClose}>
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24"
              fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <line x1="18" y1="6" x2="6" y2="18" /><line x1="6" y1="6" x2="18" y2="18" />
            </svg>
          </button>
        </div>

        <div className="edit-restaurant-modal__body">
          {error && <p className="edit-restaurant-modal__error">⚠️ {error}</p>}

          <fieldset className="edit-restaurant-modal__fieldset">
            <legend>Informações gerais</legend>

            <div className="edit-restaurant-modal__row">
              <div className="edit-restaurant-modal__field edit-restaurant-modal__field--full">
                <label>Nome</label>
                <input name="name" value={form.name} onChange={handleChange} />
              </div>
            </div>

            <div className="edit-restaurant-modal__row">
              <div className="edit-restaurant-modal__field edit-restaurant-modal__field--full">
                <label>Descrição</label>
                <textarea name="description" value={form.description} onChange={handleChange} rows={2} />
              </div>
            </div>

            <div className="edit-restaurant-modal__row">
              <div className="edit-restaurant-modal__field">
                <label>Frete (R$)</label>
                <input name="shippingFee" type="number" step="0.01" value={form.shippingFee} onChange={handleChange} />
              </div>
              <div className="edit-restaurant-modal__field">
                <label>Tempo médio (min)</label>
                <input name="averageDeliveryTimeMinutes" type="number" value={form.averageDeliveryTimeMinutes} onChange={handleChange} />
              </div>
              <div className="edit-restaurant-modal__field">
                <label>Pedido mínimo (R$)</label>
                <input name="minimumOrderValue" type="number" step="0.01" value={form.minimumOrderValue} onChange={handleChange} />
              </div>
            </div>
          </fieldset>

          <fieldset className="edit-restaurant-modal__fieldset">
            <legend>Endereço</legend>
            <RestaurantAddressForm
              address={form.address}
              onChange={handleAddressChange}
            />
          </fieldset>
        </div>

        <div className="edit-restaurant-modal__footer">
          <button className="edit-restaurant-modal__btn edit-restaurant-modal__btn--cancel" onClick={onClose} disabled={saving}>
            Cancelar
          </button>
          <button className="edit-restaurant-modal__btn edit-restaurant-modal__btn--save" onClick={handleSave} disabled={saving}>
            {saving ? 'Salvando...' : 'Salvar alterações'}
          </button>
        </div>
      </div>
    </div>
  );
};

export default EditRestaurantModal;
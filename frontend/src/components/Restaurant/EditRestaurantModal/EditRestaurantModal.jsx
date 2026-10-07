import React, { useState, useRef, useCallback } from 'react';
import './EditRestaurantModal.css';
import { useFormValidation } from '../../../hooks/userFormValidation';
import { notBlank, validationName, positiveOrZero, positive, optional } from '../../../utils/validator';
import api from '../../../services/api';
import DeliveryAddressForm from '../../DeliveryAddressForm/DeliveryAddressForm.jsx';
import Input from '../../Input/Input.jsx';

const schema = {
  name:                       [notBlank('Nome'), validationName()],
  shippingFee:                [notBlank('Frete'), positiveOrZero('Frete')],
  averageDeliveryTimeMinutes: [optional(positive('Tempo médio'))],
  minimumOrderValue:          [optional(positive('Pedido mínimo'))],
};

const EditRestaurantModal = ({ restaurant, onClose, onSave }) => {
  const addressRef = useRef(null);

  // Estado do endereço — atualizado pelo DeliveryAddressForm via onAddressUpdate
  const [address, setAddress] = useState({
    zipCode:      restaurant.address.zipCode,
    street:       restaurant.address.street,
    number:       restaurant.address.number,
    complement:   restaurant.address.complement || '',
    neighborhood: restaurant.address.neighborhood,
    cityId:       restaurant.address.city.id,
    cityName:     restaurant.address.city.name || '',
  });

  const [form, setForm] = useState({
    name:                       restaurant.name || '',
    description:                restaurant.description || '',
    shippingFee:                restaurant.shippingFee ?? '',
    averageDeliveryTimeMinutes: restaurant.averageDeliveryTimeMinutes ?? '',
    minimumOrderValue:          restaurant.minimumOrderValue ?? '',
  });

  const [saving, setSaving] = useState(false);

  const { errors, validateAll, setBackendError, clearErrors } = useFormValidation(schema);

  const handleChange = (e) =>
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));

  // ✓ corrigido — setAddress(newAddress), sem aninhar dentro de prev.address
  const handleAddressUpdate = useCallback((newAddress) => {
    setAddress(newAddress);
  }, []);

  const handleSave = async () => {
    clearErrors();

    // Valida campos do restaurante e endereço (DeliveryAddressForm via ref)
    const fieldsValid  = validateAll(form);
    const addressValid = addressRef.current?.validate();

    if (!fieldsValid || !addressValid) return;

    setSaving(true);
    try {
      await api.put(`/v1/restaurants/${restaurant.id}`, {
        name:                       form.name,
        description:                form.description || null,
        shippingFee:                parseFloat(form.shippingFee),
        averageDeliveryTimeMinutes: form.averageDeliveryTimeMinutes
          ? parseInt(form.averageDeliveryTimeMinutes) : null,
        minimumOrderValue: form.minimumOrderValue
          ? parseFloat(form.minimumOrderValue) : null,
        // ✓ usa o estado `address` atualizado pelo DeliveryAddressForm
        address: {
          zipCode:      address.zipCode,
          street:       address.street,
          number:       address.number,
          complement:   address.complement || null,
          neighborhood: address.neighborhood,
          city:         { id: address.cityId },
        },
      });

      onSave(form);
      onClose();
    } catch (err) {
      setBackendError(err);
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="edit-restaurant-overlay" onMouseDown={(e) => { if (e.target === e.currentTarget) onClose(); }}>
      <div className="edit-restaurant-modal">

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

          {errors.general && (
            <p className="edit-restaurant-modal__error">⚠️ {errors.general}</p>
          )}

          <fieldset className="edit-restaurant-modal__fieldset">
            <legend>Informações gerais</legend>

            <Input
              label="Nome"
              name="name"
              type="text"
              placeholder="Novo nome"
              value={form.name}
              onChange={handleChange}
              className={errors.name ? 'is-invalid' : ''}
              error={errors.name}
              maxLength={100}
            />
            <Input
              name="description"
              type="text"
              label="Descrição (Opcional)"
              placeholder="Nova descrição (Ainda opcional)"
              value={form.description}
              onChange={handleChange}
              maxLength={255}
              multiline
              rows={5}
            />

            <Input
              name="shippingFee"
              label="Taxa de Entrega"
              type="number"
              placeholder="Taxa de Entrega"
              value={form.shippingFee}
              onChange={handleChange}
              maxLength={4}
              error={errors.shippingFee}
            />

            <Input
              label="Tempo médio (min)"
              name="averageDeliveryTimeMinutes"
              type="number"
              step="1"
              min="0"
              value={form.averageDeliveryTimeMinutes}
              onChange={handleChange}
              className={errors.averageDeliveryTimeMinutes ? 'is-invalid' : ''}
              error={errors.averageDeliveryTimeMinutes}
            />
            <Input
              label="Pedido mínimo (R$)"
              name="minimumOrderValue"
              type="number"
              step="0.01"
              min="0"
              value={form.minimumOrderValue}
              onChange={handleChange}
              className={errors.minimumOrderValue ? 'is-invalid' : ''}
              error={errors.minimumOrderValue}
            />
          </fieldset>

          <fieldset className="edit-restaurant-modal__fieldset">
            <legend>Endereço</legend>
            <DeliveryAddressForm
              ref={addressRef}
              onAddressUpdate={handleAddressUpdate}
              initialAddress={address}
            />
          </fieldset>
        </div>

        <div className="edit-restaurant-modal__footer">
          <button
            className="edit-restaurant-modal__btn edit-restaurant-modal__btn--cancel"
            onClick={onClose} disabled={saving}
          >
            Cancelar
          </button>
          <button
            className="edit-restaurant-modal__btn edit-restaurant-modal__btn--save"
            onClick={handleSave} disabled={saving}
          >
            {saving ? 'Salvando...' : 'Salvar alterações'}
          </button>
        </div>

      </div>
    </div>
  );
};

export default EditRestaurantModal;
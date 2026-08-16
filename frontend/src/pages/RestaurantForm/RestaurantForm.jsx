import React, { useState, useCallback, useRef } from 'react';
import './RestaurantForm.css';
import DeliveryAddressForm from '../../components/DeliveryAddressForm/DeliveryAddressForm';
import { useAuth } from '../../components/context/AuthProvider';
import { useNavigate } from 'react-router-dom';
import { useFormValidation } from '../../hooks/UserFormValidation';
import { notBlank, validationName, validCpf, positiveOrZero, positive, optional } from "../../utils/validator";
import api from '../../services/api';
import Input from '../../components/Input/Input';
import { formatCpf } from '../../utils/formatter';

// Schema espelho do RestaurantDTO
// Campos opcionais (description, averageDeliveryTimeMinutes, minimumOrderValue)
// não entram no schema — sem anotação no DTO = sem validação obrigatória
const schema = {
  name:        [notBlank('Nome'), validationName()],
  ownerCpf:    [notBlank('CPF'), validCpf],
  shippingFee: [notBlank('Taxa de entrega'), positiveOrZero('Taxa de entrega')],
  averageDeliveryTimeMinutes: [optional(positive('Tempo médio de entrega'))],
  minimumOrderValue:          [optional(positive('Valor mínimo do pedido'))],
};

const RestaurantForm = () => {
  const { refreshUser } = useAuth();
  const navigate = useNavigate();
  const addressRef = useRef(null); // ref para acessar validate() do filho

  const [restaurantInfo, setRestaurantInfo] = useState({
    name: '', shippingFee: '', ownerCpf: '',
    description: '', averageDeliveryTimeMinutes: '', minimumOrderValue: ''
  });
  const [address, setAddress] = useState({});
  const [isLoading, setIsLoading] = useState(false);

  const { errors, validateAll, setBackendError, clearErrors } = useFormValidation(schema);

  const handleAddressUpdate = useCallback((newAddress) => {
    setAddress(newAddress);
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setRestaurantInfo(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    clearErrors();

    // Valida os campos do restaurante e os campos do endereço (filho)
    const restaurantValid = validateAll(restaurantInfo);
    const addressValid    = addressRef.current?.validate();

    // Se qualquer um falhou, para aqui — não bate no backend
    if (!restaurantValid || !addressValid) return;

    const payload = {
      name:        restaurantInfo.name,
      ownerCpf:    restaurantInfo.ownerCpf,
      shippingFee: Number.parseInt(restaurantInfo.shippingFee),
      description: restaurantInfo.description || null,
      averageDeliveryTimeMinutes: restaurantInfo.averageDeliveryTimeMinutes
        ? Number.parseInt(restaurantInfo.averageDeliveryTimeMinutes) : null,
      minimumOrderValue: restaurantInfo.minimumOrderValue
        ? Number.parseInt(restaurantInfo.minimumOrderValue) : null,
      address: {
        zipCode:      address.zipCode,
        street:       address.street,
        number:       address.number,
        complement:   address.complement || null,
        neighborhood: address.neighborhood,
        city: { id:  address.cityId }
      }
    };

    setIsLoading(true);
    try {
      const { data } = await api.post('/v1/restaurants', payload);
      await refreshUser();
      navigate(`/restaurant/${data.id}`); // react-router em vez de globalThis.location
    } catch (err) {
      setBackendError(err); // exibe a mensagem que o seu backend retornou
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <form className='restaurant-info' onSubmit={handleSubmit}>
      <div className="restaurant-info-left">
        <p className='title'>Informações do Restaurante</p>
        <Input
          name="name"
          maxLength={100}
          label="Nome do Restaurante"
          type="text"
          placeholder="Nome da Pizzaria"
          value={restaurantInfo.name}
          onChange={handleChange}
          error={errors.name}
        />

        <div className='multi-fields'>
            <Input
              name="ownerCpf"
              label="CPF do Proprietário"
              type="text" 
              placeholder="CPF do Proprietário"
              value={restaurantInfo.ownerCpf}
              maxLength={14}
              format={formatCpf}
              onChange={handleChange}
              error={errors.ownerCpf}
            />
            <Input
              name="shippingFee"
              label="Taxa de Entrega"
              type="number"
              placeholder="Taxa de Entrega"
              value={restaurantInfo.shippingFee}
              onChange={handleChange}
              maxLength={4}
              error={errors.shippingFee}
            />
            
        </div>
            <Input
              name="description"
              type="text"
              label="Descrição (Opcional)"
              placeholder="Descrição (Opcional)"
              value={restaurantInfo.description}
              onChange={handleChange}
              maxLength={255}
              multiline
              rows={5}
            />
        <div style={{ marginTop: '40px' }}>

          <DeliveryAddressForm ref={addressRef} onAddressUpdate={handleAddressUpdate} />
        </div>
      </div>

      <div className="restaurant-info-right">
        <p className='title'>Detalhes Operacionais</p>

        <div className='multi-fields'>
            <Input
              name="averageDeliveryTimeMinutes"
              type="number"
              label="Tempo de Entrega (min) - Opcional"
              min="0"
              placeholder="Tempo de Entrega"
              value={restaurantInfo.averageDeliveryTimeMinutes}
              onChange={handleChange}
              error={errors.averageDeliveryTimeMinutes}
            />
            <Input
              name="minimumOrderValue"
              type="number"
              step="0.01"
              min="0"
              placeholder="Pedido Mínimo"
              label="Pedido Mínimo - Opcional"
              value={restaurantInfo.minimumOrderValue}
              onChange={handleChange}
              error={errors.minimumOrderValue}
            />
        </div>

        <div className="resume-section">
          <h2>Resumo do Cadastro</h2>
          <p>Ao finalizar, seu restaurante passará por uma análise para ser listado na plataforma.</p>
          <button type="submit" className='btn-cart-checkout' disabled={isLoading}>
            {isLoading ? 'Cadastrando...' : 'Cadastrar Restaurante'}
          </button>
        </div>
      </div>
    </form>
  );
};

export default RestaurantForm;
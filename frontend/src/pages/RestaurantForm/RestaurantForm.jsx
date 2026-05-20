import React, { useState, useCallback } from 'react';
import './RestaurantForm.css'; // Usando o mesmo CSS para manter o estilo
import DeliveryAddressForm from '../../components/DeliveryAddressForm/DeliveryAddressForm';
import { useAuth } from '../../components/context/AuthProvider';
import { Navigate } from 'react-router-dom';

const RestaurantForm = () => {
  const { refreshUser } = useAuth();
  const [restaurantInfo, setRestaurantInfo] = useState({
    name: '',
    shippingFee: '',
    ownerCpf: '',
    description: '',
    averageDeliveryTimeMinutes: '',
    minimumOrderValue: ''
  });

  const [address, setAddress] = useState({});

  // Recebe o objeto de endereço do componente filho
  const handleAddressUpdate = useCallback((newAddress) => {
    setAddress(newAddress);
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setRestaurantInfo(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
  e.preventDefault();

  const payload = {
    name: restaurantInfo.name,
    shippingFee: parseFloat(restaurantInfo.shippingFee),
    ownerCpf: restaurantInfo.ownerCpf,
    description: restaurantInfo.description || null,
    averageDeliveryTimeMinutes: restaurantInfo.averageDeliveryTimeMinutes ? parseInt(restaurantInfo.averageDeliveryTimeMinutes) : null,
    minimumOrderValue: restaurantInfo.minimumOrderValue ? parseFloat(restaurantInfo.minimumOrderValue) : null,
    address: {
      zipCode: address.zipCode,
      street: address.street,
      number: address.number,
      complement: address.complement || null,
      neighborhood: address.neighborhood,
      city: {
        id: address.cityId
      }
    }
  };

  try {
    const response = await fetch('/v1/restaurants', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });

    if (response.ok) {
      await refreshUser()
      globalThis.location.href = `/restaurantes/${(await response.json()).id}`
    }

  } catch (err) {
    console.error("Erro na requisição:", err);
  }
  };

  return (
    <form className='place-order' onSubmit={handleSubmit}>
      <div className="place-order-left">
        <p className='title'>Informações do Restaurante</p>
        
        <input 
          type="text" name="name" placeholder="Nome da Pizzaria" 
          value={restaurantInfo.name} onChange={handleChange} required 
        />
        
        <div className='multi-fields'>
          <input 
            type="text" name="ownerCpf" placeholder="CPF do Proprietário" 
            value={restaurantInfo.ownerCpf} onChange={handleChange} required 
          />
          <input 
            type="number" step="0.01" name="shippingFee" placeholder="Taxa de Entrega" 
            value={restaurantInfo.shippingFee} onChange={handleChange} required 
          />
        </div>

        <textarea 
          name="description" 
          placeholder="Descrição (Opcional)" 
          value={restaurantInfo.description} 
          onChange={handleChange}
          className="custom-textarea" // Opcional: para ajustar estilo
        />

        {/* Componente inteligente de endereço */}
        <div style={{ marginTop: '40px' }}>
          <DeliveryAddressForm onAddressUpdate={handleAddressUpdate} />
        </div>
      </div>

      <div className="place-order-right">
        <p className='title'>Detalhes Operacionais</p>
        
        <div className='multi-fields'>
          <input 
            type="number" name="averageDeliveryTimeMinutes" placeholder="Tempo de Entrega (min) - Opcional" 
            value={restaurantInfo.averageDeliveryTimeMinutes} onChange={handleChange} 
          />
          <input 
            type="number" step="0.01" name="minimumOrderValue" placeholder="Pedido Mínimo - Opcional" 
            value={restaurantInfo.minimumOrderValue} onChange={handleChange} 
          />
        </div>

        <div className="cart-total" style={{marginTop: '20px'}}>
            <h2>Resumo do Cadastro</h2>
            <p>Ao finalizar, seu restaurante passará por uma análise para ser listado na plataforma.</p>
            <button type="submit" className='btn-cart-checkout'>Cadastrar Restaurante</button>
        </div>
      </div>
    </form>
  );
};

export default RestaurantForm;
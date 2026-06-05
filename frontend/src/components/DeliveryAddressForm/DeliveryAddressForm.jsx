import React, { useState, useEffect, useImperativeHandle, forwardRef } from 'react';
import { useFormValidation } from '../../hooks/UserFormValidation';
import { notBlank, notNull } from '../../utils/validator';
import Input from '../Input/Input';

// Schema espelho do AddressDTO
const addressSchema = {
  zipCode:      [notBlank('CEP')],
  street:       [notBlank('Rua')],
  number:       [notBlank('Número')],
  neighborhood: [notBlank('Bairro')],
  cityId:       [(v) => !v ? 'Cidade é obrigatória' : null],
};

// forwardRef — permite o pai chamar addressRef.current.validate()
const DeliveryAddressForm = forwardRef(({ onAddressUpdate }, ref) => {
  const [address, setAddress] = useState({
    zipCode: '', street: '', number: '',
    complement: '', neighborhood: '', cityId: '', cityName: ''
  });
  const [cities, setCities] = useState([]);

  const { errors, validateAll, clearErrors } = useFormValidation(addressSchema);

  const ufToStateName = {
    AC: 'Acre', AL: 'Alagoas', AP: 'Amapa', AM: 'Amazonas', BA: 'Bahia',
    CE: 'Ceara', DF: 'Distrito Federal', ES: 'Espirito Santo', GO: 'Goias',
    MA: 'Maranhao', MT: 'Mato Grosso', MS: 'Mato Grosso do Sul',
    MG: 'Minas Gerais', PA: 'Para', PB: 'Paraiba', PR: 'Parana',
    PE: 'Pernambuco', PI: 'Piaui', RJ: 'Rio de Janeiro',
    RN: 'Rio Grande do Norte', RS: 'Rio Grande do Sul', RO: 'Rondonia',
    RR: 'Roraima', SC: 'Santa Catarina', SP: 'Sao Paulo',
    SE: 'Sergipe', TO: 'Tocantins'
  };

  // Expõe validate() para o componente pai usar via ref
  useImperativeHandle(ref, () => ({
    validate: () => validateAll(address),
  }));

  useEffect(() => {
    fetch('/v1/cities')
      .then(r => r.json())
      .then(data => setCities(data['content'] || []))
      .catch(console.error);
  }, []);

  useEffect(() => {
    onAddressUpdate(address);
  }, [address, onAddressUpdate]);

  const handleAddressChange = (e) => {
    const { name, value } = e.target;
    setAddress(prev => ({ ...prev, [name]: value }));
  };

  const handleCepBlur = async (e) => {
    const cep = e.target.value.replace(/\D/g, '');
    if (cep.length !== 8) return;
    try {
      const res = await fetch(`https://viacep.com.br/ws/${cep}/json/`);
      const data = await res.json();
      if (!data.erro) {
        const stateName = ufToStateName[data.uf];
        const cityRes = await fetch(`/v1/cities/${data.localidade}/state/${stateName}`);
        const cityData = await cityRes.json();
        setAddress(prev => ({
          ...prev,
          street: data.logradouro,
          neighborhood: data.bairro,
          cityName: data.localidade,
          cityId: cityData.id
        }));
      } else {
        alert('CEP não encontrado.');
      }
    } catch (e) {
      console.error(e);
    }
  };

  const handleCitySelect = (e) => {
    const val = e.target.value;
    const found = cities.find(c => c.name === val);
    setAddress(prev => ({
      ...prev,
      cityName: val,
      cityId: found ? found.id : ''
    }));
  };

  return (
    <div>
      <p className='title'>Endereço</p>

      <div className='multi-fields'>
        <Input
          name="zipCode"
          type="text"
          placeholder="CEP"
          value={address.zipCode}
          onChange={handleAddressChange}
          onBlur={handleCepBlur}
          maxLength={8}
          error={errors.zipCode}
        />
        <Input
        name="neighborhood"
        type="text"
        placeholder="Bairro"
        value={address.neighborhood}
        onChange={handleAddressChange}
        onBlur={handleCepBlur}
        autoComplete ="postal-code"
        error={errors.neighborhood}
      />
      </div>
        <Input
          name="street"
          type="text"
          placeholder="Rua"
          value={address.street}
          onChange={handleAddressChange}
          autoComplete="address-line1"
          error={errors.street}
        />

      <div className='multi-fields'>
        <Input
          name="number" 
          type="text"  
          placeholder="Número"
          value={address.number} 
          onChange={handleAddressChange}
          autoComplete="address-line2"
          className={errors.number ? 'is-invalid' : ''}
          error={errors.number}
        />
        <Input
          name="complement"
          type="text"
          placeholder="Complemento"
          value={address.complement}
          onChange={handleAddressChange}
        />
      </div>

        <Input  
          name="cityName"
          placeholder="Cidade"
          list="city-options"  
          onChange={handleCitySelect}
          value={address.cityName} 
          autoComplete="address-level2"
          className={errors.cityId ? 'is-invalid' : ''}
          error={errors.cityId}
        />
        <datalist id="city-options">
          {cities.map(city => <option key={city.id} value={city.name} />)}
        </datalist>
    </div>
  );
});

DeliveryAddressForm.displayName = 'DeliveryAddressForm';
export default DeliveryAddressForm;
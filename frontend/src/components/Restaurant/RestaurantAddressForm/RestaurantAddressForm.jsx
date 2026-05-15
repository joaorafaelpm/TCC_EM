import React, { useState, useEffect } from 'react';
import './RestaurantAddressForm.css';

/**
 * Reutiliza a lógica do DeliveryAddressForm (ViaCEP + autocomplete de cidades),
 * mas com os estilos do padrão de modal do restaurante.
 *
 * Props:
 *   address  — objeto { zipCode, street, number, complement, neighborhood, city: { id, name } }
 *   onChange — callback(updatedAddress) chamado a cada alteração
 */
const RestaurantAddressForm = ({ address, onChange }) => {
  const [cities, setCities] = useState([]);
  const [cepLoading, setCepLoading] = useState(false);
  const [cepError, setCepError] = useState(null);

  const ufToStateName = {
    AC: 'Acre', AL: 'Alagoas', AP: 'Amapa', AM: 'Amazonas', BA: 'Bahia',
    CE: 'Ceara', DF: 'Distrito Federal', ES: 'Espirito Santo', GO: 'Goias',
    MA: 'Maranhao', MT: 'Mato Grosso', MS: 'Mato Grosso do Sul', MG: 'Minas Gerais',
    PA: 'Para', PB: 'Paraiba', PR: 'Parana', PE: 'Pernambuco', PI: 'Piaui',
    RJ: 'Rio de Janeiro', RN: 'Rio Grande do Norte', RS: 'Rio Grande do Sul',
    RO: 'Rondonia', RR: 'Roraima', SC: 'Santa Catarina', SP: 'Sao Paulo',
    SE: 'Sergipe', TO: 'Tocantins',
  };

  useEffect(() => {
    fetch('/v1/cities', { credentials: 'include' })
      .then(r => r.json())
      .then(data => setCities(data.content || []))
      .catch(console.error);
  }, []);

  const update = (patch) => onChange({ ...address, ...patch });

  const handleFieldChange = (e) => {
    update({ [e.target.name]: e.target.value });
  };

  const handleCepBlur = async (e) => {
    const cep = e.target.value.replace(/\D/g, '');
    if (cep.length !== 8) return;

    setCepLoading(true);
    setCepError(null);

    try {
      const res = await fetch(`https://viacep.com.br/ws/${cep}/json/`);
      const data = await res.json();

      if (data.erro) {
        setCepError('CEP não encontrado.');
        return;
      }

      const stateName = ufToStateName[data.uf];
      const cityRes = await fetch(`/v1/cities/${data.localidade}/state/${stateName}`, {
        credentials: 'include',
      });
      const cityData = await cityRes.json();

      onChange({
        ...address,
        street: data.logradouro || address.street,
        neighborhood: data.bairro || address.neighborhood,
        city: {
          id: cityData.id || '',
          name: data.localidade || '',
        },
      });
    } catch {
      setCepError('Erro ao buscar CEP.');
    } finally {
      setCepLoading(false);
    }
  };

  const handleCitySelect = (e) => {
    const val = e.target.value;
    const found = cities.find(c => c.name === val);
    onChange({
      ...address,
      city: {
        id: found ? found.id : address.city?.id || '',
        name: val,
      },
    });
  };

  const f = (name, label, props = {}) => (
    <div className="raf__field">
      <label className="raf__label">{label}</label>
      <input
        className="raf__input"
        name={name}
        value={address[name] ?? ''}
        onChange={handleFieldChange}
        {...props}
      />
    </div>
  );

  return (
    <div className="raf">
      <div className="raf__row">
        <div className="raf__field">
          <label className="raf__label">
            CEP {cepLoading && <span className="raf__cep-hint">Buscando...</span>}
          </label>
          <input
            className={`raf__input ${cepError ? 'raf__input--error' : ''}`}
            name="zipCode"
            value={address.zipCode ?? ''}
            onChange={handleFieldChange}
            onBlur={handleCepBlur}
            maxLength={9}
            placeholder="00000-000"
            autoComplete="postal-code"
          />
          {cepError && <span className="raf__error-msg">{cepError}</span>}
        </div>

        {f('number', 'Número', { autoComplete: 'address-line2' })}
      </div>

      <div className="raf__row">
        <div className="raf__field raf__field--full">
          {f('street', 'Rua', { autoComplete: 'address-line1' })}
        </div>
      </div>

      <div className="raf__row">
        {f('neighborhood', 'Bairro', { autoComplete: 'address-level3' })}
        {f('complement', 'Complemento')}
      </div>

      <div className="raf__row">
        <div className="raf__field raf__field--full">
          <label className="raf__label">Cidade</label>
          <input
            className="raf__input"
            list="raf-city-options"
            name="cityName"
            value={address.city?.name ?? ''}
            onChange={handleCitySelect}
            autoComplete="address-level2"
            placeholder="Digite ou selecione a cidade"
          />
          <datalist id="raf-city-options">
            {cities.map(city => (
              <option key={city.id} value={city.name} />
            ))}
          </datalist>
        </div>
      </div>
    </div>
  );
};

export default RestaurantAddressForm;
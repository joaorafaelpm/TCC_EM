const DeliveryAddressForm = ({ address, cities, onAddressChange, onCepBlur, onCitySelect }) => (
  <div>
    <p className='title'>Endereço de Entrega</p>
    <div className='multi-fields'>
      <input type="text" name="zipCode" placeholder="CEP" value={address.zipCode}
        onChange={onAddressChange} onBlur={onCepBlur} maxLength={8} required />
      <input type="text" name="neighborhood" placeholder="Bairro" value={address.neighborhood}
        onChange={onAddressChange} required />
    </div>
    <div className='multi-fields'>
      <input type="text" name="street" placeholder="Rua" value={address.street}
        onChange={onAddressChange} required />
    </div>
    <div className='multi-fields'>
      <input type="text" name="number" placeholder="Número" value={address.number}
        onChange={onAddressChange} required />
      <input type="text" name="complement" placeholder="Complemento" value={address.complement}
        onChange={onAddressChange} />
    </div>
    <div className='multi-fields'>
      <input list="city-options" name="cityName" placeholder="Cidade" value={address.cityName}
        onChange={onCitySelect} required />
      <datalist id="city-options">
        {cities.map(city => <option key={city.id} value={city.name} />)}
      </datalist>
    </div>
  </div>
);

export default DeliveryAddressForm;
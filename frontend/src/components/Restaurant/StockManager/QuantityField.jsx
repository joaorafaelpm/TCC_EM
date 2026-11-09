import React, { useState } from 'react';
import { UNITS, splitBase, toBase } from './stockUtils';

/**
 * Campo de quantidade com conversão para a unidade base.
 *
 * onChange(valorBase):
 *   - número inteiro em g/ml/un;
 *   - 0 se o campo estiver vazio;
 *   - null se o valor digitado for inválido.
 *
 * Para reiniciar o campo (ex.: trocou a unidade ou o ingrediente), use a prop `key` no pai.
 */
const QuantityField = ({ label, unit, initialBase = 0, onChange, error, disabled = false }) => {
  const u = UNITS[unit] || UNITS.UN;
  const initial = splitBase(initialBase, unit);
  const [amount, setAmount] = useState(initial.amount);
  const [factor, setFactor] = useState(initial.factor);

  const emit = (nextAmount, nextFactor) => {
    onChange(String(nextAmount).trim() === '' ? 0 : toBase(nextAmount, nextFactor));
  };

  return (
    <div className="stock-field">
      {label && <label>{label}</label>}
      <div className="stock-qty">
        <input
          type="number"
          min="0"
          step="any"
          inputMode="decimal"
          placeholder="0"
          value={amount}
          disabled={disabled}
          onChange={(e) => {
            setAmount(e.target.value);
            emit(e.target.value, factor);
          }}
        />
        {u.big ? (
          <select
            value={factor}
            disabled={disabled}
            onChange={(e) => {
              const next = Number(e.target.value);
              setFactor(next);
              emit(amount, next);
            }}
          >
            <option value={1}>{u.label}</option>
            <option value={u.big.factor}>{u.big.label}</option>
          </select>
        ) : (
          <span className="stock-qty__unit">{u.label}</span>
        )}
      </div>
      {error && <span className="stock-field__error">{error}</span>}
    </div>
  );
};

export default QuantityField;

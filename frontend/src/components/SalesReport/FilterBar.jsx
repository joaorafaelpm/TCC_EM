import React from 'react';
import { today } from '../utils/formatters';

const FilterBar = ({
  restaurants,
  selectedId,
  onSelectId,
  startDate,
  endDate,
  onStartDate,
  onEndDate,
  onApply,
  loading,
}) => (
  <div className="sr-filters">
    <div className="sr-filters__group">
      <label className="sr-filters__label">Restaurante</label>
      <select
        className="sr-filters__select"
        value={selectedId}
        onChange={(e) => onSelectId(e.target.value)}
      >
        {restaurants.map((r) => (
          <option key={r.id} value={r.id}>{r.name}</option>
        ))}
      </select>
    </div>

    <div className="sr-filters__group">
      <label className="sr-filters__label">De</label>
      <input
        type="date"
        className="sr-filters__input"
        value={startDate}
        max={endDate}
        onChange={(e) => onStartDate(e.target.value)}
      />
    </div>

    <div className="sr-filters__group">
      <label className="sr-filters__label">Até</label>
      <input
        type="date"
        className="sr-filters__input"
        value={endDate}
        min={startDate}
        max={today()}
        onChange={(e) => onEndDate(e.target.value)}
      />
    </div>

    <button className="sr-btn sr-btn--apply" onClick={onApply} disabled={loading}>
      {loading ? 'Buscando…' : 'Aplicar'}
    </button>
  </div>
);

export default FilterBar;
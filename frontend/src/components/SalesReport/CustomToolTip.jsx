import React from 'react';
import { fmt } from '../utils/formatters';

const CustomTooltip = ({ active, payload, label }) => {
  if (!active || !payload?.length) return null;
  return (
    <div className="sr-tooltip">
      <p className="sr-tooltip__label">{label}</p>
      {payload.map((p) => (
        <p key={p.dataKey} className="sr-tooltip__item">
          <span className="sr-tooltip__dot" style={{ background: p.color }} />
          {p.name}:{' '}
          <strong>
            {p.dataKey === 'Quantidade' ? `${p.value} un.` : fmt(p.value)}
          </strong>
        </p>
      ))}
    </div>
  );
};

export default CustomTooltip;
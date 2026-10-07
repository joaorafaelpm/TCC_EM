import React from 'react';

const HighlightCard = ({ icon, label, name, sub, mono = false }) => (
  <div className="sr-highlight-card">
    <span className="sr-highlight-card__icon" aria-hidden="true">{icon}</span>
    <div>
      <p className="sr-highlight-card__label">{label}</p>
      <p className={`sr-highlight-card__name${mono ? ' sr-highlight-card__name--mono' : ''}`}>
        {name}
      </p>
      <p className="sr-highlight-card__sub">{sub}</p>
    </div>
  </div>
);

export default HighlightCard;
import React from 'react';

const SectionCard = ({ title, children }) => (
  <div className="sr-card">
    <p className="sr-card__title">{title}</p>
    {children}
  </div>
);

export default SectionCard;
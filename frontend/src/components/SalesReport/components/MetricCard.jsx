import React from 'react';

const MetricCard = ({ label, value }) => (
  <div className="sr-metric-card">
    <span className="sr-metric-card__label">{label}</span>
    <span className="sr-metric-card__value">{value}</span>
  </div>
);

export default MetricCard;
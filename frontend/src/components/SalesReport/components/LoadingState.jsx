import React from 'react';

const LoadingState = ({ message }) => (
  <div className="sr-loading">
    <div className="sr-loading__spinner" />
    <p>{message}</p>
  </div>
);

export default LoadingState;
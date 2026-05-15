import React, { useEffect, useState } from 'react';
import './ConfirmTogglePopup.css';

const ConfirmTogglePopup = ({ currentActive, productName, onConfirm, onCancel }) => {
  const [cooldown, setCooldown] = useState(2);
  const [ready, setReady] = useState(false);

  useEffect(() => {
    if (cooldown <= 0) {
      setReady(true);
      return;
    }
    const timer = setTimeout(() => setCooldown(c => c - 1), 1000);
    return () => clearTimeout(timer);
  }, [cooldown]);

  const action = currentActive ? 'desativar' : 'ativar';
  const actionLabel = currentActive ? 'Desativar' : 'Ativar';
  const actionClass = currentActive ? 'confirm-popup__btn--danger' : 'confirm-popup__btn--success';

  return (
    <div className="confirm-popup-overlay" onClick={onCancel}>
      <div className="confirm-popup" onClick={e => e.stopPropagation()}>
        <div className="confirm-popup__icon">
          {currentActive ? '⚠️' : '✅'}
        </div>

        <h3 className="confirm-popup__title">
          {currentActive ? 'Desativar produto?' : 'Ativar produto?'}
        </h3>

        <p className="confirm-popup__message">
          Você está prestes a <strong>{action}</strong>{' '}
          <strong>"{productName}"</strong>.{' '}
          {currentActive
            ? 'Ele deixará de aparecer para os clientes.'
            : 'Ele voltará a aparecer para os clientes.'}
        </p>

        <div className="confirm-popup__progress">
          <div
            className="confirm-popup__progress-bar"
            style={{ animationDuration: '2s' }}
          />
        </div>

        <div className="confirm-popup__actions">
          <button className="confirm-popup__btn confirm-popup__btn--cancel" onClick={onCancel}>
            Cancelar
          </button>
          <button
            className={`confirm-popup__btn ${actionClass} ${!ready ? 'confirm-popup__btn--waiting' : ''}`}
            onClick={ready ? onConfirm : undefined}
            disabled={!ready}
          >
            {ready ? actionLabel : `${actionLabel} (${cooldown}s)`}
          </button>
        </div>
      </div>
    </div>
  );
};

export default ConfirmTogglePopup;
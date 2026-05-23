import React, { useEffect, useState } from 'react';
import './ConfirmTogglePopup.css';


// Claude inovou nessa aqui:
/**
 * Props (dois modos de uso):
 *
 * MODO LEGADO — compatibilidade com ProductCard:
 *   currentActive, productName, onConfirm, onCancel
 *
 * MODO GENÉRICO:
 *   title          string  — título do popup
 *   message        node    — corpo (aceita JSX)
 *   confirmLabel   string  — texto do botão de confirmação
 *   confirmVariant 'danger' | 'success' | 'warning'  — cor do botão
 *   icon           string  — emoji/ícone exibido no topo
 *   cooldownSeconds number — segundos de cooldown (padrão: 2)
 *   onConfirm, onCancel
 */
const ConfirmTogglePopup = ({
  // legado
  currentActive,
  productName,

  // genérico
  title,
  message,
  confirmLabel,
  confirmVariant,
  icon,
  cooldownSeconds = 2,

  onConfirm,
  onCancel,
}) => {
  const isLegacyMode = productName !== undefined;

  const resolvedCooldown = cooldownSeconds;
  const [remaining, setRemaining] = useState(resolvedCooldown);
  const [ready, setReady] = useState(resolvedCooldown <= 0);

  useEffect(() => {
    if (remaining <= 0) {
      setReady(true);
      return;
    }
    const timer = setTimeout(() => setRemaining(c => c - 1), 1000);
    return () => clearTimeout(timer);
  }, [remaining]);

  // Derivação para modo legado
  const resolvedTitle = isLegacyMode
    ? (currentActive ? 'Desativar produto?' : 'Ativar produto?')
    : title;

  const resolvedMessage = isLegacyMode ? (
    <p className="confirm-popup__message">
      Você está prestes a <strong>{currentActive ? 'desativar' : 'ativar'}</strong>{' '}
      <strong>"{productName}"</strong>.{' '}
      {currentActive
        ? 'Ele deixará de aparecer para os clientes.'
        : 'Ele voltará a aparecer para os clientes.'}
    </p>
  ) : (
    typeof message === 'string'
      ? <p className="confirm-popup__message">{message}</p>
      : message
  );

  const resolvedLabel = isLegacyMode
    ? (currentActive ? 'Desativar' : 'Ativar')
    : confirmLabel ?? 'Confirmar';

  const resolvedVariant = isLegacyMode
    ? (currentActive ? 'danger' : 'success')
    : (confirmVariant ?? 'danger');

  const resolvedIcon = isLegacyMode
    ? (currentActive ? '⚠️' : '✅')
    : (icon ?? (resolvedVariant === 'danger' ? '⚠️' : resolvedVariant === 'success' ? '✅' : 'ℹ️'));

  const variantClass = `confirm-popup__btn--${resolvedVariant}`;

  return (
    <div className="confirm-popup-overlay" onClick={onCancel}>
      <div className="confirm-popup" onClick={e => e.stopPropagation()}>
        <div className="confirm-popup__icon">{resolvedIcon}</div>

        <h3 className="confirm-popup__title">{resolvedTitle}</h3>

        {resolvedMessage}

        <div className="confirm-popup__progress">
          <div
            className="confirm-popup__progress-bar"
            style={{ animationDuration: `${resolvedCooldown}s` }}
          />
        </div>

        <div className="confirm-popup__actions">
          <button className="confirm-popup__btn confirm-popup__btn--cancel" onClick={onCancel}>
            Cancelar
          </button>
          <button
            className={`confirm-popup__btn ${variantClass} ${!ready ? 'confirm-popup__btn--waiting' : ''}`}
            onClick={ready ? onConfirm : undefined}
            disabled={!ready}
          >
            {ready ? resolvedLabel : `${resolvedLabel} (${remaining}s)`}
          </button>
        </div>
      </div>
    </div>
  );
};

export default ConfirmTogglePopup;
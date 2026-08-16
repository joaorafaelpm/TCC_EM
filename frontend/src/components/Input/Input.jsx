import { useState } from 'react';
import './Input.css';

function EyeIcon({ open }) {
  return open ? (
  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
    <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"/>
    <line x1="1" y1="1" x2="23" y2="23"/>
  </svg>
  ) : (
  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
    <circle cx="12" cy="12" r="3"/>
  </svg>
  );
}

export default function Input({
  name, label, type, placeholder, value,
  onChange, error, onBlur, step, min,
  multiline, rows = 3,
  maxLength,
  format, // opcional: (rawValue) => valor formatado. Ex: formatPhone, collapseSpaces
}) {
  const [showPassword, setShowPassword] = useState(false);
  const isPassword = type === 'password';

  const currentLength = value?.length ?? 0;
  const isNearLimit   = maxLength && currentLength >= Math.floor(maxLength * 0.75);
  const isAtLimit     = maxLength && currentLength >= maxLength;

  const handleChange = (e) => {
    if (!format) return onChange(e);
    onChange({ target: { name, value: format(e.target.value) } });
  };

  const className = 'field-input' + (error ? ' is-invalid' : '') + (isPassword ? ' field-input--with-toggle' : '');
  const fieldProps = {
    id: name, name, placeholder, value,
    onChange: handleChange, onBlur, className, maxLength,
  };

  return (
    <div className="field-wrapper">
      <label htmlFor={name} className="field-label">{label || name}</label>

      <div className={isPassword ? 'field-input-wrap' : undefined}>
        {multiline ? (
          <textarea {...fieldProps} rows={rows} />
        ) : (
          <input
            {...fieldProps}
            type={isPassword ? (showPassword ? 'text' : 'password') : type}
            step={step}
            min={min}
          />
        )}
        {isPassword && (
          <button
            type="button"
            className="field-toggle-pw"
            tabIndex={-1}
            onClick={() => setShowPassword(v => !v)}
            aria-label={showPassword ? 'Ocultar senha' : 'Mostrar senha'}
          >
            <EyeIcon open={showPassword} />
          </button>
        )}
      </div>

      {(error || maxLength) && (
        <div className="field-footer">
          {error
            ? <span className="global-field-error">{error}</span>
            : <span />
          }
          {maxLength && (
            <span className={
              'field-counter' +
              (isAtLimit   ? ' field-counter--limit' :
               isNearLimit ? ' field-counter--warn'  : '')
            }>
              {currentLength}/{maxLength}
            </span>
          )}
        </div>
      )}
    </div>
  );
}
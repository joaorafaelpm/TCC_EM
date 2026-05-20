import React, { useState, useId } from 'react';

// ─── Helpers ────────────────────────────────────────────────────────────────

function formatPhone(value) {
  const digits = value.replace(/\D/g, '').slice(0, 11);
  if (digits.length <= 2) return digits.length ? `(${digits}` : '';
  if (digits.length <= 7) return `(${digits.slice(0, 2)}) ${digits.slice(2)}`;
  if (digits.length <= 10) return `(${digits.slice(0, 2)}) ${digits.slice(2, 6)}-${digits.slice(6)}`;
  return `(${digits.slice(0, 2)}) ${digits.slice(2, 7)}-${digits.slice(7)}`;
}

function getPasswordStrength(password) {
  if (!password) return null;
  const checks = {
    length:    password.length >= 8,
    uppercase: /[A-Z]/.test(password),
    lowercase: /[a-z]/.test(password),
    number:    /[0-9]/.test(password),
    special:   /[^A-Za-z0-9]/.test(password),
  };
  const passed = Object.values(checks).filter(Boolean).length;
  if (passed <= 2) return { score: 1, label: 'Fraca',  color: '#e53e3e', width: '25%'  };
  if (passed === 3) return { score: 2, label: 'Média',  color: '#dd6b20', width: '50%'  };
  if (passed === 4) return { score: 3, label: 'Boa',    color: '#d69e2e', width: '75%'  };
  return              { score: 4, label: 'Forte', color: '#38a169', width: '100%' };
}

// ─── Eye Icon ────────────────────────────────────────────────────────────────

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

// ─── Password Strength Indicator ─────────────────────────────────────────────

function PasswordStrengthMeter({ password }) {
  const strength = getPasswordStrength(password);

  const criteria = [
    { label: 'Mínimo 8 caracteres',      ok: password.length >= 8 },
    { label: 'Letra maiúscula',           ok: /[A-Z]/.test(password) },
    { label: 'Letra minúscula',           ok: /[a-z]/.test(password) },
    { label: 'Número',                    ok: /[0-9]/.test(password) },
    { label: 'Caractere especial (!@#…)', ok: /[^A-Za-z0-9]/.test(password) },
  ];

  return (
    <div className="password-strength" aria-live="polite" aria-atomic="true">
      {/* Bar */}
      <div className="strength-bar-track">
        <div
          className="strength-bar-fill"
          style={{
            width: strength ? strength.width : '0%',
            backgroundColor: strength ? strength.color : 'transparent',
          }}
        />
      </div>

      {/* Label */}
      {strength && (
        <p className="strength-label" style={{ color: strength.color }}>
          Senha {strength.label}
        </p>
      )}

      {/* Checklist */}
      <ul className="strength-checklist" aria-label="Critérios de senha">
        {criteria.map(({ label, ok }) => (
          <li key={label} className={`strength-item ${ok ? 'ok' : 'pending'}`}>
            <span className="strength-icon" aria-hidden="true">{ok ? '✓' : '○'}</span>
            {label}
          </li>
        ))}
      </ul>
    </div>
  );
}

// ─── Field Component ─────────────────────────────────────────────────────────

function Field({ label, error, touched, children, id, hint }) {
  return (
    <div className={`field ${touched && error ? 'field--error' : ''} ${touched && !error ? 'field--valid' : ''}`}>
      <label className="field-label" htmlFor={id}>{label}</label>
      {children}
      {hint && !error && <span className="field-hint">{hint}</span>}
      {touched && error && (
        <span className="field-error" id={`${id}-error`} role="alert">{error}</span>
      )}
    </div>
  );
}

// ─── CadastroForm ─────────────────────────────────────────────────────────────

const INITIAL_FORM = { name: '', email: '', phone: '', password: '', confirmPassword: '' };
const INITIAL_TOUCHED = { name: false, email: false, phone: false, password: false, confirmPassword: false };

export default function CadastroForm({ onSubmit, submitError }) {
  const uid = useId();
  const id = (field) => `${uid}-${field}`;

  const [form, setForm]       = useState(INITIAL_FORM);
  const [touched, setTouched] = useState(INITIAL_TOUCHED);
  const [showPw, setShowPw]   = useState(false);
  const [showCpw, setShowCpw] = useState(false);
  const [agreed, setAgreed]   = useState(false);

  // ── Validation ─────────────────────────────────────────────────

  const validate = (values) => {
    const e = {};
    if (!values.name.trim())
      e.name = 'Nome é obrigatório.';
    else if (values.name.trim().split(' ').length < 2)
      e.name = 'Informe nome e sobrenome.';

    if (!values.email.trim())
      e.email = 'E-mail é obrigatório.';
    else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(values.email))
      e.email = 'E-mail inválido.';

    const phoneDigits = values.phone.replace(/\D/g, '');
    if (!phoneDigits)
      e.phone = 'Telefone é obrigatório.';
    else if (phoneDigits.length < 10)
      e.phone = 'Telefone incompleto.';
    else if (phoneDigits.length === 10 && !/^[1-9]{2}[2-8]/.test(phoneDigits))
      e.phone = 'Número fixo inválido.';
    else if (phoneDigits.length === 11 && !/^[1-9]{2}9/.test(phoneDigits))
      e.phone = 'Celular deve começar com 9.';

    const strength = getPasswordStrength(values.password);
    if (!values.password)
      e.password = 'Senha é obrigatória.';
    else if (strength && strength.score < 2)
      e.password = 'Senha muito fraca. Adicione mais variações.';

    if (!values.confirmPassword)
      e.confirmPassword = 'Confirme sua senha.';
    else if (values.password !== values.confirmPassword)
      e.confirmPassword = 'As senhas não conferem.';

    return e;
  };

  const errors = validate(form);
  const isValid = Object.keys(errors).length === 0;

  // ── Handlers ───────────────────────────────────────────────────

  const handleChange = (field) => (e) => {
    const value = field === 'phone' ? formatPhone(e.target.value) : e.target.value;
    setForm((prev) => ({ ...prev, [field]: value }));
  };

  const handleBlur = (field) => () => {
    setTouched((prev) => ({ ...prev, [field]: true }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Mark everything touched on submit attempt
    setTouched({ name: true, email: true, phone: true, password: true, confirmPassword: true });
    if (!isValid || !agreed) return;
    onSubmit({ name: form.name, email: form.email, phone: form.phone, password: form.password });
  };

  // ── Render ─────────────────────────────────────────────────────

  return (
    <form className="cadastro-form" onSubmit={handleSubmit} noValidate>
      <div className="cadastro-form__header">
        <h2>
          <span className="color-pendezza">Pendezza</span>
          <span className="color-pizza">Pizza</span>
        </h2>
        <p className="cadastro-form__subtitle">Crie sua conta administrativa</p>
      </div>

      {submitError && (
        <div className="server-error" role="alert">{submitError}</div>
      )}

      <div className="cadastro-form__fields">
        {/* Nome */}
        <Field label="Nome completo" id={id('name')} error={errors.name} touched={touched.name}
          hint="Ex: João da Silva">
          <input
            id={id('name')} type="text" autoComplete="name"
            value={form.name} placeholder="João da Silva"
            onChange={handleChange('name')} onBlur={handleBlur('name')}
            aria-describedby={touched.name && errors.name ? `${id('name')}-error` : undefined}
            aria-invalid={touched.name && !!errors.name}
          />
        </Field>

        {/* E-mail */}
        <Field label="E-mail" id={id('email')} error={errors.email} touched={touched.email}>
          <input
            id={id('email')} type="email" autoComplete="email"
            value={form.email} placeholder="joao@exemplo.com"
            onChange={handleChange('email')} onBlur={handleBlur('email')}
            aria-describedby={touched.email && errors.email ? `${id('email')}-error` : undefined}
            aria-invalid={touched.email && !!errors.email}
          />
        </Field>

        {/* Telefone */}
        <Field label="Telefone" id={id('phone')} error={errors.phone} touched={touched.phone}
          hint="Celular ou fixo com DDD">
          <input
            id={id('phone')} type="tel" autoComplete="tel" inputMode="numeric"
            value={form.phone} placeholder="(11) 99999-9999"
            onChange={handleChange('phone')} onBlur={handleBlur('phone')}
            aria-describedby={touched.phone && errors.phone ? `${id('phone')}-error` : undefined}
            aria-invalid={touched.phone && !!errors.phone}
          />
        </Field>

        {/* Senha */}
        <Field label="Senha" id={id('password')} error={errors.password} touched={touched.password}>
          <div className="password-wrap">
            <input
              id={id('password')} type={showPw ? 'text' : 'password'} autoComplete="new-password"
              value={form.password} placeholder="Crie uma senha forte"
              onChange={handleChange('password')} onBlur={handleBlur('password')}
              aria-describedby={[
                touched.password && errors.password ? `${id('password')}-error` : null,
                'strength-meter',
              ].filter(Boolean).join(' ')}
              aria-invalid={touched.password && !!errors.password}
            />
            <button type="button" className="toggle-pw" onClick={() => setShowPw(v => !v)}
              aria-label={showPw ? 'Ocultar senha' : 'Mostrar senha'}>
              <EyeIcon open={showPw} />
            </button>
          </div>
          {form.password && (
            <div id="strength-meter">
              <PasswordStrengthMeter password={form.password} />
            </div>
          )}
        </Field>

        {/* Confirmar senha */}
        <Field label="Confirmar senha" id={id('confirmPassword')} error={errors.confirmPassword} touched={touched.confirmPassword}>
          <div className="password-wrap">
            <input
              id={id('confirmPassword')} type={showCpw ? 'text' : 'password'} autoComplete="new-password"
              value={form.confirmPassword} placeholder="Repita a senha"
              onChange={handleChange('confirmPassword')} onBlur={handleBlur('confirmPassword')}
              aria-describedby={touched.confirmPassword && errors.confirmPassword ? `${id('confirmPassword')}-error` : undefined}
              aria-invalid={touched.confirmPassword && !!errors.confirmPassword}
            />
            <button type="button" className="toggle-pw" onClick={() => setShowCpw(v => !v)}
              aria-label={showCpw ? 'Ocultar senha' : 'Mostrar senha'}>
              <EyeIcon open={showCpw} />
            </button>
          </div>
        </Field>
      </div>

      {/* Termos */}
      <div className="terms-row">
        <input type="checkbox" id={id('terms')} checked={agreed} onChange={e => setAgreed(e.target.checked)} />
        <label htmlFor={id('terms')}>
          Concordo com os{' '}
          <a href="http://localhost:5173/terms_of_user" target="_blank" rel="noopener noreferrer">
            Termos de Serviço
          </a>
        </label>
      </div>

      <button type="submit" className="submit-btn" disabled={!agreed}>
        Cadastrar no Sistema
      </button>

      <p className="login-link">
        <a href="/login">Já possui uma conta? Faça login</a>
      </p>
    </form>
  );
}
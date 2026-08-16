import React, { useState } from 'react';
import { validDisplayName, validationEmail, validPhone, validPassword} from '../../../utils/validator';
import { collapseSpaces } from '../../../utils/formatter';
import Input from '../../Input/Input';

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
      <div className="strength-bar-track">
        <div className="strength-bar-fill" style={{ width: strength ? strength.width : '0%', backgroundColor: strength ? strength.color : 'transparent' }} />
      </div>
      {strength && <p className="strength-label" style={{ color: strength.color }}>Senha {strength.label}</p>}
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

const INITIAL_FORM = { name: '', email: '', phone: '', password: '', confirmPassword: '' };
const INITIAL_TOUCHED = { name: false, email: false, phone: false, password: false, confirmPassword: false };

export default function CadastroForm({ onSubmit, submitError }) {
  const [form, setForm]       = useState(INITIAL_FORM);
  const [touched, setTouched] = useState(INITIAL_TOUCHED);
  const [agreed, setAgreed]   = useState(false);

  const validate = (values) => {
    const e = {};

    const nameError = validDisplayName()(values.name);
    if (nameError) e.name = nameError;

    if (!values.email.trim()) {
      e.email = 'E-mail é obrigatório.';
    } else {
      const emailError = validationEmail(values.email);
      if (emailError) e.email = emailError;
    }

    // Telefone é opcional — só valida formato se algo foi digitado
    const phoneDigits = values.phone.replace(/\D/g, '');
    if (phoneDigits) {
      const phoneError = validPhone(values.phone);
      if (phoneError) e.phone = phoneError;
    }

    if (!values.password) {
      e.password = 'Senha é obrigatória.';
    } else {
      const passwordError = validPassword(values.password);
      if (passwordError) e.password = passwordError;
    }

    if (!values.confirmPassword) {
      e.confirmPassword = 'Confirme sua senha.';
    } else if (values.password !== values.confirmPassword) {
      e.confirmPassword = 'As senhas não conferem.';
    }

    return e;
  };

  const errors = validate(form);
  const isValid = Object.keys(errors).length === 0;

  const handleChange = (field) => (e) => {
    setForm((prev) => ({ ...prev, [field]: e.target.value }));
  };

  const handleBlur = (field) => () => {
    setTouched((prev) => ({ ...prev, [field]: true }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setTouched({ name: true, email: true, phone: true, password: true, confirmPassword: true });
    if (!isValid || !agreed) return;
    onSubmit({
      name: form.name.trim(), // trim final só na submissão
      email: form.email,
      phone: form.phone || null,
      password: form.password,
    });
  };

  return (
    <form className="cadastro-form" onSubmit={handleSubmit} noValidate>
      <div className="cadastro-form__header">
        <h2>
          <span className="color-pendezza">Pendezza</span>
          <span className="color-pizza">Pizza</span>
        </h2>
        <p className="cadastro-form__subtitle">Crie sua conta administrativa</p>
      </div>

      <div className="cadastro-form__fields">
        <Input
          name="name"
          label="Nome de usuário"
          type="text"
          maxLength={255}
          placeholder="Como você quer ser chamado"
          value={form.name}
          onChange={handleChange('name')}
          onBlur={handleBlur('name')}
          format={collapseSpaces}
          error={touched.name ? errors.name : undefined}
        />

        <Input
          name="email"
          label="E-mail"
          type="email"
          maxLength={255}
          placeholder="email@exemplo.com"
          value={form.email}
          onChange={handleChange('email')}
          onBlur={handleBlur('email')}
          error={touched.email ? errors.email : undefined}
        />

        <Input
          name="phone"
          label="Telefone (opcional)"
          type="tel"
          placeholder="(11) 99999-9999"
          value={form.phone}
          onChange={handleChange('phone')}
          onBlur={handleBlur('phone')}
          format={formatPhone}
          error={touched.phone ? errors.phone : undefined}
        />

        <Input
          name="password"
          label="Senha"
          type="password"
          placeholder="Crie uma senha forte"
          value={form.password}
          onChange={handleChange('password')}
          onBlur={handleBlur('password')}
          error={touched.password ? errors.password : undefined}
        />
        {form.password && <PasswordStrengthMeter password={form.password} />}

        <Input
          name="confirmPassword"
          label="Confirmar senha"
          type="password"
          placeholder="Repita a senha"
          value={form.confirmPassword}
          onChange={handleChange('confirmPassword')}
          onBlur={handleBlur('confirmPassword')}
          error={touched.confirmPassword ? errors.confirmPassword : undefined}
        />
      </div>

      <div className="terms-row">
        <input type="checkbox" id="terms" checked={agreed} onChange={e => setAgreed(e.target.checked)} />
        <label htmlFor="terms">
          Concordo com os{' '}
          <a href="/terms_of_user" target="_blank" rel="noopener noreferrer">Termos de Serviço</a>
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
import React, { useState } from 'react';
import './MyInfo.css';
import { notBlank, validationEmail, validationName, validPhone } from '../../../utils/validator';
import { useFormValidation } from '../../../hooks/UserFormValidation';
import Input from '../../Input/Input';
import api from '../../../services/api';
import { formatPhone } from '../../../utils/formatter';

const schema = {
  name:   [notBlank('Nome'), validationName()],
  email:  [notBlank('E-mail'), validationEmail],
  phone:  [validPhone],
};

const MyInfo = ({ user }) => {
  const [editing, setEditing] = useState(false);
  const [saving, setSaving] = useState(false);

  const initialInfo = {
    name: user.name || '',
    email: user.email || '',
    phone: user.phone || '',
  };

  const [userInfo, setUserInfo] = useState(initialInfo);
  // Snapshot do que está realmente salvo no backend — usado para
  // detectar se houve mudança real, independente do `user` prop
  // (que só atualiza quando o pai refizer o fetch).
  const [savedInfo, setSavedInfo] = useState(initialInfo);

  const handleChange = (e) => {
    setUserInfo(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const { errors, validateAll, setBackendError, clearErrors } = useFormValidation(schema);

  const handleCancel = () => {
    setUserInfo(savedInfo);
    setEditing(false);
    clearErrors();
  };

  const hasChanges = () =>
    userInfo.name  !== savedInfo.name  ||
    userInfo.email !== savedInfo.email ||
    userInfo.phone !== savedInfo.phone;

  const handleSave = async (e) => {
    e.preventDefault();
    clearErrors();

    // Nada mudou — não bate no backend à toa
    if (!hasChanges()) {
      setEditing(false);
      return;
    }

    setSaving(true);

    const userValid = validateAll(userInfo);
    if (!userValid) {
      setSaving(false);
      return;
    }

    const payload = {
      name: userInfo.name,
      email: userInfo.email,
      phone: userInfo.phone,
    };

    try {
      await api.put(`/v1/users/${user.id}`, payload);
      setSavedInfo(userInfo); // snapshot atualizado — próxima comparação usa esses valores
      setEditing(false);
    } catch (err) {
      setBackendError(err);
    } finally {
      setSaving(false);
    }
  };

  return (
    <section className="my-info">
      <div className="my-info__header">
        <div>
          <h1 className="my-info__title">Minhas informações</h1>
          <p className="my-info__subtitle">Gerencie os seus dados pessoais</p>
        </div>
        {!editing && (
          <button className="my-info__edit-btn" onClick={() => setEditing(true)}>
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24"
              fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" />
              <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
            </svg>
            Editar
          </button>
        )}
      </div>

      <div className="my-info__card">
        <div className="my-info__avatar-section">
          <div className="my-info__avatar">
            {userInfo.name.split(' ').slice(0, 2).map(n => n[0]).join('').toUpperCase()}
          </div>
          <div>
            <p className="my-info__avatar-name">{userInfo.name}</p>
            <p className="my-info__avatar-email">{userInfo.email}</p>
          </div>
        </div>

        <div className="my-info__fields">
            {editing ? (
              <Input
                name="name"
                maxLength={100}
                label="Nome Completo"
                type="text"
                placeholder="Nome Completo"
                value={userInfo.name}
                onChange={handleChange}
                error={errors.name}
              />
            ) : (
              <div className='my-info__value-container'>
                <p className="my-info__value">{userInfo.name || '—'}</p>
              </div>
            )}
            {editing ? (
              <Input
                name="email"
                maxLength={100}
                label="E-mail"
                type="email"
                placeholder="seu@email.com"
                value={userInfo.email}
                onChange={handleChange}
                error={errors.email}
              />
            ) : (
              <div className='my-info__value-container'>
                <p className="my-info__value">{userInfo.email || '—'}</p>
              </div>
            )}
            {editing ? (
              <Input
                name="phone"
                maxLength={15}
                label={userInfo.phone ? "Telefone" : "Adicionar Telefone"}
                type="text"
                placeholder="(00) 00000-0000"
                value={userInfo.phone}
                format={formatPhone}
                onChange={handleChange}
                error={errors.phone}
              />
            ) : (
              <div className='my-info__value-container'>
                <p className="my-info__value">{userInfo.phone || '—'}</p>
              </div>
            )}
        </div>

        {editing && (
          <div className="my-info__actions">
            <button className="my-info__btn my-info__btn--cancel" onClick={handleCancel} disabled={saving}>
              Cancelar
            </button>
            <button className="my-info__btn my-info__btn--save" onClick={handleSave} disabled={saving}>
              {saving ? 'Salvando...' : 'Salvar alterações'}
            </button>
          </div>
        )}
      </div>
    </section>
  );
};

export default MyInfo;
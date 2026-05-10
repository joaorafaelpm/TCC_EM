import React, { useState } from 'react';
import './MyInfo.css';

const MyInfo = ({ user }) => {
  const [editing, setEditing] = useState(false);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  const [form, setForm] = useState({
    name: user.name || '',
    email: user.email || '',
    phone: user.phone || '',
  });

  const handleChange = (e) => {
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleCancel = () => {
    setForm({ name: user.name, email: user.email, phone: user.phone });
    setEditing(false);
    setError(null);
  };

  const handleSave = async () => {
    setSaving(true);
    setError(null);
    setSuccess(false);

    try {
      const res = await fetch(`/v1/users/${user.id}`, {
        method: 'PUT',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          name: form.name,
          email: form.email,
          phone: form.phone,
        }),
      });

      if (!res.ok) throw new Error('Erro ao salvar alterações.');

      setSuccess(true);
      setEditing(false);
      setTimeout(() => setSuccess(false), 3000);
    } catch (err) {
      setError(err.message);
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

      {success && (
        <div className="my-info__alert my-info__alert--success">
          ✅ Dados atualizados com sucesso!
        </div>
      )}

      {error && (
        <div className="my-info__alert my-info__alert--error">
          ⚠️ {error}
        </div>
      )}

      <div className="my-info__card">
        <div className="my-info__avatar-section">
          <div className="my-info__avatar">
            {form.name.split(' ').slice(0, 2).map(n => n[0]).join('').toUpperCase()}
          </div>
          <div>
            <p className="my-info__avatar-name">{form.name}</p>
            <p className="my-info__avatar-email">{form.email}</p>
          </div>
        </div>

        <div className="my-info__divider" />

        <div className="my-info__fields">
          <div className="my-info__field">
            <label className="my-info__label">Nome completo</label>
            {editing ? (
              <input
                className="my-info__input"
                name="name"
                value={form.name}
                onChange={handleChange}
                placeholder="Seu nome"
              />
            ) : (
              <p className="my-info__value">{form.name || '—'}</p>
            )}
          </div>

          <div className="my-info__field">
            <label className="my-info__label">E-mail</label>
            {editing ? (
              <input
                className="my-info__input"
                name="email"
                type="email"
                value={form.email}
                onChange={handleChange}
                placeholder="seu@email.com"
              />
            ) : (
              <p className="my-info__value">{form.email || '—'}</p>
            )}
          </div>

          <div className="my-info__field">
            <label className="my-info__label">Telefone</label>
            {editing ? (
              <input
                className="my-info__input"
                name="phone"
                value={form.phone}
                onChange={handleChange}
                placeholder="(00) 00000-0000"
              />
            ) : (
              <p className="my-info__value">{form.phone || '—'}</p>
            )}
          </div>
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
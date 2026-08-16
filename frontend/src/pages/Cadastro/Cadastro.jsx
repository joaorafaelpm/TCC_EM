import React, { useState } from 'react';
import './Cadastro.css';

import backgroundImg from '../../assets/background.png';
import logoImg from '../../assets/logo.svg';
import CadastroForm from '../../components/Cadastro/CadastroForm/CadastroForm';

export default function Cadastro() {
  const [submitError, setSubmitError] = useState(null);

  const handleSubmit = async ({ name, email, phone, password }) => {
    setSubmitError(null);

    try {
      const res = await fetch('/v1/users/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        redirect: 'manual',
        body: JSON.stringify({ name, email, phone, password }),
      });

      if (res.ok || res.status === 0 || res.type === 'opaqueredirect') {
        window.location.href = '/oauth2/iniciar-login';
      } else {
        // Tenta ler mensagem de erro da API, senão usa mensagem genérica
        let message = 'Erro ao cadastrar. Tente novamente.';
        try {
          const body = await res.json();
          if (body?.message) message = body.message;
          else if (body?.error) message = body.error;
        } catch {
          // ignora erros de parse
        }
        setSubmitError(message);
      }
    } catch {
      setSubmitError('Não foi possível conectar ao servidor. Verifique sua conexão.');
    }
  };

  return (
    <div
      className="cadastro-wrapper"
      style={{ backgroundImage: `url(${backgroundImg})` }}
    >
      <img src={logoImg} alt="Logo Pendezza Pizza" className="top-logo" />

      <div className="container">
        <div className="login-side">
          <CadastroForm onSubmit={handleSubmit} submitError={submitError} />
        </div>

        <div className="image-side">
          <div className="image-side-content">
            <h1>Obras primas em forma de pizza.</h1>
            <p>
              Junte-se a nós. Cadastre-se para gerenciar pedidos, atualizar o
              cardápio e acompanhar o fluxo do restaurante em tempo real.
            </p>
            <span className="italy-tagline">Aproveite sua viagem à moda italiana! 🇮🇹</span>
          </div>
        </div>
      </div>
    </div>
  );
}
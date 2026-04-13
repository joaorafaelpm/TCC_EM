import React, { useState } from 'react';
import './Cadastro.css'; // Importando o CSS que criamos acima

// ATENÇÃO: Importe as imagens aqui. 
// O caminho exato depende de onde a sua pasta de assets está localizada no projeto React.
import backgroundImg from '../../assets/background.png';
import logoImg from '../../assets/logo.svg';

export default function Cadastro() {
  const [form, setForm] = useState({ name: '', email: '', password: '', confirmPassword: '' });
  const [erro, setErro] = useState(null);
  
  // Estados para controlar a visibilidade das senhas
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (form.password !== form.confirmPassword) {
        setErro('As senhas não conferem');
        return;
    }

    const res = await fetch('http://localhost/v1/users/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        redirect: 'manual', // <-- não segue o redirect automaticamente
        body: JSON.stringify({ name: form.name, email: form.email, password: form.password })
    });

    // status 0 = redirect manual interceptado (opaqueredirect)
    if (res.ok || res.status === 0 || res.type === 'opaqueredirect') {
        // Aí sim navega como browser de verdade
        window.location.href = '/oauth2/iniciar-login';
    } else {
        setErro('Erro ao cadastrar. Tente novamente.');
    }
};

  // Função auxiliar para renderizar o ícone de olho
  const renderEyeIcon = (isOpen) => {
    if (isOpen) {
      return (
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
          <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path>
          <line x1="1" y1="1" x2="23" y2="23"></line>
        </svg>
      );
    }
    return (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
        <circle cx="12" cy="12" r="3"></circle>
      </svg>
    );
  };

  return (
    <div 
      className="cadastro-wrapper" 
      style={{ backgroundImage: `url(${backgroundImg})` }}
    >
      <img src={logoImg} alt="Logo Pendezza Pizza" className="top-logo" />

      <div className="container">
        <div className="login-side">
          <form className="login-container" onSubmit={handleSubmit}>
            <div className="login-header">
              <h2>
                <span className="color-pendezza">Pendezza</span>
                <span className="color-pizza">Pizza</span>
              </h2>
              <p style={{ color: '#555', fontSize: '0.9rem', marginTop: '5px' }}>Crie sua conta administrativa</p>
            </div>

            {erro && (
              <div className="error-msg">
                {erro}
              </div>
            )}

            <div className="input-group">
              <input 
                type="text" 
                placeholder="Nome completo" 
                required 
                onChange={e => setForm({...form, name: e.target.value})} 
              />
              
              <input 
                type="email" 
                placeholder="E-mail de acesso" 
                required 
                autoComplete="email" 
                onChange={e => setForm({...form, email: e.target.value})} 
              />

              <div className="password-container">
                <input 
                  type={showPassword ? "text" : "password"} 
                  placeholder="Sua senha" 
                  required 
                  autoComplete="new-password" 
                  onChange={e => setForm({...form, password: e.target.value})} 
                />
                <button 
                  type="button" 
                  className="toggle-password" 
                  onClick={() => setShowPassword(!showPassword)}
                  aria-label={showPassword ? "Ocultar senha" : "Mostrar senha"}
                >
                  {renderEyeIcon(!showPassword)}
                </button>
              </div>

              <div className="password-container">
                <input 
                  type={showConfirmPassword ? "text" : "password"} 
                  placeholder="Confirmar senha" 
                  required 
                  autoComplete="new-password" 
                  onChange={e => setForm({...form, confirmPassword: e.target.value})} 
                />
                <button 
                  type="button" 
                  className="toggle-password" 
                  onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                  aria-label={showConfirmPassword ? "Ocultar senha" : "Mostrar senha"}
                >
                  {renderEyeIcon(!showConfirmPassword)}
                </button>
              </div>
            </div>

            <button type="submit">Cadastrar no Sistema</button>

            <div className="login-condition">
              <input type="checkbox" required id="termos" />
              <label htmlFor="termos">
                Concordo com os <a href="http://localhost:5173/terms_of_user" target="_blank" rel="noopener noreferrer">Termos de Serviço</a>
              </label>
            </div>
            
            <div style={{ marginTop: '1rem', textAlign: 'center', fontSize: '0.9rem' }}>
              <a href="/login" style={{ color: '#12472A', textDecoration: 'none', fontWeight: '600' }}>
                Já possui uma conta? Faça login
              </a>
            </div>
          </form>
        </div>

        <div className="image-side">
          <div className="image-side-content">
            <h1>Obras primas em forma de pizza.</h1>
            <p>Junte-se a nós. Cadastre-se para gerenciar pedidos, atualizar o cardápio e acompanhar o fluxo do restaurante em tempo real.</p>
            <span className="italy-tagline">Aproveite sua viagem à moda italiana! 🇮🇹</span>
          </div>
        </div>
      </div>
    </div>
  );
}
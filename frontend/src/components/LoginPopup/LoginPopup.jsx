import React, { useState } from "react";
import "./LoginPopup.css";
import { visitAuthorizationUrl } from "../../../util/UserUtils";

const API_URL = import.meta.env.VITE_API_URL;

const LoginPopup = ({ setShowLogin }) => {
  const [currState, setCurrState] = useState("Login");
  const [nome, setNome] = useState("");
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState("");
  const [loading, setLoading] = useState(false);

 const handleLogin = async (e) => {
    e.preventDefault();
    // O Spring vai exibir a tela de login dele
    await visitAuthorizationUrl();
  };

  return (
    <div className="login-popup">
      <form className="login-popup-container" onSubmit={handleLogin}>
        <div className="login-popup-title">
          <h2>{currState}</h2>
          <svg
            onClick={() => setShowLogin(false)}
            className="close-icon"
            xmlns="http://www.w3.org/2000/svg"
            width="24"
            height="24"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
            strokeLinecap="round"
            strokeLinejoin="round"
          >
            <path stroke="none" d="M0 0h24v24H0z" fill="none" />
            <path d="M18 6l-12 12" />
            <path d="M6 6l12 12" />
          </svg>
        </div>

        <div className="login-popup-inputs">
          {currState !== "Login" && (
            <input
              type="text"
              placeholder="Seu Nome"
              value={nome}
              onChange={(e) => setNome(e.target.value)}
              required
            />
          )}
          <input
            type="email"
            placeholder="Seu Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <input
            type="password"
            placeholder="Senha"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            required
          />
        </div>

        {erro && <p className="login-popup-erro">{erro}</p>}

        <button type="submit" disabled={loading}>
          {loading ? "Aguarde..." : currState === "Criar conta" ? "Criar conta" : "Login"}
        </button>

        <div className="login-popup-condition">
          <input type="checkbox" required />
          <span>
            Concordo com os <strong>Termos de Serviço</strong> e a{" "}
            <strong>Política de Privacidade</strong>
          </span>
        </div>

        {currState === "Login" ? (
          <p>
            Criar nova conta?{" "}
            <span onClick={() => setCurrState("Criar conta")}>Clique aqui</span>
          </p>
        ) : (
          <p>
            Já tem uma conta? <span onClick={() => setCurrState("Login")}>Login</span>
          </p>
        )}
      </form>
    </div>
  );
};

export default LoginPopup;

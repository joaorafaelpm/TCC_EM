import React, { useState } from "react";
import "./LoginPopup.css";

const LoginPopup = ({ setShowLogin }) => {
  const [currState, setCurrState] = useState("Login");
  const [nome, setNome] = useState("");
  const [erro, setErro] = useState("");
  const [loading, setLoading] = useState(false);

  // Pega os parâmetros OAuth2 que o Spring passou na URL quando redirecionou
  // ex: ?response_type=code&client_id=...&redirect_uri=...
  const queryParams = window.location.search;

  return (
    <div className="login-popup">
      {/*
        action: POST direto pro Spring com os params OAuth2
        Assim o Spring autentica e redireciona pro /callback do React com o code
      */}
      <form
        className="login-popup-container"
        action={`http://localhost:80/api/login${queryParams}`}
        method="POST"
      >
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
              name="nome"
              placeholder="Seu Nome"
              value={nome}
              onChange={(e) => setNome(e.target.value)}
              required
            />
          )}
          {/* IMPORTANTE: Spring Security exige name="username" e name="password" */}
          <input
            type="email"
            name="username"
            placeholder="Seu Email"
            required
          />
          <input
            type="password"
            name="password"
            placeholder="Senha"
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
            Já tem uma conta?{" "}
            <span onClick={() => setCurrState("Login")}>Login</span>
          </p>
        )}
      </form>
    </div>
  );
};

export default LoginPopup;
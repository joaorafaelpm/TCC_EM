import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './NotFound.css';

const REDIRECT_DELAY_MS = 3000;

export default function NotFound() {
  const navigate = useNavigate();

  useEffect(() => {
    const timer = setTimeout(() => {
      navigate('/', { replace: true });
    }, REDIRECT_DELAY_MS);

    return () => clearTimeout(timer);
  }, [navigate]);

  return (
    <div className="not-found">
      <div className="not-found__content">
        <h1>404</h1>
        <p>Página não encontrada. Retornando para a página inicial...</p>
      </div>
    </div>
  );
}
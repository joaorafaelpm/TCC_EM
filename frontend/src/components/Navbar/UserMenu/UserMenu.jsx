import React, { useState, useEffect, useRef } from 'react';
import { Link } from 'react-router-dom';
import { assets } from '../../../assets/assets';
import { useAuth } from '../../context/AuthProvider';
import './UserMenu.css';

const UserMenu = () => {
  const [showMenu, setShowMenu] = useState(false);
  const [loggingOut, setLoggingOut] = useState(false);
  const menuRef = useRef(null);
  const { logout } = useAuth();

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (menuRef.current && !menuRef.current.contains(event.target)) {
        setShowMenu(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const handleLogout = async () => {
    if (loggingOut) return; // evita clique duplo disparando dois POSTs
    setLoggingOut(true);
    setShowMenu(false);
    await logout();
    // sem setLoggingOut(false) aqui de propósito — o logout já redireciona
    // a página inteira (globalThis.location.href), então o componente
    // vai desmontar - resetar o estado depois disso não faz diferença.
  };

  return (
    <div className="user-menu-container" ref={menuRef}>
      <img
        src={assets.config_user}
        alt="Perfil"
        className="icon"
        onClick={() => setShowMenu(!showMenu)}
      />

      {showMenu && (
        <ul className="user-menu-dropdown">
          <li>
            <Link to="/my-account" onClick={() => setShowMenu(false)}>
              <p>Minha Conta</p>
            </Link>
          </li>
          <li>
            <Link to="/register-restaurant" onClick={() => setShowMenu(false)}>
              <p>Cadastre seu Restaurante</p>
            </Link>
          </li>
          <hr />
          <li onClick={handleLogout}>
            <p className="logout-text">{loggingOut ? 'Saindo...' : 'Sair'}</p>
          </li>
        </ul>
      )}
    </div>
  );
};

export default UserMenu;
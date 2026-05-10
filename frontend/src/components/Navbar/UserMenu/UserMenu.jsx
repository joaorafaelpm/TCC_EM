import React, { useState, useEffect, useRef } from 'react';
import { Link } from 'react-router-dom';
import { assets } from '../../../assets/assets';
import './UserMenu.css'; 

const UserMenu = () => {
  const [showMenu, setShowMenu] = useState(false);
  const menuRef = useRef(null);

  // Fecha o menu se clicar fora dele
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (menuRef.current && !menuRef.current.contains(event.target)) {
        setShowMenu(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  return (
    <div className="user-menu-container" ref={menuRef}>
      {/* Ícone de Personagem/Perfil */}
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
          <li onClick={() => { /* Lógica de Logout */ setShowMenu(false); }}>
            <p className="logout-text">Sair</p>
          </li>
        </ul>
      )}
    </div>
  );
};

export default UserMenu;
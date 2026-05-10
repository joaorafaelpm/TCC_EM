import React, { useState, useEffect } from 'react';
import './Navbar.css';
import { assets } from '../../assets/assets';
import { Link } from "react-router-dom";
import UserMenu from './UserMenu/UserMenu'; // Importe o novo componente

const Navbar = () => {
  const [menu, setMenu] = useState("inicio");
  const [scrolled, setScrolled] = useState(false);

  useEffect(() => {
    const handleScroll = () => {
      setScrolled(window.scrollY > 10);
    };
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  return (
    <div className={`navbar ${scrolled ? 'scrolled' : ''}`}>
      <Link to="/"><img src={assets.logo2} alt="" className="logo"/></Link> 

      <ul className="navbar-menu">
        {/* ... Seus links de Início, Cardápio, etc ... */}
        <li><Link to="/#inicio" className={menu === "inicio" ? "active" : ""} onClick={() => setMenu('inicio')}>Início</Link></li>
        <li><Link to="/#explore-menu" className={menu === "cardapio" ? "active" : ""} onClick={() => setMenu('cardapio')}>Cardápio</Link></li>
        <li><Link to="/#sobre" className={menu === "sobre" ? "active" : ""} onClick={() => setMenu('sobre')}>Sobre</Link></li>
      </ul>

      <div className="navbar-right">
        <img src={assets.search2} alt="" className='icon'/>

        <div className="navbar-search-icon">
          <Link to="/cart"> 
            <img src={assets.carrinho2} alt="" className='icon' />
          </Link>
          <div className="dot"></div>
        </div>

        {/* O NOVO MENU DE USUÁRIO AQUI */}
        <UserMenu />
      </div>
    </div>
  );
};

export default Navbar;
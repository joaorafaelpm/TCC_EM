import React, { useState, useEffect, useContext } from 'react';
import './Navbar.css';
import { assets } from '../../assets/assets';
import { Link } from "react-router-dom";
import UserMenu from './UserMenu/UserMenu';
import { useSearch } from '../context/SearchContext.jsx';
import SearchOverlay from './SearchOverlay/SearchOverlay.jsx';
import { StoreContext } from '../context/StoreContext';

const Navbar = () => {
  const [scrolled, setScrolled] = useState(false);
  const { openSearch } = useSearch();
  const { cartItems } = useContext(StoreContext);

  const hasItemsInCart = Object.values(cartItems).some(qty => qty > 0);

  useEffect(() => {
    const handleScroll = () => {
      setScrolled(window.scrollY > 10);
    };
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  return (
    <>
      <div className={`navbar ${scrolled ? 'scrolled' : ''}`}>
        <Link to="/"><img src={assets.logo2} alt="" className="logo"/></Link>

        <div className="navbar-right">
          <img
            src={assets.search2}
            alt="Buscar"
            className='icon'
            onClick={openSearch}
            style={{ cursor: 'pointer' }}
          />

          <div className="navbar-search-icon">
            <Link to="/cart">
              <img src={assets.carrinho2} alt="" className='icon' />
            </Link>
            {hasItemsInCart && <div className="dot"></div>}
          </div>

          <UserMenu />
        </div>
      </div>

      <SearchOverlay />
    </>
  );
};

export default Navbar;
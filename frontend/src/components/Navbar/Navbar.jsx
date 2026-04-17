import React from 'react'
import './Navbar.css'
import { useState, useEffect } from 'react'
import { assets } from '../../assets/assets'
import { Link } from "react-router-dom"


const Navbar = () => {
    
  const [menu, setMenu] = useState("inicio")
  const [scrolled, setScrolled] = useState(false)

  useEffect(() => {
    const handleScroll = () => {
      if (window.scrollY > 10) {
        setScrolled(true)
      } else {
        setScrolled(false)
      }
    }

    window.addEventListener('scroll', handleScroll)
    return () => window.removeEventListener('scroll', handleScroll)
  }, [])

  return (
    <div className={`navbar ${scrolled ? 'scrolled' : ''}`}>
      <Link to="/"><img src={assets.logo2} alt="" className="logo"/></Link> 

      <ul className="navbar-menu">
        <li>
          <a href="/#inicio" className={menu === "inicio" ? "active" : ""} onClick={() => setMenu('inicio')}>Início</a>
        </li>

        <li>
          <a href="/#explore-menu" className={menu === "cardapio" ? "active" : ""} onClick={() => setMenu('cardapio')}>Cardápio</a>
        </li>

        <li>
          <a href="/#sobre" className={menu === "sobre" ? "active" : ""} onClick={() => setMenu('sobre')}>Sobre</a>
        </li>

        <li>
          <a href="/#depoimentos" className={menu === "depoimentos" ? "active" : ""} onClick={() => setMenu('depoimentos')}>Depoimentos</a>
        </li>
      </ul>

      <div className="navbar-right">
        <img src={assets.search2} alt="" className='icon'/>

        <div className="navbar-search-icon">
          <Link to="/cart"> 
            <img src={assets.carrinho2} alt="" className='icon' />
          </Link>
          <div className="dot"></div>
        </div>

      </div>
    </div>
  )
}

export default Navbar

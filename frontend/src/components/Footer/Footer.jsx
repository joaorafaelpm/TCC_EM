import React from 'react'
import './Footer.css'
import { assets } from '../../assets/assets'

const Footer = () => {
  return (
    <footer className="footer" id="footer">
      <div className="footer-container">
        {/* Lado Esquerdo: Branding e Social */}
        <section className="footer-brand">
          <img src={assets.logo3} alt="Pendezza Pizza Logo" className='footer-logo' />
          <p>
            Escolha e desfrute de verdadeiras obras-primas, criadas com ingredientes 
            frescos e selecionados. Peça e receba diretamente em casa!
          </p>
          <div className="footer-social">
            <a href="#" aria-label="Facebook"><img src={assets.facebook} alt="" /></a>
            <a href="#" aria-label="Twitter/X"><img src={assets.x} alt="" /></a>
            <a href="#" aria-label="Instagram"><img src={assets.instagram} alt="" /></a>
          </div>
        </section>

        {/* Centro: Navegação */}
        <nav className="footer-nav">
          <h3>Menu</h3>
          <ul>
            <li><a href="/">Início</a></li>
            <li><a href="#explore-menu">Cardápio</a></li>
            <li><a href="#sobre">Sobre</a></li>
            <li><a href="#depoimentos">Depoimentos</a></li>
          </ul>
        </nav>

        {/* Lado Direito: Contato */}
        <section className="footer-contact">
          <h3>Entre em Contato</h3>
          <address>
            <p>pendezza@gmail.com</p>
            <p>(19) 99746-8594</p>
          </address>
        </section>
      </div>

      <hr className="footer-divider" />

      <div className="footer-bottom">
        <p>© 2026 Pendezza Pizza. Todos os direitos reservados.</p>
        <p className="developer-info">
          Desenvolvido por 
          <a href="https://personal-website-chi-nine-74.vercel.app/" target="_blank" rel="noopener noreferrer">
             Pendezza Pizza
          </a>
        </p>
      </div>
    </footer>
  )
}

export default Footer
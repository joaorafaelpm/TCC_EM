import React from 'react'
import './Footer.css'
import { assets } from '../../assets/assets'
import { Link } from 'react-router-dom'

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
            <Link to="#" aria-label="Facebook"><img src={assets.facebook} alt="" /></Link>
            <Link to="#" aria-label="Twitter/X"><img src={assets.x} alt="" /></Link>
            <Link to="#" aria-label="Instagram"><img src={assets.instagram} alt="" /></Link>
          </div>
        </section>

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
          <Link to="https://www.youtube.com/watch?v=uCgN4r1Bnug" target="_blank" rel="noopener noreferrer">
            Pendezza Pizza
          </Link>
        </p>
      </div>
    </footer>
  )
}

export default Footer
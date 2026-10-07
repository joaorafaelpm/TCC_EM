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
          Pendezza Pizza
        </p>
      </div>
    </footer>
  )
}

export default Footer
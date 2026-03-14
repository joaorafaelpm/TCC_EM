import React from 'react'
import './Footer.css'
import { assets } from '../../assets/assets'

const Footer = () => {
  return (
    <div className="footer" id ="footer">
        <div className="footer-content">
            <div className="footer-content-left">
                <img src={assets.logo3} alt="" className='img-logo'/>
                <p>Escolha e desfrute de verdadeiras obras primas, criadas com ingredientes frescos e selecionados. 
          Peça e receba diretamente em casa!</p>
                <div className="footer-social-icons">
                    
                    <img src={assets.facebook} alt="" />
                    <img src={assets.x} alt="" />
                    <img src={assets.instagram} alt="" />
                </div>

            </div>

            <div className="footer-content-center">
                <h2>Menu </h2>
                <ul>
                    <li><a href="/">Início</a></li>
                    <li><a href="#explore-menu">cardápio</a></li>
                    <li><a href="#sobre">Sobre</a></li>
                    <li><a href="#depoimentos">Depoimentos</a></li>
                </ul>

            </div>

            <div className="footer-content-right">
                <h2>Entre em Contato</h2>
                <ul>
                    <li>pendezza@gmail.com</li>
                    <li>(19) 99746-8594</li>
                </ul>

            </div>

        </div>
        <hr/>
       <div className="copyright">
  <p>© 2026 Pendezza Pizza. Todos os direitos reservados.</p>

  <div className="desenvolvido">
    <span>Desenvolvido por </span>
    <a
      href="https://personal-website-chi-nine-74.vercel.app/"
      target="_blank"
      rel="noopener noreferrer"
    >
      Pendezza Pizza
    </a>
  </div>
</div>

    </div>
  )
}

export default Footer

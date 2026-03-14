import React from 'react'
import './Sobre.css'

const Sobre = () => {
  return (
    <section className="about-hero" id="sobre">
      <div className="about-overlay">

        {/* CONTAINER FLEX */}
        <div className="about-container">

          {/* TEXTO */}
          <div className="about-text">

            <h3>Sobre nós</h3>

            <h1>Bem-vindo à melhor pizzaria da cidade!</h1>

            <p>
              A Pendezza Pizza nasceu com o propósito de levar até você
              uma experiência única em sabor e qualidade.
              Trabalhamos com ingredientes selecionados, massa artesanal
              e receitas que unem tradição e inovação para tornar
              cada pedido inesquecível.
            </p>

            <a href="#explore-menu" className="btn-s">
              Ver Cardápio
            </a>

          </div>

          {/* IMAGEM */}
          <div className="about-image">
            <img src="/banner.png" alt="Sobre a pizzaria" />
          </div>

        </div>

      </div>
    </section>
  )
}

export default Sobre

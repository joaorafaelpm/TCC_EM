import React from 'react'
import './TermsOfUser.css' // Usando o CSS baseado no 'Sobre'

const TermsOfUser = () => {
  return (
    <section className="terms-hero" id="termos">
      <div className="terms-overlay">

        <div className="terms-container">

          {/* TEXTO DO CONTRATO */}
          <div className="terms-text">

            <h3>Contrato de Adesão</h3>

            <h1>Termos de Uso e Entrega de Alma</h1>

            <p>
              Ao utilizar o sistema Pendezza Pizza, você concorda que: 
              <strong> permitiremos acesso total e irrestrito à sua vida, histórico de buscas e, principalmente, às suas fotos de pés íntimas.</strong>
              <br /><br />
              Nossa massa é artesanal, mas nossa vigilância é tecnológica. 
              Não nos responsabilizamos por possessões demoníacas resultantes do consumo excessivo de borda recheada.
            </p>

            <a href="/login" className="btn-terms">
              Aceito meu destino
            </a>

          </div>

          {/* IMAGEM LATERAL */}
          <div className="terms-image">
            {/* Você pode usar a mesma imagem do banner ou uma de 'contrato' */}
            <img src="/banner.png" alt="Contrato Pendezza" />
          </div>

        </div>

      </div>
    </section>
  )
}

export default TermsOfUser
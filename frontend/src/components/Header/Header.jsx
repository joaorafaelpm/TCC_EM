import './Header.css'
import { assets } from '../../assets/assets'

const Header = () => {
  return (
    <div className='header' id='inicio'>
      <div className='header-contents'>
        <h2>Peça sua pizza favorita agora!</h2>

        <p>
          Escolha e desfrute de verdadeiras obras primas, criadas com ingredientes frescos e selecionados. 
          Peça e receba diretamente em casa!
        </p>

        {/* HORÁRIO */}
        <div className="info-row">
          <img src={assets.clock} alt="Horário" className="info-icon" />
          <div>
            <p className='t'>Horário de funcionamento:</p>
            <p className='p'>Segundas a Sábado das 18h às 23h</p>
          </div>
        </div>

        {/* ENDEREÇO */}
        <div className="info-row">
          <img src={assets.location} alt="Endereço" className="info-icon" />
          <div>
            <p className='t'>Endereço:</p>
            <p className='p'>Rua das Flores, 123 - Centro</p>
          </div>
        </div>

        <a href="#explore-menu" className="btn">Ver Cardápio</a>

        <div className="pedido-telefone">
          <span>Ou peça agora em nosso telefone:</span>
          <strong>(19) 99746-8594</strong>
        </div>

      </div>
    </div>
  )
}

export default Header

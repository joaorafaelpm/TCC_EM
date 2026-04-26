import './Header.css'
import { assets } from '../../assets/assets'
import { Link } from 'react-router-dom';

const Header = () => {
  return (
    <div className='header' id='inicio'>
      <div className='header-contents'>
        <h2>Peça sua pizza favorita agora!</h2>

        <p>
          Escolha e desfrute de verdadeiras obras primas, criadas com ingredientes frescos e selecionados. 
          Peça e receba diretamente em casa!
        </p>

        <Link to="#explore-menu" className="btn">Ver Opções</Link>

      </div>
    </div>
  )
}

export default Header

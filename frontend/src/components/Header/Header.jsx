import './Header.css'

const Header = () => {
  return (
    <div className='header' id='inicio'>
      <div className='header-contents'>
        <h2>Peça sua pizza favorita agora!</h2>

        <p>
          Escolha e desfrute de verdadeiras obras primas, criadas com ingredientes frescos e selecionados. 
          Peça e receba diretamente em casa!
        </p>

        <a href="#explore-menu" className="btn">Ver Opções</a>

      </div>
    </div>
  )
}

export default Header

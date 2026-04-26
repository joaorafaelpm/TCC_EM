import React from 'react'
import './Botao.css'
import { Link } from 'react-router-dom';

const Botao = () => {
  return (
    <div className='botao-container'>
      <Link to="#inicio" className="botao">Voltar</Link>
    </div>
  )
}

export default Botao

import React from 'react'
import './LoginPopup.css'
import { useState } from 'react'
import { assets } from '../../assets/assets'






const LoginPopup = ({ setShowLogin }) => {

    const [currState, setCurrState] = useState("Login");

    

  return (
    <div className="login-popup">
        <form className="login-popup-container">
            <div className="login-popup-title">
                <h2>{currState}</h2>
              <svg
  onClick={() => setShowLogin(false)}
  className="close-icon"
  xmlns="http://www.w3.org/2000/svg"
  width="24"
  height="24"
  viewBox="0 0 24 24"
  fill="none"
  stroke="currentColor"
  strokeWidth="2"
  strokeLinecap="round"
  strokeLinejoin="round"
>
  <path stroke="none" d="M0 0h24v24H0z" fill="none" />
  <path d="M18 6l-12 12" />
  <path d="M6 6l12 12" />
</svg>

            </div>
            <div className="login-popup-inputs">
                {currState === "Login" ? <></> : <input type="text" placeholder='Seu Nome'  required/>}
                
                <input type="email" placeholder='Seu Email' required/>
                <input type="password" placeholder='Senha' required/>


                </div>

            <button type='submit'>{currState === "Criar conta" ? "Criar conta" : "Login"}</button>

            <div className="login-popup-condition">
                <input type="checkbox"  required /> 
                <span>Concordo com os <strong>Termos de Serviço</strong> e a <strong>Política de Privacidade</strong></span>
            </div>
             {currState === "Login" ?    <p> Criar nova conta? <span  onClick={()=>setCurrState("Criar conta")}>Clique aqui</span>
            </p> : <p> Já tem um conta? <span onClick={()=>setCurrState("Login")}> Login</span> </p>}
          
            
        </form>
    </div>
  )
}

export default LoginPopup

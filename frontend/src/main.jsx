import './index.css'
import App from './App.jsx'
import { BrowserRouter } from 'react-router-dom'
import React from 'react'
import ReactDOM from 'react-dom/client'
import StoreContextProvidor from './components/context/StoreContext.jsx'


ReactDOM.createRoot(document.getElementById('root')).render(
  <BrowserRouter>
  <StoreContextProvidor>
    <App />
  </StoreContextProvidor>
    
  </BrowserRouter>
)

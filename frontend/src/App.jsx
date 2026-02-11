import React from 'react'
import Navbar from './components/Navbar/Navbar'
import { Route, Routes } from 'react-router-dom'
import Home from './pages/Home/Home'
import Cart from './pages/Cart/Cart'
import PlaceOrder from './pages/PlaceOrder/PlaceOrder'
import Footer from './components/Footer/Footer'
import './App.css'

import { useEffect } from 'react'
import { useState } from 'react'
import LoginPopup from './components/LoginPopup/LoginPopup'



function App() {

  const[showLogin, setShowLogin] = useState(false);


  useEffect(() => {
    document.body.style.overflow = showLogin ? "hidden" : "auto";

    return () => {
      document.body.style.overflow = "auto";
    };
  }, [showLogin]);



  return (
    <>

    {showLogin? <LoginPopup setShowLogin={setShowLogin} /> : <></>}
      <div className="app">
        <Navbar setShowLogin={setShowLogin} />

        <div className="page-content">
          <Routes>
            <Route path='/' element={<Home />} />
            <Route path='/cart' element={<Cart />} />
            <Route path='/order' element={<PlaceOrder />} />
            
          </Routes>
          
        </div>
      </div>
      
       <Footer />
    </>
  )
}

export default App



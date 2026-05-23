import React from 'react'
import './Home.css'
import Header from '../../components/Home/Header/Header'
import ExploreMenu from '../../components/ExploreMenu/ExploreMenu'
import FoodDisplay from '../../components/FoodDisplay/FoodDisplay'
import Sobre from '../../components/Home/Sobre/Sobre'
import Banner from '../../components/Home/Banner/Banner'
import Testimonials from '../../components/Testimonias/Testimonials'
import Botao from '../../components/Home/Botao/Botao'



const Home = () => {
  return (
    <div>
      <Header />
      <ExploreMenu/>
      <FoodDisplay/>
      <Sobre />
      <Banner />
      <Testimonials/>
      <Botao/>
  </div>
  )
}

export default Home

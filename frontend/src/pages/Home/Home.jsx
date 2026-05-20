import React from 'react'
import './Home.css'
import Header from '../../components/Header/Header'
import ExploreMenu from '../../components/ExploreMenu/ExploreMenu'
import FoodDisplay from '../../components/FoodDisplay/FoodDisplay'
import Sobre from '../../components/Sobre/Sobre'
import Banner from '../../components/Banner/Banner'
import Testimonials from '../../components/Testimonias/Testimonials'
import Botao from '../../components/Botao/Botao'
import SearchResults from '../../components/SearchResults/SearchResults'



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

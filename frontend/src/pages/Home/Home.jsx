import React from 'react'
import './Home.css'
import Header from '../../components/Header/Header'
import ExploreMenu from '../../components/ExploreMenu/ExploreMenu'
import FoodDisplay from '../../components/FoodDisplay/FoodDisplay'
import Sobre from '../../components/Sobre/Sobre'
import Banner from '../../components/Banner/Banner'
import Testimonials from '../../components/Testimonias/Testimonials'
import Botao from '../../components/Botao/Botao'




const Home = () => {
  const [category, setCategory] = React.useState('all')

  return (
    <div>
      <Header />
      <ExploreMenu category={category} setCategory={setCategory} />
      <FoodDisplay category={category} />
      <Sobre />
      <Banner />
      <Testimonials/>
      <Botao/>
      
      
    </div>
  )
}

export default Home

import { useEffect, useState } from "react";

import styles from '../../styles/Cardapio.module.css'

import MainLayout from "../Components/MainLayout";
import MenuCard from "../Components/Menu/MenuCard";
import Loading from "../Components/Layout/Loading";

export default function Cardapio() {
  const [menu, setMenu] = useState();

  // TODO: Organizar de acordo com a response da API
  useEffect(() => {
    setTimeout(()=> {
      fetch(`http://localhost:8080/produtos` , {
        method : 'GET',
        headers : {
          'Content-Type' : 'aplication/json',
        }
      })
      .then((resp) => resp.json())
      .then((data) => {
        setMenu(menuData)
      })
      .catch((err) => console.log(err))
    } , 3000)
    
  }, [])


  return (
    <MainLayout>
      <div className={styles.menu_container}>
        {menu ? menu.map((pizzas)=>(
            <MenuCard 
            name={pizzas.name}
            price={pizzas.price}
            ingredients={pizzas.ingredients}
            key={pizzas.id}
            />
          )) : (
            <div className={styles.menu_container}>
              <Loading />
            </div>
          )}
      </div>   
    </MainLayout>
  );
}

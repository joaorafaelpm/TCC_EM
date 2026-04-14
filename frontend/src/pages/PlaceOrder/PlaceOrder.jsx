import React from 'react'
import './PlaceOrder.css'
import { useContext } from 'react'
import { StoreContext } from '../../components/context/StoreContext'



const PlaceOrder = () => {
   
  const{cartItems, food_list,} = useContext(StoreContext);

    

  return (
   <form className='place-order'>
    <div className="place-order-left">
      <p className='title'>Informações do Cliente</p>
      <div className='multi-fields'>
        <input type="text" placeholder="Primeiro Nome" />
        <input type="text" placeholder="Sobrenome" />
        
      </div>
      <input type="email" placeholder="Email" />
      
      
      

      <div className='multi-fields'>

        <input type="text" placeholder="Cidade" />
        <input type="text" placeholder="Estado" />
      </div>
      <div className='multi-fields'>
        <input type="text" placeholder="Rua" />
      </div>
      
      <div className='multi-fields'>
        <input type="text" placeholder="CEP" />
        <input type="text" placeholder="Número" />
        <input type="text" placeholder="Telefone" />
      </div>
      
      <div className='multi-fields'>
        <input type="text" placeholder="Complemento" />
      </div>
    
    </div>
    <div className="place-order-right"> 
          <div className="cart-bottom-order">
                <div className='cart-total'>
                  <h2>Total do Carrinho </h2>
                <div className="cart-bottom-total">
                  <h3>R$ {food_list.reduce((total, item) => total + item.price * (cartItems[item._id] || 0), 0).toFixed(2)}</h3>
                </div>               

              </div>
              
               <div>
                
    </div>
   
    </div>
     <button  className='btn-cart-checkout '>Finalizar Pagamento</button>
    </div>

   </form>
  )
}

export default PlaceOrder

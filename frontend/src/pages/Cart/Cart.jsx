import React, { use, useContext } from 'react'
import './Cart.css'
import { StoreContext } from '../../components/context/StoreContext'
import { useNavigate } from 'react-router-dom';


const Cart = () => {

    const{cartItems, food_list, removeFromCart} = useContext(StoreContext);

    const navigate = useNavigate();


  return (
    <div className='cart'>
        <div className="cart-itens">
            <div className="cart-itens-title">
                
                <p>Itens</p>
                <p>Título</p>
                <p>Preço</p>
                <p>Quantidade</p>
                <p>Total</p>
                <p>Remover</p>
            </div>

            <br />
            <hr />

            {food_list.map((item,index) => {
                if(cartItems[item._id]>0){
                    return(
                         <div className='item-cart-container' key={index}>
                            <div key={index} className="cart-itens-item  cart-itens-title">
                                <img src={item.image} alt={item.name} className='cart-item-image' />
                                <p className='cart-item-name'>{item.name}</p>
                                <p className='cart-item-price'>R$ {item.price.toFixed(2)}</p>
                                <p className='cart-item-quantity'>{cartItems[item._id]}</p>
                                <p className='cart-item-total'>R$ {(item.price * cartItems[item._id]).toFixed(2)}</p>
                                <svg
                                onClick={() => removeFromCart(item._id)}
                                className="close-icon-cart"
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
                            <hr />

                         </div>
                    )
                }

            })}
              </div>

              <div className="cart-bottom">
                <div className='cart-total'>
                    <h2>Total do Carrinho </h2>
                <div className="cart-bottom-total">
                    <h3>R$ {food_list.reduce((total, item) => total + item.price * (cartItems[item._id] || 0), 0).toFixed(2)}</h3>
                </div>

                

              </div>
              
               <div>
                <button onClick={() => navigate('/order')} className='btn-cart-checkout '>Finalizar Pedido</button>
              </div>

                </div>
               

                
      
    </div>
  )
}

export default Cart

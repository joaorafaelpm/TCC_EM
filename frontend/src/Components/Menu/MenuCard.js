import styles from '../../../styles/Cardapio.module.css'

export default function MenuCard ({name , price , ingredients}) {
    return(
        <div className={styles.pizza_card}>
            <h4>{name}</h4>

            <p>
                <span>Preço:</span> R$ {price} 
            </p>

            <p>
                <span>Ingredientes:</span> {ingredients} 
            </p>

            <p> 
                Algumas tags para adicionarmais tarde exemplo: (tags de prato vegano, ou sem lactose, de peixe, borda recheada, doce e etc)
            </p>

        </div>
    )
}
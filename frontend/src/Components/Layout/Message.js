import { useEffect, useState } from 'react'
import styles from '../../../styles/Message.module.css'

export default function Message ({type , text}) {

    const [visible , setVisible] = useState(false)

    // A condição para satisfazer o useEffect é o texto, logo, se eu não receber eu não exibo nada
    useEffect(()=> {
        if (!text){
            setVisible(false)
            return ;
        }

        setVisible(true)

        const timer = setTimeout(()=> {
            setVisible(false)
        } , 3000)

        return () => clearTimeout(timer)
    } , [text])

    return (
        <>
            {visible && (
            <div className={`${styles.default} ${styles[type]}`}>
                <p>{text}</p>
            </div>
            )}
        </>
        
    )
}
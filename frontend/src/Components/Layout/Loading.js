import Logo from '../../../public/Loading.svg'
import styles from '../../../styles/Loading.module.css'

export default function Loading () {
    return (
        <div className={styles.loader_container}>
            <Logo 
                className={styles.loader}
                width={100}
                height={100}
            />
        </div>
        
    )
}
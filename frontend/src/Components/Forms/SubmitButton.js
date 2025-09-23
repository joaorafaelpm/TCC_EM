import styles from '../../../styles/SubmitButton.module.css'

export default function SubmitButton ({text , customClass}) {
    return (
    <div className={styles[customClass]}>
        <button className={styles.btn} > {text} </button>
    </div>
    )
     
}
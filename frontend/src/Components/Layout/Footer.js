import {FaGithub, FaInstagram } from 'react-icons/fa'

import styles from '../../../styles/Footer.module.css'

export default function Footer () {
    return (
        <footer className={styles.footer}>
            <ul className={styles.social_list}>
                <li >
                    <a href='https://www.instagram.com/joaorafaelpm/' target='_'><FaInstagram/></a>
                </li>

                <li >
                    <a href='https://github.com/jGoldenClover' target='_'><FaGithub/></a>
                </li>
            </ul>

            <p className={styles.copy_right}><span>Pizzaria Pendezza</span>  &copy;  Desde 2025 Matando a sua <span>Fome</span></p>

        </footer>

    )
}

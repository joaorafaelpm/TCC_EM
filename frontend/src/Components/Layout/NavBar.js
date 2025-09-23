import Link from "next/link";
import { getSession } from "../../actions";

import Logo from "../../../public/PizzaLogo_svg.svg";
import styles from "../../../styles/NavBar.module.css";
import Container from "./Container";
import LogoutForm from "../Forms/LogOutForm";


async function defineUser () {
    const session = await getSession();

    
    return session.isLogged
}

export default function NavBar() {

    

    return (
        <nav className={styles.navBar}>
        <Container>
            <Link href="/" className={styles.image_container}>
                <Logo width={120} height={120} />
            </Link>
            <ul className={styles.list}>
            <li className={styles.item}>
                <Link href="/"> Conheça Sobre </Link>
            </li>
            <li className={styles.item}>
                <Link href="/cardapio"> Cardápio </Link>
            </li>
            <li className={styles.item}>
                <Link href="/login"> Login </Link>
            </li>

            {defineUser && (
                <li className={styles.item}>
                    <LogoutForm />
                </li>
            )}
            </ul>
        </Container>
    </nav>
  );
}

import styles from '../app/page.module.css'

import Container from "./Layout/Container"
import Footer from "./Layout/Footer"
import NavBar from "./Layout/NavBar"

export default function MainLayout (props) {
    // const session = await getSession();


    return (
        <div className={styles.page}>
            <Container customClass="min-height">
                <NavBar />

                    {props.children}

                <Footer />
            </Container>
        </div>
)}


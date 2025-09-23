import styles from "../app/page.module.css";
import Message from "../Components/Layout/Message";

import MainLayout from "../Components/MainLayout";

export default function Home() {
  let message = "";

  return (
    <MainLayout>
      {message && <Message type="success" text={message} />}
      <div className={styles.container_content}>asdojasodasdkosko</div>
    </MainLayout>
  );
}

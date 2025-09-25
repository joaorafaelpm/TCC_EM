import { useState } from "react";
import { redirect } from "react-router-dom";

import uuid4 from "uuid4";
import supabase from "../../bd";


import MainLayout from "../Components/MainLayout";
import UserCadastroForm from "../Components/Users/UserCadastroForm";

export default function Cadastro() {
  const [user, setUser] = useState([]);

  function handleOnChange(e) {
    setUser({ ...user, [e.target.name]: e.target.value });
  }

  

  return (
    <MainLayout>
      <UserCadastroForm
        userData={user}
        handleChange={handleOnChange}
        handleSubmit={cadastrarUsuario}
      />
    </MainLayout>
  );
}

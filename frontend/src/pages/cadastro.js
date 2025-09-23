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

  async function cadastrarUsuario(user) {
    try {
      const user_id = uuid4();
      const { error } = await supabase.from("usuarios").insert({
        id: user_id,
        complete_name: user.complete_name,
        born_date: user.born_date,
        password: user.password,
        is_logged: "no",
        is_client: "yes",
        hierarchy: "client",
        email: user.email,
        cpf: user.cpf,
      });

      if (error) {
        console.log(`Erro ao cadastrar usuário , ${JSON.stringify(error)}`);
      } else {
        console.log("bora bill");
      }

      localStorage.setItem("userId", user_id);
      redirect('/' , {state:{message : 'Usuário Cadastrado com sucesso!'}})


    } catch (err) {
      console.log(`Erro ao cadastrar usuário , ${err}`);
    }
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

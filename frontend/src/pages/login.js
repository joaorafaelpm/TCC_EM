import { useEffect, useState } from "react";
import supabase from "../../bd";

import MainLayout from "../Components/MainLayout";
import UserLoginForm from "../Components/Users/UserLoginForm";
import Message from "../Components/Layout/Message";

export default function Login() {

  const [user , setUser] = useState([])
  

  function handleOnChange(e) {
    setUser({ ...user, [e.target.name]: e.target.value });
  }

  function logUser(user) {

  }

  return (
    <MainLayout>
      <Message text="kadsaksdkjasdjasjkdakj" type="success" />
      <UserLoginForm
        handleSubmit={logUser}
        userData={user}
        handleChange={handleOnChange}
      />
    </MainLayout>
  );
}

import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getAccessToken } from "../../../util/UserUtils";

const Callback = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const code = params.get("code");

    if (code) {
      getAccessToken(code).then((data) => {
        // Salva o token e redireciona pro início
        localStorage.setItem("access_token", data.access_token);
        localStorage.setItem("refresh_token", data.refresh_token);
        navigate("/");
      });
    }
  }, []);

  return <p>Autenticando...</p>;
};

export default Callback;
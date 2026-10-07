let _token = null; // em memória (preferido)

export const setToken = (token) => {
  _token = token;
  // sessionStorage como fallback para reload de página
  sessionStorage.setItem("pendezza_token", token);
};

export const getToken = () =>
  _token || sessionStorage.getItem("pendezza_token");

export const clearToken = () => {
  _token = null;
  sessionStorage.removeItem("pendezza_token");
};

// Verifica se o token ainda é válido pela data de expiração
export const isTokenExpired = () => {
  const token = getToken();
  if (!token) return true;
  try {
    const payload = JSON.parse(atob(token.split(".")[1]));
    return payload.exp * 1000 < Date.now();
  } catch {
    return true;
  }
};

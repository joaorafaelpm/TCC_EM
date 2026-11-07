import axios from "axios";

const API_URL = import.meta.env.VITE_API_URL;

const api = axios.create({
  baseURL: API_URL,
  timeout: 10000,
  withCredentials: true,
  headers: { "Content-Type": "application/json" },
});

let isRefreshing = false;
let queuedRequests = [];

const processQueue = (error) => {
  queuedRequests.forEach(({ resolve, reject }) =>
    error ? reject(error) : resolve(),
  );
  queuedRequests = [];
};

// Redireciona só uma vez e nunca se já estiver numa página de saída
const redirectOnce = (url, internalPath) => {
  if (window.location.pathname === internalPath) return;
  window.location.href = url;
};

api.interceptors.response.use(
  (response) => response,

  async (error) => {
    const status = error.response?.status;
    const original = error.config;

    if (status === 401 && original && !original._retry) {
      const url = original.url ?? "";
      const isAuthEndpoint = url.includes("/auth/") || url.includes("/oauth2/");

      if (isAuthEndpoint) return Promise.reject(error);

      if (isRefreshing) {
        return new Promise((resolve, reject) =>
          queuedRequests.push({ resolve, reject }),
        )
          .then(() => api(original))
          .catch((err) => Promise.reject(err));
      }

      original._retry = true;
      isRefreshing = true;

      try {
        // Agora vai para o BACKEND, não para o front
        const refreshRes = await fetch(`${API_URL}/auth/refresh`, {
          method: "POST",
          credentials: "include",
        });

        if (!refreshRes.ok) throw new Error("refresh_failed");

        processQueue(null);
        return api(original);
      } catch (refreshError) {
        processQueue(refreshError);
        // Login do Spring fica no BACKEND
        redirectOnce(
          `${API_URL}/oauth2/iniciar-login`,
          "/oauth2/iniciar-login",
        );
        return Promise.reject(refreshError);
      } finally {
        isRefreshing = false;
      }
    }

    if (status === 403) redirectOnce("/403", "/403");

    return Promise.reject(error);
  },
);

export default api;

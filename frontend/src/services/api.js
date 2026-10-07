import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  timeout: 10000,
  withCredentials: true, // ← equivalente a credentials:'include' no fetch
  headers: { "Content-Type": "application/json" },
});

// Controle de refresh — evita disparar múltiplos /auth/refresh simultâneos
let isRefreshing = false;
let queuedRequests = [];

const processQueue = (error) => {
  queuedRequests.forEach(({ resolve, reject }) =>
    error ? reject(error) : resolve(),
  );
  queuedRequests = [];
};

api.interceptors.response.use(
  (response) => response,

  async (error) => {
    const status = error.response?.status;
    const original = error.config;

    if (status === 401 && !original._retry) {
      const url = original.url ?? "";
      const isAuthEndpoint = url.includes("/auth/") || url.includes("/oauth2/");

      // Não tenta refresh em endpoints de auth — evita loop
      if (isAuthEndpoint) return Promise.reject(error);

      // Se já tem um refresh em andamento, enfileira a request
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
        const refreshRes = await fetch("/auth/refresh", {
          method: "POST",
          credentials: "include",
        });

        if (!refreshRes.ok) throw new Error("refresh_failed");

        processQueue(null);
        return api(original); // repete a request original com a sessão renovada
      } catch (refreshError) {
        processQueue(refreshError);
        window.location.href = "/oauth2/iniciar-login";
        return Promise.reject(refreshError);
      } finally {
        isRefreshing = false;
      }
    }

    if (status === 403) window.location.href = "/403";

    return Promise.reject(error);
  },
);

export default api;

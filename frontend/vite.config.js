  import { defineConfig, loadEnv } from 'vite'
  import react from '@vitejs/plugin-react'
  const env = loadEnv(process.env.NODE_ENV, process.cwd(), '')
  export default defineConfig({
    plugins: [react()],
    server: {
      proxy: {
        "/v1": env.VITE_API_URL,
        "/auth": env.VITE_API_URL,
        "/auth/refresh": env.VITE_API_URL,
        "/auth/logout": env.VITE_API_URL,
        "/oauth2": env.VITE_API_URL,
        "/redirect": env.VITE_API_URL,
        "/restaurants": env.VITE_API_URL ,
        "/restaurants/*/payment-methods": env.VITE_API_URL,
        "/payment-methods": env.VITE_API_URL,
        "/statistics": env.VITE_API_URL,
        "/products": env.VITE_API_URL,
        "/cities": env.VITE_API_URL,
        "/orders": env.VITE_API_URL,
        "/orders/*": env.VITE_API_URL,
        "/users": env.VITE_API_URL,
        "/users/*/restaurants": env.VITE_API_URL,
        "/restaurants/exists-responsible/*": env.VITE_API_URL,
      },
    },
  });
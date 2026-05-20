  import { defineConfig } from 'vite'
  import react from '@vitejs/plugin-react'

  export default defineConfig({
    plugins: [react()],
    server: {
      proxy: {
        "/v1": "http://localhost:80",
        "/auth": "http://localhost:80",
        "/auth/refresh": "http://localhost:80",
        "/oauth2": "http://localhost:80",
        "/redirect": "http://localhost:80",
        "/restaurants": "http://localhost:80",
        "/statistics": "http://localhost:80",
        "/products": "http://localhost:80",
        "/cities": "http://localhost:80",
        "/orders": "http://localhost:80",
        "/users": "http://localhost:80",
        "/users/*/restaurants": "http://localhost:80",
        "/restaurants/exists-responsible/*": "http://localhost:80",
      },
    },
  });
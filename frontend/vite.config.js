import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      "/auth": "http://localhost:80",
      "/oauth2": "http://localhost:80",
      "/redirect": "http://localhost:80",
      "/v1/restaurants": "http://localhost:80",
      "/v1/cities": "http://localhost:80",
      "/v1/orders": "http://localhost:80",
      "/v1/users": "http://localhost:80",
    },
  },
});
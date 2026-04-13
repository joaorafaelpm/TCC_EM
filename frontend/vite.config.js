import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/auth': 'http://localhost:80',
      '/oauth2': 'http://localhost:80',
      '/redirect': 'http://localhost:80',
    }
  }
})
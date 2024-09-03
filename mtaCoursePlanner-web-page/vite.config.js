import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  /*build: {
    sourcemap: true, // This enables source maps in production
  },*/
  plugins: [react()],
  server: {
    port:3000
  }
})

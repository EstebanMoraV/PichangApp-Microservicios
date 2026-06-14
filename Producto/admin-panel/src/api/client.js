import axios from 'axios'

// URL base del API Gateway (Railway). Configurable vía variable de entorno de Vite.
const baseURL =
  import.meta.env.VITE_API_URL ||
  'https://pichangapp-microservicios-production.up.railway.app'

const client = axios.create({
  baseURL,
  headers: { 'Content-Type': 'application/json' },
})

// Interceptor de petición: adjunta el JWT guardado en localStorage.
client.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// Interceptor de respuesta: si el token expira o no autoriza, cierra sesión.
client.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error?.response?.status
    if (status === 401) {
      localStorage.removeItem('token')
      if (window.location.pathname !== '/login') {
        window.location.href = '/login'
      }
    }
    return Promise.reject(error)
  }
)

export default client

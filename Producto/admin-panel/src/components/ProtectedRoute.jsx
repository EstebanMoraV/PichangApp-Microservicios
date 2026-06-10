import { Navigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import Navbar from './Navbar'

/**
 * Protege las rutas privadas: solo permite el acceso si hay un JWT válido con rol ADMIN.
 * Renderiza el layout con la barra lateral.
 */
export default function ProtectedRoute({ children }) {
  const { isAuthenticated, isAdmin } = useAuth()

  if (!isAuthenticated || !isAdmin) {
    return <Navigate to="/login" replace />
  }

  return (
    <div className="layout">
      <Navbar />
      <main className="content">{children}</main>
    </div>
  )
}

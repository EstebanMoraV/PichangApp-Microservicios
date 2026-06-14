import { NavLink, useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'

export default function Navbar() {
  const navigate = useNavigate()
  const { logout, adminEmail } = useAuth()

  const handleLogout = () => {
    logout()
    navigate('/login', { replace: true })
  }

  return (
    <aside className="sidebar">
      <div className="sidebar-brand">
        <span className="sidebar-logo">⚽</span>
        <div>
          <div className="sidebar-title">PichangApp</div>
          <div className="sidebar-subtitle">Administración</div>
        </div>
      </div>

      <nav className="sidebar-nav">
        <NavLink to="/dashboard" className="nav-item">
          Dashboard
        </NavLink>
        <NavLink to="/users" className="nav-item">
          Usuarios
        </NavLink>
        <NavLink to="/events" className="nav-item">
          Eventos
        </NavLink>
      </nav>

      <div className="sidebar-footer">
        {adminEmail && <div className="sidebar-email" title={adminEmail}>{adminEmail}</div>}
        <button className="btn btn-logout" onClick={handleLogout}>
          Cerrar sesión
        </button>
      </div>
    </aside>
  )
}

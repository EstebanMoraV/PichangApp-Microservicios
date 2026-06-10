import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'

export default function LoginPage() {
  const navigate = useNavigate()
  const { login } = useAuth()

  const [correo, setCorreo] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      await login(correo.trim(), password)
      navigate('/dashboard', { replace: true })
    } catch (err) {
      const status = err?.response?.status
      if (status === 401) {
        setError('Credenciales inválidas')
      } else if (status === 403) {
        setError('La cuenta no está verificada')
      } else {
        setError(err.message || 'No se pudo iniciar sesión')
      }
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="login-wrapper">
      <form className="login-card" onSubmit={handleSubmit}>
        <div className="login-logo">⚽</div>
        <h1 className="login-title">PichangApp</h1>
        <p className="login-subtitle">Panel de Administración</p>

        {error && <div className="alert alert-error">{error}</div>}

        <label className="field-label">Correo</label>
        <input
          type="email"
          className="field-input"
          value={correo}
          onChange={(e) => setCorreo(e.target.value)}
          placeholder="admin@pichangapp.cl"
          required
        />

        <label className="field-label">Contraseña</label>
        <input
          type="password"
          className="field-input"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          placeholder="••••••••"
          required
        />

        <button type="submit" className="btn btn-primary btn-block" disabled={loading}>
          {loading ? 'Ingresando…' : 'Ingresar'}
        </button>
      </form>
    </div>
  )
}

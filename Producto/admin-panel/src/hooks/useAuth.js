import { login as loginRequest } from '../api/authApi'

/**
 * Decodifica el payload de un JWT sin verificar la firma.
 * Devuelve un objeto con los claims (sub, correo, role, exp...).
 */
export function decodeJwt(token) {
  try {
    const payload = token.split('.')[1]
    const normalized = payload.replace(/-/g, '+').replace(/_/g, '/')
    const json = decodeURIComponent(
      atob(normalized)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    )
    return JSON.parse(json)
  } catch {
    return null
  }
}

/** Hook simple de autenticación basado en localStorage. */
export function useAuth() {
  const token = localStorage.getItem('token')
  const claims = token ? decodeJwt(token) : null
  const isAuthenticated = Boolean(token) && isTokenValid(claims)
  const isAdmin = claims?.role === 'ADMIN'

  /**
   * Inicia sesión. Lanza un error si las credenciales son inválidas
   * o si el usuario autenticado no es ADMIN.
   */
  async function login(correo, password) {
    const data = await loginRequest(correo, password)
    const jwt = data.token
    const decoded = decodeJwt(jwt)
    if (!decoded || decoded.role !== 'ADMIN') {
      throw new Error('Acceso denegado: esta cuenta no tiene privilegios de administrador')
    }
    localStorage.setItem('token', jwt)
    if (data.user?.correo) {
      localStorage.setItem('adminEmail', data.user.correo)
    }
    return decoded
  }

  function logout() {
    localStorage.removeItem('token')
    localStorage.removeItem('adminEmail')
  }

  return {
    token,
    claims,
    isAuthenticated,
    isAdmin,
    adminEmail: localStorage.getItem('adminEmail') || claims?.correo || '',
    login,
    logout,
  }
}

function isTokenValid(claims) {
  if (!claims) return false
  if (claims.exp && Date.now() >= claims.exp * 1000) return false
  return true
}

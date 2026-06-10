import client from './client'

/**
 * Autentica al administrador contra el users-service.
 * Devuelve el objeto JWTResponse { token, type, expiresIn, user }.
 */
export async function login(correo, password) {
  const { data } = await client.post('/api/v1/auth/login', { correo, password })
  return data
}

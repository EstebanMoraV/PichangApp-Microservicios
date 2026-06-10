import client from './client'

/** Lista todos los usuarios (rol ADMIN). */
export async function getUsers() {
  const { data } = await client.get('/api/v1/admin/users')
  return data
}

/** Detalle de un usuario por id (uso interno; no se muestra el id en la UI). */
export async function getUser(userId) {
  const { data } = await client.get(`/api/v1/admin/users/${userId}`)
  return data
}

/** Elimina un usuario por id. */
export async function deleteUser(userId) {
  await client.delete(`/api/v1/admin/users/${userId}`)
}

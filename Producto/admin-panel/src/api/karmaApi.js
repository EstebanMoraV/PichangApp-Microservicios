import client from './client'

/** Obtiene el karma (puntaje, categoría e historial) de un usuario por su id. */
export async function getKarma(userId) {
  const { data } = await client.get(`/api/v1/karma/${userId}`)
  return data
}

/** Ajuste manual del karma de un usuario (rol ADMIN). */
export async function adjustKarma(userId, newKarmaScore, reason) {
  const { data } = await client.put(`/api/v1/admin/karma/${userId}`, {
    newKarmaScore,
    reason,
  })
  return data
}

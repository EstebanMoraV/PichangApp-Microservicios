import client from './client'

/** Lista todos los eventos (activos, finalizados y cancelados). */
export async function getEvents() {
  const { data } = await client.get('/api/v1/admin/events')
  return data
}

/** Elimina (cancela) un evento por id. */
export async function deleteEvent(eventId) {
  await client.delete(`/api/v1/admin/events/${eventId}`)
}

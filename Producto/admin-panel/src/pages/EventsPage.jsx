import { useState } from 'react'
import { useQuery, useQueryClient, useMutation } from '@tanstack/react-query'
import { getEvents, deleteEvent } from '../api/eventsApi'
import ConfirmModal from '../components/ConfirmModal'

const STATUS_LABELS = {
  ACTIVE: 'Activo',
  FINISHED: 'Finalizado',
  CANCELLED: 'Cancelado',
}

export default function EventsPage() {
  const queryClient = useQueryClient()
  const { data: events, isLoading, isError } = useQuery({ queryKey: ['events'], queryFn: getEvents })

  const [filter, setFilter] = useState('ALL')
  const [toDelete, setToDelete] = useState(null)

  const deleteMutation = useMutation({
    mutationFn: (eventId) => deleteEvent(eventId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['events'] })
      setToDelete(null)
    },
  })

  const filtered = (events ?? []).filter((e) => filter === 'ALL' || e.status === filter)

  return (
    <div>
      <h1 className="page-title">Eventos</h1>
      <p className="page-subtitle">Gestión de todos los eventos</p>

      <div className="filter-bar">
        {['ALL', 'ACTIVE', 'FINISHED', 'CANCELLED'].map((s) => (
          <button
            key={s}
            className={`chip ${filter === s ? 'chip-active' : ''}`}
            onClick={() => setFilter(s)}
          >
            {s === 'ALL' ? 'Todos' : STATUS_LABELS[s]}
          </button>
        ))}
      </div>

      {isLoading && <p className="muted">Cargando eventos…</p>}
      {isError && <div className="alert alert-error">No se pudieron cargar los eventos</div>}

      {!isLoading && !isError && (
        <div className="table-wrapper">
          <table className="data-table">
            <thead>
              <tr>
                <th>Nombre</th>
                <th>Deporte</th>
                <th>Organizador</th>
                <th>Fecha</th>
                <th>Estado</th>
                <th>Jugadores</th>
                <th className="col-actions">Acciones</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((e) => (
                <tr key={e.id}>
                  <td>{e.name}</td>
                  <td>{e.sport}</td>
                  <td>{e.organizerEmail || '—'}</td>
                  <td>{formatDate(e.eventDate)}</td>
                  <td>
                    <span className={`badge badge-${e.status?.toLowerCase()}`}>
                      {STATUS_LABELS[e.status] || e.status}
                    </span>
                  </td>
                  <td>{e.currentPlayers} / {e.maxPlayers}</td>
                  <td className="col-actions">
                    <button
                      className="btn btn-danger btn-sm"
                      disabled={e.status === 'CANCELLED'}
                      title={e.status === 'CANCELLED' ? 'El evento ya está cancelado' : 'Eliminar'}
                      onClick={() => setToDelete(e)}
                    >
                      Eliminar
                    </button>
                  </td>
                </tr>
              ))}
              {filtered.length === 0 && (
                <tr>
                  <td colSpan={7} className="muted center">No hay eventos para este filtro.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}

      <ConfirmModal
        open={Boolean(toDelete)}
        title="Eliminar evento"
        message={
          toDelete
            ? `¿Seguro que deseas eliminar "${toDelete.name}"? Los participantes recibirán sus puntos de karma y una notificación.`
            : ''
        }
        confirmText="Eliminar"
        loading={deleteMutation.isPending}
        onCancel={() => setToDelete(null)}
        onConfirm={() => deleteMutation.mutate(toDelete.id)}
      />
    </div>
  )
}

function formatDate(value) {
  if (!value) return '—'
  try {
    return new Date(value).toLocaleString('es-CL')
  } catch {
    return value
  }
}

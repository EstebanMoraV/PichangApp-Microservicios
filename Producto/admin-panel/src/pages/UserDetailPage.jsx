import { useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useQuery, useQueryClient, useMutation } from '@tanstack/react-query'
import { getUser, deleteUser } from '../api/usersApi'
import { getKarma, adjustKarma } from '../api/karmaApi'
import ConfirmModal from '../components/ConfirmModal'

export default function UserDetailPage() {
  const { userId } = useParams()
  const navigate = useNavigate()
  const queryClient = useQueryClient()

  const userQuery = useQuery({ queryKey: ['user', userId], queryFn: () => getUser(userId) })
  const karmaQuery = useQuery({ queryKey: ['karma', userId], queryFn: () => getKarma(userId), retry: false })

  const [newScore, setNewScore] = useState('')
  const [reason, setReason] = useState('')
  const [feedback, setFeedback] = useState(null)
  const [showDelete, setShowDelete] = useState(false)

  const adjustMutation = useMutation({
    mutationFn: () => adjustKarma(userId, Number(newScore), reason.trim()),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['karma', userId] })
      setFeedback({ type: 'success', text: 'Karma actualizado correctamente' })
      setNewScore('')
      setReason('')
    },
    onError: () => setFeedback({ type: 'error', text: 'No se pudo actualizar el karma' }),
  })

  const deleteMutation = useMutation({
    mutationFn: () => deleteUser(userId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['users'] })
      navigate('/users', { replace: true })
    },
  })

  const submitAdjust = (e) => {
    e.preventDefault()
    setFeedback(null)
    if (newScore === '' || Number(newScore) < 0) {
      setFeedback({ type: 'error', text: 'Ingresa un puntaje válido (mayor o igual a 0)' })
      return
    }
    if (!reason.trim()) {
      setFeedback({ type: 'error', text: 'Ingresa una razón para el ajuste' })
      return
    }
    adjustMutation.mutate()
  }

  const user = userQuery.data
  const karma = karmaQuery.data

  return (
    <div>
      <button className="btn btn-link back-link" onClick={() => navigate('/users')}>
        ← Volver a usuarios
      </button>

      <h1 className="page-title">Detalle de usuario</h1>

      {userQuery.isLoading && <p className="muted">Cargando…</p>}
      {userQuery.isError && <div className="alert alert-error">No se pudo cargar el usuario</div>}

      {user && (
        <div className="detail-grid">
          <section className="panel">
            <h2 className="panel-title">Información</h2>
            <InfoRow label="Nombre" value={`${user.nombre} ${user.apellido}`} />
            <InfoRow label="Correo" value={user.correo} />
            <InfoRow label="Rol" value={user.role} />
            <InfoRow label="Estado" value={user.enabled ? 'Habilitado' : 'Deshabilitado'} />
          </section>

          <section className="panel">
            <h2 className="panel-title">Karma</h2>
            {karmaQuery.isLoading && <p className="muted">Cargando karma…</p>}
            {karmaQuery.isError && <p className="muted">Sin información de karma todavía.</p>}
            {karma && (
              <>
                <div className="karma-score">
                  <span className="karma-number">{karma.karmaScore}</span>
                  <span className="karma-category">{karma.category}</span>
                </div>

                <form className="adjust-form" onSubmit={submitAdjust}>
                  <h3 className="subsection-title">Modificar karma</h3>
                  <label className="field-label">Nuevo puntaje</label>
                  <input
                    type="number"
                    className="field-input"
                    value={newScore}
                    min="0"
                    onChange={(e) => setNewScore(e.target.value)}
                    placeholder="Ej: 75"
                  />
                  <label className="field-label">Razón</label>
                  <input
                    type="text"
                    className="field-input"
                    value={reason}
                    onChange={(e) => setReason(e.target.value)}
                    placeholder="Ej: Ajuste manual por administrador"
                  />
                  {feedback && (
                    <div className={`alert ${feedback.type === 'success' ? 'alert-success' : 'alert-error'}`}>
                      {feedback.text}
                    </div>
                  )}
                  <button type="submit" className="btn btn-primary" disabled={adjustMutation.isPending}>
                    {adjustMutation.isPending ? 'Guardando…' : 'Guardar ajuste'}
                  </button>
                </form>
              </>
            )}
          </section>

          <section className="panel panel-wide">
            <h2 className="panel-title">Historial de karma</h2>
            {karma?.history?.length ? (
              <table className="data-table">
                <thead>
                  <tr>
                    <th>Fecha</th>
                    <th>Cambio</th>
                    <th>Motivo</th>
                  </tr>
                </thead>
                <tbody>
                  {karma.history.map((h, i) => (
                    <tr key={i}>
                      <td>{formatDate(h.createdAt)}</td>
                      <td className={h.amount >= 0 ? 'text-positive' : 'text-negative'}>
                        {h.amount >= 0 ? `+${h.amount}` : h.amount}
                      </td>
                      <td>{h.reason}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            ) : (
              <p className="muted">Sin movimientos de karma.</p>
            )}
          </section>

          <section className="panel panel-wide danger-zone">
            <h2 className="panel-title">Zona peligrosa</h2>
            <p className="muted">Eliminar al usuario es una acción permanente.</p>
            <button
              className="btn btn-danger"
              disabled={user.role === 'ADMIN'}
              title={user.role === 'ADMIN' ? 'No se puede eliminar a un administrador' : 'Eliminar usuario'}
              onClick={() => setShowDelete(true)}
            >
              Eliminar usuario
            </button>
          </section>
        </div>
      )}

      <ConfirmModal
        open={showDelete}
        title="Eliminar usuario"
        message={user ? `¿Seguro que deseas eliminar a ${user.correo}?` : ''}
        confirmText="Eliminar"
        loading={deleteMutation.isPending}
        onCancel={() => setShowDelete(false)}
        onConfirm={() => deleteMutation.mutate()}
      />
    </div>
  )
}

function InfoRow({ label, value }) {
  return (
    <div className="info-row">
      <span className="info-label">{label}</span>
      <span className="info-value">{value}</span>
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

import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useQuery, useQueryClient, useMutation } from '@tanstack/react-query'
import { getUsers, deleteUser } from '../api/usersApi'
import { getKarma } from '../api/karmaApi'
import ConfirmModal from '../components/ConfirmModal'

export default function UsersPage() {
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const { data: users, isLoading, isError } = useQuery({ queryKey: ['users'], queryFn: getUsers })

  const [toDelete, setToDelete] = useState(null)

  const deleteMutation = useMutation({
    mutationFn: (userId) => deleteUser(userId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['users'] })
      setToDelete(null)
    },
  })

  return (
    <div>
      <h1 className="page-title">Usuarios</h1>
      <p className="page-subtitle">Gestión de usuarios registrados</p>

      {isLoading && <p className="muted">Cargando usuarios…</p>}
      {isError && <div className="alert alert-error">No se pudieron cargar los usuarios</div>}

      {!isLoading && !isError && (
        <div className="table-wrapper">
          <table className="data-table">
            <thead>
              <tr>
                <th>Nombre</th>
                <th>Apellido</th>
                <th>Correo</th>
                <th>Karma</th>
                <th>Categoría</th>
                <th>Estado</th>
                <th>Rol</th>
                <th className="col-actions">Acciones</th>
              </tr>
            </thead>
            <tbody>
              {users.map((u) => (
                <tr key={u.correo}>
                  <td>{u.nombre}</td>
                  <td>{u.apellido}</td>
                  <td>{u.correo}</td>
                  <KarmaCells userId={u.id} />
                  <td>
                    <span className={`badge ${u.enabled ? 'badge-on' : 'badge-off'}`}>
                      {u.enabled ? 'Habilitado' : 'Deshabilitado'}
                    </span>
                  </td>
                  <td>
                    <span className={`badge ${u.role === 'ADMIN' ? 'badge-admin' : 'badge-user'}`}>
                      {u.role}
                    </span>
                  </td>
                  <td className="col-actions">
                    <button className="btn btn-link" onClick={() => navigate(`/users/${u.id}`)}>
                      Ver detalle
                    </button>
                    <button
                      className="btn btn-danger btn-sm"
                      disabled={u.role === 'ADMIN'}
                      title={u.role === 'ADMIN' ? 'No se puede eliminar a un administrador' : 'Eliminar'}
                      onClick={() => setToDelete(u)}
                    >
                      Eliminar
                    </button>
                  </td>
                </tr>
              ))}
              {users.length === 0 && (
                <tr>
                  <td colSpan={8} className="muted center">No hay usuarios registrados.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}

      <ConfirmModal
        open={Boolean(toDelete)}
        title="Eliminar usuario"
        message={toDelete ? `¿Seguro que deseas eliminar a ${toDelete.correo}? Esta acción no se puede deshacer.` : ''}
        confirmText="Eliminar"
        loading={deleteMutation.isPending}
        onCancel={() => setToDelete(null)}
        onConfirm={() => deleteMutation.mutate(toDelete.id)}
      />
    </div>
  )
}

/** Celdas de karma y categoría; se cargan por usuario de forma independiente. */
function KarmaCells({ userId }) {
  const { data, isLoading, isError } = useQuery({
    queryKey: ['karma', userId],
    queryFn: () => getKarma(userId),
    retry: false,
  })

  if (isLoading) {
    return (
      <>
        <td className="muted">…</td>
        <td className="muted">…</td>
      </>
    )
  }
  if (isError || !data) {
    return (
      <>
        <td className="muted">—</td>
        <td className="muted">—</td>
      </>
    )
  }
  return (
    <>
      <td><strong>{data.karmaScore}</strong></td>
      <td>{data.category}</td>
    </>
  )
}

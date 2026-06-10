import { useQuery } from '@tanstack/react-query'
import { getUsers } from '../api/usersApi'
import { getEvents } from '../api/eventsApi'

export default function DashboardPage() {
  const usersQuery = useQuery({ queryKey: ['users'], queryFn: getUsers })
  const eventsQuery = useQuery({ queryKey: ['events'], queryFn: getEvents })

  const totalUsers = usersQuery.data?.length ?? 0
  const events = eventsQuery.data ?? []
  const activos = events.filter((e) => e.status === 'ACTIVE').length
  const finalizados = events.filter((e) => e.status === 'FINISHED').length
  const cancelados = events.filter((e) => e.status === 'CANCELLED').length

  const loading = usersQuery.isLoading || eventsQuery.isLoading
  const error = usersQuery.isError || eventsQuery.isError

  return (
    <div>
      <h1 className="page-title">Dashboard</h1>
      <p className="page-subtitle">Resumen general de la plataforma</p>

      {loading && <p className="muted">Cargando estadísticas…</p>}
      {error && <div className="alert alert-error">No se pudieron cargar las estadísticas</div>}

      {!loading && !error && (
        <div className="cards-grid">
          <StatCard label="Usuarios registrados" value={totalUsers} accent="#2E7D32" icon="👥" />
          <StatCard label="Eventos activos" value={activos} accent="#1565C0" icon="🟢" />
          <StatCard label="Eventos finalizados" value={finalizados} accent="#6A1B9A" icon="🏁" />
          <StatCard label="Eventos cancelados" value={cancelados} accent="#C62828" icon="🚫" />
        </div>
      )}
    </div>
  )
}

function StatCard({ label, value, accent, icon }) {
  return (
    <div className="stat-card" style={{ borderTopColor: accent }}>
      <div className="stat-icon">{icon}</div>
      <div className="stat-value">{value}</div>
      <div className="stat-label">{label}</div>
    </div>
  )
}

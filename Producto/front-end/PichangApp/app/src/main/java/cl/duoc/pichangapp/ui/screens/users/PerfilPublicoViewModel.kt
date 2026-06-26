package cl.duoc.pichangapp.ui.screens.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.pichangapp.data.model.KarmaHistoryDto
import cl.duoc.pichangapp.data.model.PerfilPublicoDto
import cl.duoc.pichangapp.data.repository.EventRepository
import cl.duoc.pichangapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Patrón que detecta "evento: 42" o "evento: 42)" al final del reason del backend
private val EVENT_ID_REGEX = Regex("""evento:\s*(\d+)\)?$""")

data class PerfilPublicoUiState(
    val isLoading: Boolean = true,
    val perfil: PerfilPublicoDto? = null,
    val error: String? = null
)

@HiltViewModel
class PerfilPublicoViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PerfilPublicoUiState())
    val state: StateFlow<PerfilPublicoUiState> = _state.asStateFlow()

    private var loaded = false

    /**
     * El backend no expone perfil-público por nombre/apellido (solo por correo). Reutilizamos
     * la búsqueda para localizar al usuario, y luego pedimos su perfil individual por correo,
     * que incluye el historial de karma (el backend lo entrega solo si historialVisible == true).
     */
    fun load(nombre: String, apellido: String) {
        if (loaded) return
        loaded = true
        viewModelScope.launch {
            _state.value = PerfilPublicoUiState(isLoading = true)
            try {
                val resultados = userRepository.buscarUsuarios(nombre.ifBlank { apellido })
                val match = resultados.firstOrNull {
                    it.nombre?.equals(nombre, ignoreCase = true) == true &&
                        it.apellido?.equals(apellido, ignoreCase = true) == true
                } ?: resultados.firstOrNull { it.nombre?.equals(nombre, ignoreCase = true) == true }
                ?: resultados.firstOrNull()

                if (match != null) {
                    // Enriquecer con el perfil individual (incluye historial gated por el backend)
                    val completo = try {
                        match.correo?.let { userRepository.getPerfilPublico(it) } ?: match
                    } catch (e: Exception) {
                        match
                    }
                    val historialResuelto = resolverNombresDeEvento(completo.history)
                    _state.value = PerfilPublicoUiState(
                        isLoading = false,
                        perfil = completo.copy(history = historialResuelto)
                    )
                } else {
                    _state.value = PerfilPublicoUiState(isLoading = false, error = "No se encontró el perfil")
                }
            } catch (e: Exception) {
                _state.value = PerfilPublicoUiState(isLoading = false, error = "No se pudo cargar el perfil")
            }
        }
    }

    // Reemplaza "evento: {id}" por el nombre real del evento en cada reason (igual que KarmaViewModel)
    private suspend fun resolverNombresDeEvento(history: List<KarmaHistoryDto>): List<KarmaHistoryDto> {
        val idsUnicos = history.mapNotNull { item ->
            EVENT_ID_REGEX.find(item.reason)?.groupValues?.get(1)?.toIntOrNull()
        }.toSet()

        if (idsUnicos.isEmpty()) return history

        val idANombre = mutableMapOf<Int, String>()
        for (id in idsUnicos) {
            try {
                idANombre[id] = eventRepository.getEventById(id).name
            } catch (e: Exception) { /* ignorar IDs que no resuelven */ }
        }

        return history.map { item ->
            val match = EVENT_ID_REGEX.find(item.reason)
            val nombre = match?.groupValues?.get(1)?.toIntOrNull()?.let { idANombre[it] }
            if (nombre != null) item.copy(reason = item.reason.replace(match.value, "evento: $nombre"))
            else item
        }
    }
}

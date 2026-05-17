package cl.duoc.pichangapp.ui.screens.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.pichangapp.data.model.CreateEventRequest
import cl.duoc.pichangapp.data.model.EventDto
import cl.duoc.pichangapp.data.repository.EventRepository
import cl.duoc.pichangapp.core.datastore.TokenDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val tokenDataStore: TokenDataStore
) : ViewModel() {
    private val _events = MutableStateFlow<List<EventDto>>(emptyList())
    val events: StateFlow<List<EventDto>> = _events.asStateFlow()

    val currentUserId = tokenDataStore.userIdFlow

    private val _myEvents = MutableStateFlow<List<EventDto>>(emptyList())
    val myEvents: StateFlow<List<EventDto>> = _myEvents.asStateFlow()

    private val _organizingEvents = MutableStateFlow<List<EventDto>>(emptyList())
    val organizingEvents: StateFlow<List<EventDto>> = _organizingEvents.asStateFlow()

    private val _eventDetail = MutableStateFlow<EventDto?>(null)
    val eventDetail: StateFlow<EventDto?> = _eventDetail.asStateFlow()

    private val _registrations = MutableStateFlow<List<cl.duoc.pichangapp.data.model.EventRegistrationDto>>(emptyList())
    val registrations: StateFlow<List<cl.duoc.pichangapp.data.model.EventRegistrationDto>> = _registrations.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var lastLat: Double? = null
    private var lastLng: Double? = null

    fun refresh() {
        lastLat?.let { lat -> lastLng?.let { lng -> loadEvents(lat, lng) } }
        loadMyEvents()
        loadOrganizingEvents()
    }

    fun loadEvents(lat: Double, lng: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                lastLat = lat
                lastLng = lng
                _events.value = eventRepository.getEvents(lat, lng).filter { it.status == "ACTIVE" }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMyEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _myEvents.value = eventRepository.getMyEvents().filter { it.status == "ACTIVE" }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadOrganizingEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _organizingEvents.value = eventRepository.getOrganizingEvents().filter { it.status == "ACTIVE" }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun joinEvent(eventId: Int): Result<Unit> {
        return try {
            val response = eventRepository.joinEvent(eventId)
            if (response.isSuccessful) {
                refresh()
                loadEventDetail(eventId)
                Result.success(Unit)
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string(), "Error al unirse al evento")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Error de conexión"))
        }
    }
    
    suspend fun leaveEvent(eventId: Int): Result<Unit> {
        return try {
            val response = eventRepository.leaveEvent(eventId)
            if (response.isSuccessful) {
                refresh()
                loadEventDetail(eventId)
                Result.success(Unit)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createEvent(request: CreateEventRequest): Result<Unit> {
        return try {
            eventRepository.createEvent(request)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun finishEvent(eventId: Int): Result<Unit> {
        return try {
            val response = eventRepository.finishEvent(eventId)
            if (response.isSuccessful) {
                refresh()
                Result.success(Unit)
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string(), "Error al finalizar evento")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Error de conexión"))
        }
    }

    fun loadEventDetail(eventId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _eventDetail.value = eventRepository.getEventById(eventId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadRegistrations(eventId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _registrations.value = eventRepository.getRegistrations(eventId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun deleteEvent(eventId: Int): Result<Unit> {
        return try {
            val response = eventRepository.deleteEvent(eventId)
            if (response.isSuccessful) {
                refresh()
                Result.success(Unit)
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string(), "Error al eliminar el evento")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Error de conexión"))
        }
    }

    suspend fun markAttendance(eventId: Int, userId: Int, attended: Boolean): Result<Unit> {
        return try {
            val response = eventRepository.markAttendance(eventId, userId, attended)
            if (response.isSuccessful) {
                // Remover el registro de la lista local inmediatamente
                _registrations.value = _registrations.value.filter { it.userId != userId }
                Result.success(Unit)
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string(), "Error al registrar asistencia")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Error de conexión"))
        }
    }

    private fun parseErrorMessage(errorBody: String?, fallback: String): String {
        if (errorBody.isNullOrBlank()) return fallback
        return try {
            val json = JSONObject(errorBody)
            json.optString("message").takeIf { it.isNotEmpty() }
                ?: json.optString("error").takeIf { it.isNotEmpty() }
                ?: json.optString("detail").takeIf { it.isNotEmpty() }
                ?: fallback
        } catch (e: Exception) {
            // Si no es JSON, devolver el texto plano pero evitar mensajes técnicos
            if (errorBody.length < 200) errorBody else fallback
        }
    }

    suspend fun getUserName(userId: Int): String {
        return try {
            val user = eventRepository.getUserById(userId)
            "${user.nombre} ${user.apellido}"
        } catch (e: Exception) {
            "Usuario #$userId"
        }
    }
}

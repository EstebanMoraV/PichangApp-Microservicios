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

    fun loadEvents(lat: Double, lng: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _events.value = eventRepository.getEvents(lat, lng)
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
                _myEvents.value = eventRepository.getMyEvents()
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
                _organizingEvents.value = eventRepository.getOrganizingEvents()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun joinEvent(eventId: Int): Result<Unit> {
        return try {
            eventRepository.joinEvent(eventId)
            loadMyEvents()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun leaveEvent(eventId: Int): Result<Unit> {
        return try {
            val response = eventRepository.leaveEvent(eventId)
            if (response.isSuccessful) {
                loadEventDetail(eventId)
                loadMyEvents()
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
            eventRepository.finishEvent(eventId)
            loadOrganizingEvents()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
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

    suspend fun checkIn(eventId: Int, lat: Double, lng: Double): Result<Unit> {
        return try {
            eventRepository.checkIn(eventId, lat, lng)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun markAttendance(eventId: Int, userId: Int, attended: Boolean): Result<Unit> {
        return try {
            eventRepository.markAttendance(eventId, userId, attended)
            loadRegistrations(eventId) // Refresh list
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
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

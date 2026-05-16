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

    fun joinEvent(eventId: Int) {
        viewModelScope.launch {
            try {
                eventRepository.joinEvent(eventId)
                // Reload list or my events
                loadMyEvents()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    
    fun createEvent(request: CreateEventRequest) {
        viewModelScope.launch {
            try {
                eventRepository.createEvent(request)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    
    fun finishEvent(eventId: Int) {
        viewModelScope.launch {
            try {
                eventRepository.finishEvent(eventId)
                loadOrganizingEvents()
            } catch (e: Exception) {
                _error.value = e.message
            }
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

    fun checkIn(eventId: Int, lat: Double, lng: Double) {
        viewModelScope.launch {
            try {
                eventRepository.checkIn(eventId, lat, lng)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun markAttendance(eventId: Int, userId: Int, attended: Boolean) {
        viewModelScope.launch {
            try {
                eventRepository.markAttendance(eventId, userId, attended)
                loadRegistrations(eventId) // Refresh list
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}

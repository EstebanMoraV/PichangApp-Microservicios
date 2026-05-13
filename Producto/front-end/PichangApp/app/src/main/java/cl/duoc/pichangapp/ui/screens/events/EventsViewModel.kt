package cl.duoc.pichangapp.ui.screens.events

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class EventsUiState(
    val selectedTab: Int = 0 // 0: Mis Eventos, 1: Crear Partido
)

@HiltViewModel
class EventsViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(EventsUiState())
    val state: StateFlow<EventsUiState> = _state.asStateFlow()

    fun setTab(index: Int) {
        _state.value = _state.value.copy(selectedTab = index)
    }
}

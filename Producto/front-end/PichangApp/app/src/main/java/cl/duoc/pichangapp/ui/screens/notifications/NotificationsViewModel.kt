package cl.duoc.pichangapp.ui.screens.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.pichangapp.core.datastore.TokenDataStore
import cl.duoc.pichangapp.core.util.Result
import cl.duoc.pichangapp.data.model.NotificationDto
import cl.duoc.pichangapp.domain.usecase.GetNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationsUiState(
    val isLoading: Boolean = true,
    val notifications: List<NotificationDto> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val tokenDataStore: TokenDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationsUiState())
    val state: StateFlow<NotificationsUiState> = _state.asStateFlow()

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val userId = tokenDataStore.userIdFlow.firstOrNull() ?: "1"

            getNotificationsUseCase(userId).collect { result ->
                when (result) {
                    is Result.Success -> _state.value = _state.value.copy(notifications = result.data, isLoading = false, error = null)
                    is Result.Error -> _state.value = _state.value.copy(error = result.message, isLoading = false)
                    is Result.Loading -> _state.value = _state.value.copy(isLoading = true)
                }
            }
        }
    }
}

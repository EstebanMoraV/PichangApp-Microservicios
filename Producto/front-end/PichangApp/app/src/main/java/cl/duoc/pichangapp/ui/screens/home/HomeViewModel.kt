package cl.duoc.pichangapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.pichangapp.core.datastore.TokenDataStore
import cl.duoc.pichangapp.core.util.Result
import cl.duoc.pichangapp.data.model.KarmaDto
import cl.duoc.pichangapp.data.model.UserDto
import cl.duoc.pichangapp.domain.usecase.GetKarmaUseCase
import cl.duoc.pichangapp.domain.usecase.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val user: UserDto? = null,
    val karma: KarmaDto? = null,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getKarmaUseCase: GetKarmaUseCase,
    private val tokenDataStore: TokenDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            // Retrieve userId, defaulting to "1" for testing based on backend specs
            val userId = tokenDataStore.userIdFlow.firstOrNull() ?: "1"
            
            // Load User
            getUserProfileUseCase(userId).collect { result ->
                when (result) {
                    is Result.Success -> _state.value = _state.value.copy(user = result.data, isLoading = false)
                    is Result.Error -> _state.value = _state.value.copy(error = result.message, isLoading = false)
                    is Result.Loading -> {}
                }
            }

            // Load Karma
            getKarmaUseCase(userId).collect { result ->
                if (result is Result.Success) {
                    _state.value = _state.value.copy(karma = result.data)
                }
            }
        }
    }
}

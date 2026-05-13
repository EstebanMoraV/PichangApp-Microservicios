package cl.duoc.pichangapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.pichangapp.core.datastore.TokenDataStore
import cl.duoc.pichangapp.core.util.JwtUtils
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
            
            // Prioridad 1: Extraer userId directamente del JWT (claim "sub")
            // Prioridad 2: Leer userId guardado en DataStore
            val token = tokenDataStore.tokenFlow.firstOrNull()
            val userId = if (!token.isNullOrEmpty()) {
                JwtUtils.extractUserId(token)
            } else {
                null
            } ?: tokenDataStore.userIdFlow.firstOrNull()
            
            if (userId.isNullOrEmpty()) {
                _state.value = _state.value.copy(
                    error = "No se pudo obtener el ID del usuario. Inicia sesión nuevamente.",
                    isLoading = false
                )
                return@launch
            }
            
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
                    val score = result.data.puntaje ?: 0
                    val calculatedCategory = when {
                        score >= 80 -> "Excelente"
                        score >= 60 -> "Bueno"
                        score >= 40 -> "Regular"
                        else -> "Bajo"
                    }
                    val updatedKarma = result.data.copy(categoria = calculatedCategory)
                    _state.value = _state.value.copy(karma = updatedKarma)
                }
            }
        }
    }
}

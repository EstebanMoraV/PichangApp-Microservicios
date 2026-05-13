package cl.duoc.pichangapp.ui.screens.karma

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.pichangapp.core.datastore.TokenDataStore
import cl.duoc.pichangapp.core.util.JwtUtils
import cl.duoc.pichangapp.core.util.Result
import cl.duoc.pichangapp.data.model.KarmaDto
import cl.duoc.pichangapp.domain.usecase.GetKarmaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

data class KarmaUiState(
    val isLoading: Boolean = true,
    val karma: KarmaDto? = null,
    val error: String? = null
)

@HiltViewModel
class KarmaViewModel @Inject constructor(
    private val getKarmaUseCase: GetKarmaUseCase,
    private val tokenDataStore: TokenDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(KarmaUiState())
    val state: StateFlow<KarmaUiState> = _state.asStateFlow()

    init {
        loadKarma()
    }

    fun loadKarma() {
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
                    error = "No se pudo obtener el ID del usuario",
                    isLoading = false
                )
                return@launch
            }

            getKarmaUseCase(userId).collect { result ->
                when (result) {
                    is Result.Success -> {
                        val score = result.data.puntaje ?: 0
                        val calculatedCategory = when {
                            score >= 80 -> "Excelente"
                            score >= 60 -> "Bueno"
                            score >= 40 -> "Regular"
                            else -> "Bajo"
                        }
                        val updatedKarma = result.data.copy(categoria = calculatedCategory)
                        _state.value = _state.value.copy(karma = updatedKarma, isLoading = false)
                    }
                    is Result.Error -> _state.value = _state.value.copy(error = result.message, isLoading = false)
                    is Result.Loading -> {}
                }
            }
        }
    }
}

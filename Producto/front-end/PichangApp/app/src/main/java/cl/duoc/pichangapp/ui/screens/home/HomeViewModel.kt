package cl.duoc.pichangapp.ui.screens.home

import android.util.Log
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

private const val TAG = "PICHANGAPP_DEBUG"
private const val BASE_URL = "https://pichangapp-microservicios-production.up.railway.app"

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

    // Guard: evita múltiples llamadas al backend si los datos ya se cargaron
    private var dataLoaded = false

    init {
        loadData()
    }

    fun refresh() {
        dataLoaded = false
        loadData()
    }

    private fun loadData() {
        // Si ya cargamos datos exitosamente, no volvemos a hacer las llamadas
        if (dataLoaded) {
            Log.d(TAG, "[HomeViewModel] Datos ya cargados, omitiendo llamada al backend.")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            // --- DEBUG: Token y userId ---
            val token = tokenDataStore.tokenFlow.firstOrNull()
            Log.d(TAG, "[HomeViewModel] Token: ${if (token.isNullOrEmpty()) "NULL ⚠️" else "OK (${token.length} chars)"}")

            val userIdFromJwt = if (!token.isNullOrEmpty()) JwtUtils.extractUserId(token) else null
            val userIdFromStore = tokenDataStore.userIdFlow.firstOrNull()
            Log.d(TAG, "[HomeViewModel] userId JWT='${userIdFromJwt}' DataStore='${userIdFromStore}'")

            val userId = userIdFromJwt ?: userIdFromStore
            Log.d(TAG, "[HomeViewModel] userId FINAL: ${userId ?: "NULL → abortando ⛔"}")

            if (userId.isNullOrEmpty()) {
                _state.value = _state.value.copy(
                    error = "No se pudo obtener el ID del usuario. Inicia sesión nuevamente.",
                    isLoading = false
                )
                return@launch
            }

            // --- Load User ---
            Log.d(TAG, "[HomeViewModel] GET $BASE_URL/api/v1/users/$userId")
            getUserProfileUseCase(userId).collect { result ->
                when (result) {
                    is Result.Success -> {
                        Log.d(TAG, "[HomeViewModel] Usuario OK → nombre='${result.data.nombre}'")
                        _state.value = _state.value.copy(user = result.data, isLoading = false)
                    }
                    is Result.Error -> {
                        Log.e(TAG, "[HomeViewModel] Error usuario: ${result.message}")
                        _state.value = _state.value.copy(error = result.message, isLoading = false)
                    }
                    is Result.Loading -> {}
                }
            }

            // --- Load Karma ---
            Log.d(TAG, "[HomeViewModel] GET $BASE_URL/api/v1/karma/$userId")
            getKarmaUseCase(userId).collect { result ->
                when (result) {
                    is Result.Success -> {
                        val score = result.data.puntaje
                        // El backend ya devuelve la categoría, pero la recalculamos por consistencia
                        val calculatedCategory = when {
                            score >= 80 -> "Excelente"
                            score >= 60 -> "Bueno"
                            score >= 40 -> "Regular"
                            else -> "Bajo"
                        }
                        Log.d(TAG, "[HomeViewModel] Karma OK → puntaje=$score categoría='$calculatedCategory'")
                        val updatedKarma = result.data.copy(categoria = calculatedCategory)
                        _state.value = _state.value.copy(karma = updatedKarma)
                        dataLoaded = true // Marcar datos como cargados exitosamente
                    }
                    is Result.Error -> {
                        Log.e(TAG, "[HomeViewModel] Error karma: ${result.message}")
                    }
                    is Result.Loading -> {}
                }
            }
        }
    }
}

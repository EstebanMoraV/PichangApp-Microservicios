package cl.duoc.pichangapp.ui.screens.karma

import android.util.Log
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

private const val TAG = "PICHANGAPP_DEBUG"
private const val BASE_URL = "https://pichangapp-microservicios-production.up.railway.app"

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

    // Guard: evita múltiples llamadas al backend si los datos ya se cargaron
    private var dataLoaded = false

    init {
        loadKarma()
    }

    fun refresh() {
        dataLoaded = false
        loadKarma()
    }

    fun loadKarma() {
        // Si ya cargamos datos exitosamente, no volvemos a hacer las llamadas
        if (dataLoaded) {
            Log.d(TAG, "[KarmaViewModel] Datos ya cargados, omitiendo llamada al backend.")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            // --- DEBUG: Token y userId ---
            val token = tokenDataStore.tokenFlow.firstOrNull()
            Log.d(TAG, "[KarmaViewModel] Token: ${if (token.isNullOrEmpty()) "NULL ⚠️" else "OK (${token.length} chars)"}")

            val userIdFromJwt = if (!token.isNullOrEmpty()) JwtUtils.extractUserId(token) else null
            val userIdFromStore = tokenDataStore.userIdFlow.firstOrNull()
            Log.d(TAG, "[KarmaViewModel] userId JWT='${userIdFromJwt}' DataStore='${userIdFromStore}'")

            val userId = userIdFromJwt ?: userIdFromStore
            Log.d(TAG, "[KarmaViewModel] userId FINAL: ${userId ?: "NULL → abortando ⛔"}")

            if (userId.isNullOrEmpty()) {
                _state.value = _state.value.copy(
                    error = "No se pudo obtener el ID del usuario",
                    isLoading = false
                )
                return@launch
            }

            // --- Load Karma ---
            Log.d(TAG, "[KarmaViewModel] GET $BASE_URL/api/v1/karma/$userId")
            getKarmaUseCase(userId).collect { result ->
                when (result) {
                    is Result.Success -> {
                        val score = result.data.puntaje
                        val calculatedCategory = when {
                            score >= 80 -> "Excelente"
                            score >= 60 -> "Bueno"
                            score >= 40 -> "Regular"
                            else -> "Bajo"
                        }
                        Log.d(TAG, "[KarmaViewModel] Karma OK → puntaje=$score categoría='$calculatedCategory'")
                        val updatedKarma = result.data.copy(categoria = calculatedCategory)
                        _state.value = _state.value.copy(karma = updatedKarma, isLoading = false)
                        dataLoaded = true // Marcar datos como cargados exitosamente
                    }
                    is Result.Error -> {
                        Log.e(TAG, "[KarmaViewModel] Error karma: ${result.message}")
                        _state.value = _state.value.copy(error = result.message, isLoading = false)
                    }
                    is Result.Loading -> {}
                }
            }
        }
    }
}

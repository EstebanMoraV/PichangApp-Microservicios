package cl.duoc.pichangapp.ui.screens.karma

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.pichangapp.core.datastore.TokenDataStore
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

    private fun loadKarma() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val userId = tokenDataStore.userIdFlow.firstOrNull() ?: "1"

            getKarmaUseCase(userId).collect { result ->
                when (result) {
                    is Result.Success -> _state.value = _state.value.copy(karma = result.data, isLoading = false)
                    is Result.Error -> _state.value = _state.value.copy(error = result.message, isLoading = false)
                    is Result.Loading -> {}
                }
            }
        }
    }
}

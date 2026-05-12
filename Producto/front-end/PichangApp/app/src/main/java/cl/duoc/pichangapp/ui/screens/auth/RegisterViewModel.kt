package cl.duoc.pichangapp.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.pichangapp.core.util.Result
import cl.duoc.pichangapp.data.model.RegisterRequest
import cl.duoc.pichangapp.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    fun register(correo: String, pass: String, nombre: String, apellido: String) {
        viewModelScope.launch {
            val request = RegisterRequest(correo, pass, nombre, apellido)
            registerUseCase(request).collect { result ->
                when (result) {
                    is Result.Loading -> _state.value = RegisterState.Loading
                    is Result.Success -> _state.value = RegisterState.Success
                    is Result.Error -> _state.value = RegisterState.Error(result.message)
                }
            }
        }
    }
}

package cl.duoc.pichangapp.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.pichangapp.core.util.Result
import cl.duoc.pichangapp.data.model.LoginRequest
import cl.duoc.pichangapp.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun login(correo: String, pass: String) {
        viewModelScope.launch {
            val request = LoginRequest(correo, pass)
            loginUseCase(request).collect { result ->
                when (result) {
                    is Result.Loading -> _state.value = LoginState.Loading
                    is Result.Success -> _state.value = LoginState.Success
                    is Result.Error -> _state.value = LoginState.Error(result.message)
                }
            }
        }
    }
}

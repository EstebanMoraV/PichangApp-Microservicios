package cl.duoc.pichangapp.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.pichangapp.data.model.ResendCodeRequest
import cl.duoc.pichangapp.data.model.VerifyCodeRequest
import cl.duoc.pichangapp.data.remote.AuthApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class VerifyCodeState {
    object Idle : VerifyCodeState()
    object Loading : VerifyCodeState()
    object Success : VerifyCodeState()
    data class Error(val message: String) : VerifyCodeState()
}

@HiltViewModel
class VerifyCodeViewModel @Inject constructor(
    private val authApi: AuthApi
) : ViewModel() {

    private val _state = MutableStateFlow<VerifyCodeState>(VerifyCodeState.Idle)
    val state: StateFlow<VerifyCodeState> = _state.asStateFlow()

    private val _timeLeft = MutableStateFlow(300) // 5 minutes
    val timeLeft: StateFlow<Int> = _timeLeft.asStateFlow()

    init {
        startTimer()
    }

    private fun startTimer() {
        viewModelScope.launch {
            _timeLeft.value = 300
            while (_timeLeft.value > 0) {
                delay(1000L)
                _timeLeft.value -= 1
            }
        }
    }

    fun verifyCode(email: String, code: String) {
        viewModelScope.launch {
            _state.value = VerifyCodeState.Loading
            try {
                val response = authApi.verifyCode(VerifyCodeRequest(email, code))
                if (response.isSuccessful) {
                    _state.value = VerifyCodeState.Success
                } else {
                    _state.value = VerifyCodeState.Error("Código inválido o expirado")
                }
            } catch (e: Exception) {
                _state.value = VerifyCodeState.Error("Error de conexión: ${e.message}")
            }
        }
    }

    fun resendCode(email: String) {
        viewModelScope.launch {
            _state.value = VerifyCodeState.Loading
            try {
                val response = authApi.resendCode(ResendCodeRequest(email))
                if (response.isSuccessful) {
                    _state.value = VerifyCodeState.Idle
                    startTimer()
                } else {
                    _state.value = VerifyCodeState.Error("Error al reenviar código")
                }
            } catch (e: Exception) {
                _state.value = VerifyCodeState.Error("Error de conexión: ${e.message}")
            }
        }
    }
}

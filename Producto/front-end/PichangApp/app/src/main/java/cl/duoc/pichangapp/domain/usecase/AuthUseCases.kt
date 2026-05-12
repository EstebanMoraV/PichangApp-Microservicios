package cl.duoc.pichangapp.domain.usecase

import cl.duoc.pichangapp.core.util.Result
import cl.duoc.pichangapp.data.model.LoginRequest
import cl.duoc.pichangapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(request: LoginRequest): Flow<Result<Unit>> {
        return authRepository.login(request)
    }
}

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(request: RegisterRequest): Flow<Result<Unit>> {
        return authRepository.register(request)
    }
}

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        authRepository.logout()
    }
}

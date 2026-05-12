package cl.duoc.pichangapp.domain.usecase

import cl.duoc.pichangapp.core.util.Result
import cl.duoc.pichangapp.data.model.UserDto
import cl.duoc.pichangapp.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(userId: String): Flow<Result<UserDto>> {
        return userRepository.getUserProfile(userId)
    }
}

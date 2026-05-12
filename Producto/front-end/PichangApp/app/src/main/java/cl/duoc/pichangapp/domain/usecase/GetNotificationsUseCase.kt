package cl.duoc.pichangapp.domain.usecase

import cl.duoc.pichangapp.core.util.Result
import cl.duoc.pichangapp.data.model.NotificationDto
import cl.duoc.pichangapp.data.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    operator fun invoke(userId: String): Flow<Result<List<NotificationDto>>> {
        return notificationRepository.getNotifications(userId)
    }
}

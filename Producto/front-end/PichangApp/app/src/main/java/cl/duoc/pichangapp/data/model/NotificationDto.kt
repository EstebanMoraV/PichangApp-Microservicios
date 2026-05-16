package cl.duoc.pichangapp.data.model

import androidx.compose.runtime.Stable

@Stable
data class NotificationDto(
    val id: String?,
    val title: String,
    val body: String,
    val type: String, // KARMA_INCREASE, KARMA_DECREASE, EVENT_REMINDER
    val timestamp: String? = null
)

@Stable
data class DeviceTokenRequest(
    val userId: String,
    val token: String
)

@Stable
data class NotificationSendRequest(
    val userId: String,
    val title: String,
    val body: String,
    val type: String
)

@Stable
data class NotificationPageResponse(
    val content: List<NotificationDto>,
    val totalElements: Int,
    val totalPages: Int,
    val size: Int,
    val number: Int
)

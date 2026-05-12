package cl.duoc.pichangapp.data.model

data class NotificationDto(
    val id: String?,
    val title: String,
    val body: String,
    val type: String, // KARMA_INCREASE, KARMA_DECREASE, EVENT_REMINDER
    val timestamp: String? = null
)

data class DeviceTokenRequest(
    val userId: String,
    val token: String
)

data class NotificationSendRequest(
    val userId: String,
    val title: String,
    val body: String,
    val type: String
)

package cl.duoc.pichangapp.data.model

data class EventDto(
    val id: Int,
    val organizerId: Int,
    val name: String,
    val sport: String,
    val eventDate: String,
    val latitude: Double,
    val longitude: Double,
    val locationName: String,
    val maxPlayers: Int,
    val currentPlayers: Int,
    val status: String,
    val createdAt: String,
    val distanceKm: Double?
)

data class CreateEventRequest(
    val name: String,
    val sport: String,
    val eventDate: String,
    val latitude: Double,
    val longitude: Double,
    val locationName: String,
    val maxPlayers: Int
)

data class EventCheckInRequest(
    val latitude: Double,
    val longitude: Double
)

data class AttendanceRequest(
    val userId: Int,
    val attended: Boolean
)

data class EventRegistrationDto(
    val id: Int,
    val userId: Int,
    val status: String,
    val registeredAt: String
)

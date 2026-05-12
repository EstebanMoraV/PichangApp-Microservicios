package cl.duoc.pichangapp.data.model

data class KarmaDto(
    val puntaje: Int,
    val categoria: String // Bronce, Plata, Oro, Excelente, Bueno, Regular, Bajo
)

data class CheckInRequest(
    val userId: String,
    val eventId: String,
    val location: String
)

data class ValidationRequest(
    val userId: String,
    val eventId: String,
    val organizerId: String,
    val isPositiveValidation: Boolean
)

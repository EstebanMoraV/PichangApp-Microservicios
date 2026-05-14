package cl.duoc.pichangapp.data.model

import com.google.gson.annotations.SerializedName

data class KarmaDto(
    @SerializedName("karmaScore") val puntaje: Int = 0,
    @SerializedName("category")   val categoria: String = "Sin categoría",
    @SerializedName("history")    val history: List<KarmaHistoryDto> = emptyList()
)

data class KarmaHistoryDto(
    @SerializedName("amount") val amount: Int,
    @SerializedName("reason") val reason: String,
    @SerializedName("createdAt") val createdAt: String
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

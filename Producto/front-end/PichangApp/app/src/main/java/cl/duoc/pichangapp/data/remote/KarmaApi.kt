package cl.duoc.pichangapp.data.remote

import cl.duoc.pichangapp.data.model.CheckInRequest
import cl.duoc.pichangapp.data.model.KarmaDto
import cl.duoc.pichangapp.data.model.ValidationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface KarmaApi {
    @GET("api/v1/karma/{id}")
    suspend fun getKarma(@Path("id") userId: String): Response<KarmaDto>

    @POST("api/v1/karma/check-in")
    suspend fun checkIn(@Body request: CheckInRequest): Response<Void>

    @POST("api/v1/karma/absence/{userId}/event/{eventId}")
    suspend fun registerAbsence(@Path("userId") userId: String, @Path("eventId") eventId: String): Response<Void>

    @POST("api/v1/karma/validation")
    suspend fun validateKarma(@Body request: ValidationRequest): Response<Void>
}

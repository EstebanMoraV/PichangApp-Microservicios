package cl.duoc.pichangapp.data.remote

import cl.duoc.pichangapp.data.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface EventApi {
    @GET("api/v1/events")
    suspend fun getEvents(
        @Query("lat") lat: Double,
        @Query("lng") lng: Double
    ): List<EventDto>

    @GET("api/v1/events/{id}")
    suspend fun getEventById(@Path("id") id: Int): EventDto

    @POST("api/v1/events")
    suspend fun createEvent(@Body request: CreateEventRequest): EventDto

    @POST("api/v1/events/{id}/join")
    suspend fun joinEvent(@Path("id") id: Int): Any

    @POST("api/v1/events/{id}/checkin")
    suspend fun checkIn(
        @Path("id") id: Int,
        @Body location: EventCheckInRequest
    ): Any

    @GET("api/v1/events/{id}/registrations")
    suspend fun getRegistrations(@Path("id") id: Int): List<EventRegistrationDto>

    @POST("api/v1/events/{id}/attendance")
    suspend fun markAttendance(
        @Path("id") id: Int,
        @Body request: AttendanceRequest
    ): Any

    @POST("api/v1/events/{id}/finish")
    suspend fun finishEvent(@Path("id") id: Int): Any

    @GET("api/v1/events/my-events")
    suspend fun getMyEvents(): List<EventDto>

    @GET("api/v1/events/organizing")
    suspend fun getOrganizingEvents(): List<EventDto>
}

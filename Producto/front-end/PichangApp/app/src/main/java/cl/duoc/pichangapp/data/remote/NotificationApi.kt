package cl.duoc.pichangapp.data.remote

import cl.duoc.pichangapp.data.model.DeviceTokenRequest
import cl.duoc.pichangapp.data.model.NotificationDto
import cl.duoc.pichangapp.data.model.NotificationSendRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApi {
    @POST("api/v1/notifications/device-token")
    suspend fun registerDeviceToken(@Body request: DeviceTokenRequest): Response<Void>

    @POST("api/v1/notifications/send")
    suspend fun sendNotification(@Body request: NotificationSendRequest): Response<Void>

    // Note: The backend might return a page object or a list. Let's assume it returns a Page object and we might need to map it. 
    // If it returns a Page, we will need a Page wrapper. Assuming it returns List<NotificationDto> for simplicity, 
    // or we'll wrap it if needed.
    @GET("api/v1/notifications/{id}")
    suspend fun getNotifications(
        @Path("id") userId: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): retrofit2.Response<cl.duoc.pichangapp.data.model.NotificationPageResponse>
}

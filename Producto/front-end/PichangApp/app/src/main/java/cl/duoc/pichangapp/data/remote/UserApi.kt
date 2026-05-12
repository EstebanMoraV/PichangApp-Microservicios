package cl.duoc.pichangapp.data.remote

import cl.duoc.pichangapp.data.model.PasswordUpdateRequest
import cl.duoc.pichangapp.data.model.UserDto
import cl.duoc.pichangapp.data.model.UserUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {
    @GET("api/v1/users/{id}")
    suspend fun getUserProfile(@Path("id") id: String): Response<UserDto>

    @PUT("api/v1/users/{id}")
    suspend fun updateUserProfile(@Path("id") id: String, @Body request: UserUpdateRequest): Response<UserDto>

    @PUT("api/v1/users/{id}/password")
    suspend fun updatePassword(@Path("id") id: String, @Body request: PasswordUpdateRequest): Response<Void>
}

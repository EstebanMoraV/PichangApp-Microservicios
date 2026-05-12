package cl.duoc.pichangapp.data.remote

import cl.duoc.pichangapp.data.model.AuthResponse
import cl.duoc.pichangapp.data.model.LoginRequest
import cl.duoc.pichangapp.data.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Void>

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
}

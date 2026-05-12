package cl.duoc.pichangapp.data.repository

import cl.duoc.pichangapp.core.datastore.TokenDataStore
import cl.duoc.pichangapp.core.util.Result
import cl.duoc.pichangapp.data.model.LoginRequest
import cl.duoc.pichangapp.data.model.RegisterRequest
import cl.duoc.pichangapp.data.remote.AuthApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val tokenDataStore: TokenDataStore
) {
    fun login(request: LoginRequest): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val response = authApi.login(request)
            if (response.isSuccessful && response.body() != null) {
                val token = response.body()?.token ?: ""
                tokenDataStore.saveToken(token)
                
                // For simplicity, we can decode the JWT to get the user ID if the backend doesn't return it
                // Or if it returns it in AuthResponse, we save it here. Assuming we can query the user later or have ID=1 as mocked in Postman.
                // We'll just save the token for now.
                
                emit(Result.Success(Unit))
            } else {
                emit(Result.Error("Login fallido: ${response.code()}"))
            }
        } catch (e: IOException) {
            emit(Result.Error("Error de conexión"))
        } catch (e: HttpException) {
            emit(Result.Error("Error HTTP: ${e.code()}"))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Error desconocido"))
        }
    }

    fun register(request: RegisterRequest): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val response = authApi.register(request)
            if (response.isSuccessful) {
                emit(Result.Success(Unit))
            } else {
                emit(Result.Error("Registro fallido: ${response.code()}"))
            }
        } catch (e: IOException) {
            emit(Result.Error("Error de conexión"))
        } catch (e: HttpException) {
            emit(Result.Error("Error HTTP: ${e.code()}"))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Error desconocido"))
        }
    }

    suspend fun logout() {
        tokenDataStore.clearAuth()
    }
}

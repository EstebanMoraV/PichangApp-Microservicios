package cl.duoc.pichangapp.data.repository

import cl.duoc.pichangapp.core.util.Result
import cl.duoc.pichangapp.data.model.UserDto
import cl.duoc.pichangapp.data.remote.UserApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userApi: UserApi
) {
    fun getUserProfile(userId: String): Flow<Result<UserDto>> = flow {
        emit(Result.Loading)
        try {
            val response = userApi.getUserProfile(userId)
            if (response.isSuccessful && response.body() != null) {
                emit(Result.Success(response.body()!!))
            } else {
                emit(Result.Error("Error al obtener perfil: ${response.code()}"))
            }
        } catch (e: IOException) {
            emit(Result.Error("Error de conexión"))
        } catch (e: HttpException) {
            emit(Result.Error("Error HTTP: ${e.code()}"))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Error desconocido"))
        }
    }
}

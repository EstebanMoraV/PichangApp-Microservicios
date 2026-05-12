package cl.duoc.pichangapp.data.model

data class UserDto(
    val id: String?,
    val nombre: String,
    val apellido: String,
    val correo: String? = null
)

data class UserUpdateRequest(
    val nombre: String,
    val apellido: String
)

data class PasswordUpdateRequest(
    val currentPassword: String,
    val newPassword: String
)

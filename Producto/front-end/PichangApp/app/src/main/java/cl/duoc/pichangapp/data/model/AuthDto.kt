package cl.duoc.pichangapp.data.model

data class AuthResponse(
    val token: String,
    val userId: String? = null // Assuming the backend might return userId on login, if not we'll extract from JWT or another endpoint
)

data class LoginRequest(
    val correo: String,
    val password: String
)

data class RegisterRequest(
    val correo: String,
    val password: String,
    val nombre: String,
    val apellido: String
)

data class VerifyCodeRequest(
    val email: String,
    val code: String
)

data class ResendCodeRequest(
    val email: String
)

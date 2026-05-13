package cl.duoc.pichangapp.core.util

import android.util.Base64
import org.json.JSONObject

/**
 * Utilidad para extraer claims del JWT sin necesidad de verificar la firma.
 * El backend genera el JWT con:
 *   - sub = userId (String del ID numérico)
 *   - correo = email del usuario
 */
object JwtUtils {

    /**
     * Extrae el userId (claim "sub") del payload del JWT.
     * Retorna null si el token es inválido o no contiene el claim.
     */
    fun extractUserId(token: String): String? {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return null

            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP))
            val json = JSONObject(payload)

            // El backend usa setSubject(userId), por lo tanto el claim es "sub"
            json.optString("sub").takeIf { it.isNotEmpty() }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Extrae el correo (claim "correo") del payload del JWT.
     */
    fun extractCorreo(token: String): String? {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return null

            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP))
            val json = JSONObject(payload)

            json.optString("correo").takeIf { it.isNotEmpty() }
        } catch (e: Exception) {
            null
        }
    }
}

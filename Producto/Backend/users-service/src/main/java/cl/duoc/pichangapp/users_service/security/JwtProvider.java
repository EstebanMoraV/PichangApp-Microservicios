package cl.duoc.pichangapp.users_service.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Componente responsable de generar y validar tokens JWT.
 * - Lee la clave secreta y la expiración desde application.properties.
 * - Provee métodos para generar token, validar y obtener subject.
 */
@Component
public class JwtProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret; // Debe ser una cadena suficientemente larga en application.properties

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs; // Tiempo de expiración en milisegundos

    // Construye la clave de firma a partir del secreto
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Genera un token JWT con subject = userId (string), claim "correo" y claim "role".
     * El token se firma con HS256.
     */
    public String generateToken(String subjectId, String correo, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(subjectId)
                .claim("correo", correo)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valida la integridad y expiración del token.
     * Retorna true si el token es válido, false en caso contrario.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Devuelve el subject (userId) contenido en el token.
     * Lanza excepción si el token no es válido.
     */
    public String getSubject(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    /**
     * Devuelve el rol contenido en el claim "role" del token.
     * Si el token no incluye el claim (tokens antiguos o internos), retorna "USER" por defecto.
     */
    public String getRole(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token).getBody();
        String role = claims.get("role", String.class);
        return role != null ? role : "USER";
    }

    /**
     * Retorna el tiempo de expiración configurado (ms).
     */
    public long getExpirationMs() {
        return expirationMs;
    }
}



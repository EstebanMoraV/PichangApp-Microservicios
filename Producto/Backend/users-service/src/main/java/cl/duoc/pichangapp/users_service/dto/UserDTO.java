package cl.duoc.pichangapp.users_service.dto;

/**
 * DTO que representa la información pública de un usuario.
 * No expone el campo {@code id}: el identificador único visible para el cliente es el correo.
 * El id solo se usa internamente entre microservicios (vía JWT subject o path params).
 */
public record UserDTO(
        String correo,    // Correo electrónico (identificador único visible)
        String nombre,    // Nombre
        String apellido,  // Apellido
        boolean enabled,  // Estado de verificación de la cuenta
        String role       // Rol del usuario: "USER" o "ADMIN"
) {}




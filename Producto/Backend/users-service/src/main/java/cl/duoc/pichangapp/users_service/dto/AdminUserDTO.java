package cl.duoc.pichangapp.users_service.dto;

/**
 * DTO usado exclusivamente por los endpoints de administración (rol ADMIN).
 * Incluye el {@code id} numérico SOLO para uso interno del panel de administración
 * (construir URLs como /api/v1/admin/users/{id} o /api/v1/admin/karma/{id}).
 * El panel web NO debe renderizar este id; el identificador visible es el correo.
 * Nunca expone la contraseña.
 */
public record AdminUserDTO(
        Integer id,       // Solo para uso interno del panel (acciones); no se muestra en la UI
        String correo,    // Identificador único visible
        String nombre,
        String apellido,
        boolean enabled,  // Estado de la cuenta (habilitado/deshabilitado)
        String role       // "USER" o "ADMIN"
) {}

package cl.duoc.pichangapp.users_service.dto;

import java.util.List;

/**
 * Perfil público de un usuario: lo que se muestra a OTROS usuarios.
 * No expone id ni contraseña. El correo se incluye como clave única de identidad.
 * El historial solo viene poblado en la vista de perfil individual y únicamente
 * si el usuario tiene historialVisible == true (gate aplicado en el servicio).
 */
public record PerfilPublicoDTO(
        String correo,
        String nombre,
        String apellido,
        Integer karmaScore,
        String categoriaKarma,
        Boolean historialVisible,
        List<HistorialItem> history
) {
    /** Movimiento de karma expuesto en el perfil público. */
    public record HistorialItem(Integer amount, String reason, String createdAt) {}
}

package cl.duoc.pichangapp.karma_service.dto;

/**
 * Petición para el ajuste manual de karma por parte de un administrador.
 */
public record AdminKarmaAdjustmentDTO(
        Integer newKarmaScore, // Nuevo puntaje de karma a establecer
        String reason          // Motivo del ajuste (informativo)
) {}

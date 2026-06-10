package cl.duoc.pichangapp.events_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO usado por los endpoints de administración de eventos (rol ADMIN).
 * Incluye el {@code id} numérico SOLO para uso interno del panel (construir URLs
 * como /api/v1/admin/events/{id}); el panel no debe renderizarlo.
 * El organizador se identifica por su correo, no por su id numérico.
 */
@Data
public class AdminEventDTO {
    private Integer id;              // Solo para acciones internas del panel; no se muestra
    private String organizerEmail;  // Correo del organizador (identificador visible)
    private String name;
    private String sport;
    private LocalDateTime eventDate;
    private String locationName;
    private Integer maxPlayers;
    private Integer currentPlayers;
    private String status;          // ACTIVE, FINISHED, CANCELLED
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
}

package cl.duoc.pichangapp.events_service.controller;

import cl.duoc.pichangapp.events_service.dto.AdminEventDTO;
import cl.duoc.pichangapp.events_service.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints de administración de eventos.
 * Protegidos a nivel de SecurityConfig: solo accesibles con rol ADMIN.
 */
@RestController
@RequestMapping("/api/v1/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final EventService eventService;

    /**
     * Lista todos los eventos (activos, finalizados y cancelados).
     * GET /api/v1/admin/events
     */
    @GetMapping
    public ResponseEntity<List<AdminEventDTO>> listAllEvents() {
        return ResponseEntity.ok(eventService.listAllEventsForAdmin());
    }

    /**
     * Elimina (cancela) un evento aplicando el mismo flujo que la eliminación por organizador
     * (compensación de karma + notificación a los inscritos).
     * DELETE /api/v1/admin/events/{eventId}
     */
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer eventId) {
        eventService.deleteEventAsAdmin(eventId);
        return ResponseEntity.noContent().build();
    }
}

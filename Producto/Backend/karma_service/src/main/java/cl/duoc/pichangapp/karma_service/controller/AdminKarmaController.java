package cl.duoc.pichangapp.karma_service.controller;

import cl.duoc.pichangapp.karma_service.dto.AdminKarmaAdjustmentDTO;
import cl.duoc.pichangapp.karma_service.dto.KarmaResponseDTO;
import cl.duoc.pichangapp.karma_service.service.KarmaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints de administración de karma.
 * Protegidos a nivel de SecurityConfig: solo accesibles con rol ADMIN.
 */
@RestController
@RequestMapping("/api/v1/admin/karma")
@RequiredArgsConstructor
public class AdminKarmaController {

    private final KarmaService karmaService;

    /**
     * Ajuste manual del karma de un usuario.
     * PUT /api/v1/admin/karma/{userId}
     */
    @PutMapping("/{userId}")
    public ResponseEntity<KarmaResponseDTO> adjustKarma(
            @PathVariable String userId,
            @RequestBody AdminKarmaAdjustmentDTO request) {
        return ResponseEntity.ok(karmaService.adminAdjustKarma(userId, request));
    }
}

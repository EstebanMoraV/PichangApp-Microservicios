package cl.duoc.pichangapp.users_service.controller;

import cl.duoc.pichangapp.users_service.dto.AdminUserDTO;
import cl.duoc.pichangapp.users_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints de administración de usuarios.
 * Protegidos a nivel de SecurityConfig: solo accesibles con rol ADMIN.
 */
@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    /**
     * Lista todos los usuarios (sin contraseñas; el id solo es para uso interno del panel).
     * GET /api/v1/admin/users
     */
    @GetMapping
    public ResponseEntity<List<AdminUserDTO>> listUsers() {
        return ResponseEntity.ok(userService.listAllUsers());
    }

    /**
     * Detalle de un usuario específico.
     * GET /api/v1/admin/users/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<AdminUserDTO> getUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.getUserForAdmin(userId));
    }

    /**
     * Elimina un usuario.
     * DELETE /api/v1/admin/users/{userId}
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}

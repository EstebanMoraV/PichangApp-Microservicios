package cl.duoc.pichangapp.users_service.dto;

public record VerifyCodeRequest(
        String email,
        String code
) {}

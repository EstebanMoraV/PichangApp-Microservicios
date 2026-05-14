package cl.duoc.pichangapp.karma_service.dto;

import java.time.Instant;

public record KarmaHistoryDTO(
        Integer amount,
        String reason,
        Instant createdAt
) {}

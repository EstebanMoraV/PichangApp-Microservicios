package cl.duoc.pichangapp.karma_service.dto;

import java.util.List;

public record KarmaResponseDTO(
        String userId,
        Integer karmaScore,
        String category,
        List<KarmaHistoryDTO> history
) {}

package cl.duoc.pichangapp.events_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateEventRequest {
    private String name;
    private String sport;
    private LocalDateTime eventDate;
    private Double latitude;
    private Double longitude;
    private String locationName;
    private Integer maxPlayers;
}

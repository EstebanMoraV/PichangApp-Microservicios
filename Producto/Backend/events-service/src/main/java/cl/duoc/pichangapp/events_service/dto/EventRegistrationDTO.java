package cl.duoc.pichangapp.events_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventRegistrationDTO {
    private Integer id;
    private Integer eventId;
    private Integer userId;
    private String status;
    private LocalDateTime registeredAt;
    private LocalDateTime checkedInAt;
}

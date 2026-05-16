package cl.duoc.pichangapp.events_service.dto;

import lombok.Data;

@Data
public class NotificationRequest {
    private String userId;
    private String title;
    private String body;
    private String type;
}

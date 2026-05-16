package cl.duoc.pichangapp.events_service.dto;

import lombok.Data;

@Data
public class AttendanceRequest {
    private Integer userId;
    private boolean attended;
}

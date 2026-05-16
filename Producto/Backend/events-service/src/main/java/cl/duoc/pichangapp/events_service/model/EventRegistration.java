package cl.duoc.pichangapp.events_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "event_registrations")
@Data
public class EventRegistration {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer eventId;
    private Integer userId;
    private String status;  // REGISTERED, ATTENDED, ABSENT
    private LocalDateTime registeredAt;
    private LocalDateTime checkedInAt;
}

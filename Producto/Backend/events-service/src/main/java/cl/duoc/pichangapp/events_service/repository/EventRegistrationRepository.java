package cl.duoc.pichangapp.events_service.repository;

import cl.duoc.pichangapp.events_service.model.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Integer> {
    boolean existsByEventIdAndUserId(Integer eventId, Integer userId);
    Optional<EventRegistration> findByEventIdAndUserId(Integer eventId, Integer userId);
    List<EventRegistration> findByEventId(Integer eventId);
    List<EventRegistration> findByUserId(Integer userId);
    List<EventRegistration> findByEventIdAndStatusIn(Integer eventId, List<String> statuses);
}

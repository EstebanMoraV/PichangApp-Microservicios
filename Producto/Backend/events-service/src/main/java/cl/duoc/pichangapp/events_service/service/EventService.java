package cl.duoc.pichangapp.events_service.service;

import cl.duoc.pichangapp.events_service.dto.CreateEventRequest;
import cl.duoc.pichangapp.events_service.dto.EventRegistrationDTO;
import cl.duoc.pichangapp.events_service.dto.EventResponseDTO;
import cl.duoc.pichangapp.events_service.exception.EventNotFoundException;
import cl.duoc.pichangapp.events_service.model.Event;
import cl.duoc.pichangapp.events_service.model.EventRegistration;
import cl.duoc.pichangapp.events_service.repository.EventRegistrationRepository;
import cl.duoc.pichangapp.events_service.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventRegistrationRepository eventRegistrationRepository;
    private final KarmaServiceClient karmaServiceClient;

    @Transactional
    public EventResponseDTO createEvent(CreateEventRequest request, Integer organizerId) {
        Event event = new Event();
        event.setOrganizerId(organizerId);
        event.setName(request.getName());
        event.setSport(request.getSport());
        event.setEventDate(request.getEventDate());
        event.setLatitude(request.getLatitude());
        event.setLongitude(request.getLongitude());
        event.setLocationName(request.getLocationName());
        event.setMaxPlayers(request.getMaxPlayers());
        event.setCurrentPlayers(0);
        event.setStatus("ACTIVE");
        event.setCreatedAt(LocalDateTime.now());
        
        Event saved = eventRepository.save(event);
        return mapToDTO(saved, null);
    }

    public List<EventResponseDTO> findNearbyEvents(double lat, double lng) {
        LocalDateTime now = LocalDateTime.now();
        List<Event> activeEvents = eventRepository.findByStatusAndEventDateAfter("ACTIVE", now);
        
        return activeEvents.stream()
                .map(event -> {
                    double distance = calculateDistance(lat, lng, event.getLatitude(), event.getLongitude());
                    return mapToDTO(event, distance);
                })
                .sorted(Comparator.comparing(EventResponseDTO::getDistanceKm))
                .collect(Collectors.toList());
    }

    public EventResponseDTO getEventDetails(Integer id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        return mapToDTO(event, null);
    }

    @Transactional
    public void joinEvent(Integer eventId, Integer userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        
        if (!"ACTIVE".equals(event.getStatus())) {
            throw new IllegalStateException("Event is not active");
        }
        if (event.getEventDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot join a past event");
        }
        if (event.getOrganizerId().equals(userId)) {
            throw new IllegalStateException("Organizer cannot join their own event as a participant");
        }
        if (event.getCurrentPlayers() >= event.getMaxPlayers()) {
            throw new IllegalStateException("Event is full");
        }
        if (eventRegistrationRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new IllegalStateException("User already registered");
        }

        EventRegistration registration = new EventRegistration();
        registration.setEventId(eventId);
        registration.setUserId(userId);
        registration.setStatus("REGISTERED");
        registration.setRegisteredAt(LocalDateTime.now());
        eventRegistrationRepository.save(registration);

        event.setCurrentPlayers(event.getCurrentPlayers() + 1);
        eventRepository.save(event);
    }

    @Transactional
    public void leaveEvent(Integer eventId, Integer userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        
        EventRegistration registration = eventRegistrationRepository.findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new IllegalStateException("User is not registered for this event"));

        if (!event.getEventDate().minusHours(2).isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("No puedes cancelar con menos de 2 horas de anticipación");
        }

        eventRegistrationRepository.delete(registration);
        event.setCurrentPlayers(event.getCurrentPlayers() - 1);
        eventRepository.save(event);
    }


    @Transactional
    public void checkIn(Integer eventId, Integer userId, double userLat, double userLng) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
                
        EventRegistration registration = eventRegistrationRepository.findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new IllegalStateException("User is not registered for this event"));

        if (!"REGISTERED".equals(registration.getStatus())) {
            throw new IllegalStateException("User has already checked in or is absent");
        }

        double distance = calculateDistance(userLat, userLng, event.getLatitude(), event.getLongitude());
        if (distance > 0.5) { // 500 meters
            throw new IllegalStateException("User is too far from the event location (max 500m)");
        }

        registration.setStatus("ATTENDED");
        registration.setCheckedInAt(LocalDateTime.now());
        eventRegistrationRepository.save(registration);

        karmaServiceClient.registerCheckIn(userId, eventId);
    }

    public List<EventRegistrationDTO> getEventRegistrations(Integer eventId, Integer organizerId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        if (!event.getOrganizerId().equals(organizerId)) {
            throw new IllegalStateException("Only the organizer can view registrations");
        }
        return eventRegistrationRepository.findByEventId(eventId).stream()
                .map(this::mapRegToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAttendance(Integer eventId, Integer organizerId, Integer userId, boolean attended) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        if (!event.getOrganizerId().equals(organizerId)) {
            throw new IllegalStateException("Only the organizer can mark attendance");
        }
        EventRegistration registration = eventRegistrationRepository.findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new IllegalStateException("User is not registered"));
        
        if (!"REGISTERED".equals(registration.getStatus())) {
            throw new IllegalStateException("Attendance already marked");
        }

        if (attended) {
            registration.setStatus("ATTENDED");
            karmaServiceClient.registerCheckIn(userId, eventId);
        } else {
            registration.setStatus("ABSENT");
            karmaServiceClient.registerAbsence(userId, eventId);
        }
        eventRegistrationRepository.save(registration);
    }

    @Transactional
    public void finishEvent(Integer eventId, Integer organizerId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        if (!event.getOrganizerId().equals(organizerId)) {
            throw new IllegalStateException("Only the organizer can finish the event");
        }
        
        event.setStatus("FINISHED");
        event.setFinishedAt(LocalDateTime.now());
        eventRepository.save(event);

        List<EventRegistration> registrations = eventRegistrationRepository.findByEventId(eventId);
        for (EventRegistration reg : registrations) {
            if ("REGISTERED".equals(reg.getStatus())) {
                reg.setStatus("ABSENT");
                eventRegistrationRepository.save(reg);
                karmaServiceClient.registerAbsence(reg.getUserId(), eventId);
            }
        }
    }

    public List<EventResponseDTO> getMyEvents(Integer userId) {
        List<EventRegistration> registrations = eventRegistrationRepository.findByUserId(userId);
        return registrations.stream()
                .map(reg -> eventRepository.findById(reg.getEventId()).orElse(null))
                .filter(event -> event != null)
                .map(event -> mapToDTO(event, null))
                .collect(Collectors.toList());
    }

    public List<EventResponseDTO> getOrganizingEvents(Integer userId) {
        return eventRepository.findByOrganizerId(userId).stream()
                .map(event -> mapToDTO(event, null))
                .collect(Collectors.toList());
    }

    public double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        final int R = 6371; // Radio de la Tierra en km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lngDistance = Math.toRadians(lng2 - lng1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private EventResponseDTO mapToDTO(Event event, Double distance) {
        EventResponseDTO dto = new EventResponseDTO();
        dto.setId(event.getId());
        dto.setOrganizerId(event.getOrganizerId());
        dto.setName(event.getName());
        dto.setSport(event.getSport());
        dto.setEventDate(event.getEventDate());
        dto.setLatitude(event.getLatitude());
        dto.setLongitude(event.getLongitude());
        dto.setLocationName(event.getLocationName());
        dto.setMaxPlayers(event.getMaxPlayers());
        dto.setCurrentPlayers(event.getCurrentPlayers());
        dto.setStatus(event.getStatus());
        dto.setCreatedAt(event.getCreatedAt());
        dto.setFinishedAt(event.getFinishedAt());
        dto.setDistanceKm(distance);
        return dto;
    }

    private EventRegistrationDTO mapRegToDTO(EventRegistration reg) {
        EventRegistrationDTO dto = new EventRegistrationDTO();
        dto.setId(reg.getId());
        dto.setEventId(reg.getEventId());
        dto.setUserId(reg.getUserId());
        dto.setStatus(reg.getStatus());
        dto.setRegisteredAt(reg.getRegisteredAt());
        dto.setCheckedInAt(reg.getCheckedInAt());
        return dto;
    }
}

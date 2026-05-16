package cl.duoc.pichangapp.events_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KarmaServiceClient {

    @Value("${karma.service.url:http://localhost:8082}")
    private String karmaServiceUrl;

    @Value("${service.internal.token}")
    private String internalToken;

    private final RestTemplate restTemplate;

    public KarmaServiceClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(internalToken);
        return headers;
    }

    public void registerCheckIn(Integer userId, Integer eventId) {
        try {
            String url = karmaServiceUrl + "/api/v1/karma/check-in";
            Map<String, Integer> body = new HashMap<>();
            body.put("userId", userId);
            body.put("eventId", eventId);

            HttpEntity<Map<String, Integer>> request = new HttpEntity<>(body, getHeaders());
            ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);
            log.info("Karma check-in registered for user {}, event {}, status {}", userId, eventId, response.getStatusCode());
        } catch (Exception e) {
            log.error("Failed to register check-in in karma service: {}", e.getMessage());
        }
    }

    public void registerAbsence(Integer userId, Integer eventId) {
        try {
            String url = karmaServiceUrl + "/api/v1/karma/absence/" + userId + "/event/" + eventId;
            HttpEntity<Void> request = new HttpEntity<>(getHeaders());
            ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);
            log.info("Karma absence registered for user {}, event {}, status {}", userId, eventId, response.getStatusCode());
        } catch (Exception e) {
            log.error("Failed to register absence in karma service: {}", e.getMessage());
        }
    }
}

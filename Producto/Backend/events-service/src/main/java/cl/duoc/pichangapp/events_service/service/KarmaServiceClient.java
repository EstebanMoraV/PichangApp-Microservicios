package cl.duoc.pichangapp.events_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@SuppressWarnings("null")
public class KarmaServiceClient {

    @Value("${karma.service.url:http://localhost:8082}")
    private String karmaServiceUrl;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    private final RestTemplate restTemplate;

    public KarmaServiceClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    private String generateInternalToken() {
        return Jwts.builder()
            .setSubject("events-service")
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora
            .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256)
            .compact();
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(generateInternalToken());
        return headers;
    }

    public void registerCheckIn(Integer userId, Integer eventId) {
        try {
            String url = karmaServiceUrl + "/api/v1/karma/check-in";
            Map<String, String> body = new HashMap<>();
            body.put("userId", String.valueOf(userId));
            body.put("eventId", String.valueOf(eventId));

            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, getHeaders());
            ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);
            log.info("Karma check-in registered for user {}, event {}, status {}", userId, eventId, response.getStatusCode());
        } catch (Exception e) {
            log.error("Failed to register check-in in karma service: {}", e.getMessage());
            throw new RuntimeException("Error al registrar check-in", e);
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
            throw new RuntimeException("Error al registrar absence", e);
        }
    }
}

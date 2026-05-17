package cl.duoc.pichangapp.events_service.service;

import cl.duoc.pichangapp.events_service.dto.NotificationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@SuppressWarnings("null")
public class NotificationServiceClient {

    private final RestTemplate restTemplate;
    private final String notificationServiceUrl;
    private final String internalToken;
    private static final Logger log = LoggerFactory.getLogger(NotificationServiceClient.class);

    public NotificationServiceClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${notification.service.url}") String notificationServiceUrl,
            @Value("${service.internal.token}") String internalToken) {
        this.restTemplate = restTemplateBuilder.build();
        this.notificationServiceUrl = notificationServiceUrl;
        this.internalToken = internalToken;
    }

    public void sendNotification(Integer userId, String title, String body, String type) {
        NotificationRequest req = new NotificationRequest();
        req.setUserId(String.valueOf(userId));
        req.setTitle(title);
        req.setBody(body);
        req.setType(type);
        sendNotification(req);
    }

    public void sendNotification(NotificationRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(internalToken);
            HttpEntity<NotificationRequest> entity = new HttpEntity<>(request, headers);

            restTemplate.postForEntity(
                    notificationServiceUrl + "/api/v1/notifications/send",
                    entity,
                    Void.class
            );
        } catch (Exception e) {
            log.error("Error al enviar notificación al servicio: {}", e.getMessage());
        }
    }
}

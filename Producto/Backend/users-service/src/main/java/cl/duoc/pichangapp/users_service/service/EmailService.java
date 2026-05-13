package cl.duoc.pichangapp.users_service.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.from.email}")
    private String fromEmail;

    @Value("${sendgrid.from.name}")
    private String fromName;

    public void sendVerificationEmail(String toEmail, String code) {
        Email from = new Email(fromEmail, fromName);
        Email to = new Email(toEmail);
        String subject = "Código de verificación - PichangApp";
        Content content = new Content("text/html", buildEmailHtml(code));
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            if (response.getStatusCode() >= 400) {
                throw new RuntimeException("Error enviando email: " + response.getBody());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error enviando email: " + e.getMessage());
        }
    }

    private String buildEmailHtml(String code) {
        return """
            <div style="font-family:Arial,sans-serif;max-width:600px;margin:0 auto;padding:20px;background:#f5f5f5;">
                <div style="background:#2E7D32;padding:30px;text-align:center;border-radius:8px 8px 0 0;">
                    <h1 style="color:white;margin:0;">PichangApp</h1>
                </div>
                <div style="background:white;padding:30px;border-radius:0 0 8px 8px;">
                    <h2 style="color:#333;">Verifica tu cuenta</h2>
                    <p style="color:#666;">Usa este código para activar tu cuenta:</p>
                    <div style="background:#f0f7f0;border:2px solid #2E7D32;border-radius:8px;padding:20px;text-align:center;margin:20px 0;">
                        <span style="font-size:48px;font-weight:bold;color:#2E7D32;letter-spacing:8px;">""" + code + """
                        </span>
                    </div>
                    <p style="color:#e53935;font-weight:bold;">⚠️ Este código expira en 5 minutos.</p>
                </div>
            </div>
        """;
    }
}

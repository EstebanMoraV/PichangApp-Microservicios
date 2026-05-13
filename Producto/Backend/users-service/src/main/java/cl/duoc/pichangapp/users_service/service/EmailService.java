package cl.duoc.pichangapp.users_service.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String to, String verificationCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Verifica tu cuenta - PichangApp");
            helper.setFrom("pichangapp.noreply@gmail.com");

            String htmlContent = "<div style=\"font-family: Arial, sans-serif; text-align: center; color: #333; max-width: 600px; margin: auto;\">"
                    + "<div style=\"background-color: #2E7D32; padding: 20px; border-radius: 10px 10px 0 0;\">"
                    + "<h1 style=\"color: white; margin: 0;\">PichangApp</h1>"
                    + "</div>"
                    + "<div style=\"padding: 30px; border: 1px solid #ddd; border-top: none; border-radius: 0 0 10px 10px; background-color: #f9f9f9;\">"
                    + "<h2 style=\"color: #2E7D32;\">Código de Verificación</h2>"
                    + "<p>Usa el siguiente código para verificar tu cuenta en PichangApp:</p>"
                    + "<div style=\"font-size: 36px; font-weight: bold; margin: 20px 0; color: #2E7D32; letter-spacing: 5px; background: #fff; padding: 10px; border: 2px dashed #2E7D32; display: inline-block; border-radius: 5px;\">"
                    + verificationCode
                    + "</div>"
                    + "<p style=\"color: #d32f2f; font-weight: bold;\">Este código expira en 5 minutos.</p>"
                    + "<p style=\"font-size: 12px; color: #777;\">Si no solicitaste este código, puedes ignorar este correo.</p>"
                    + "</div>"
                    + "</div>";

            helper.setText(htmlContent, true);
            mailSender.send(message);
            logger.info("Correo de verificación enviado a {}", to);

        } catch (MessagingException e) {
            logger.error("Error al enviar el correo de verificación a {}", to, e);
        }
    }
}

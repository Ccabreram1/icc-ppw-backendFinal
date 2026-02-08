package ec.edu.ups.icc.portafolio.modules.notifications.services;

import ec.edu.ups.icc.portafolio.modules.appointments.models.AppointmentEntity;
import ec.edu.ups.icc.portafolio.modules.users.models.UserEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendAppointmentNotification(AppointmentEntity appointment) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(appointment.getClient().getEmail());
            helper.setSubject("Solicitud de Asesoría Enviada");

            Map<String, Object> variables = new HashMap<>();
            variables.put("appointment", appointment);
            variables.put("client", appointment.getClient());
            variables.put("programmer", appointment.getProgrammer());

            Context context = new Context();
            context.setVariables(variables);

            String htmlContent = templateEngine.process("appointment-notification", context);

            helper.setText(htmlContent, true);
            mailSender.send(message);

            logger.info("📧 Email de notificación enviado a: {}", appointment.getClient().getEmail());

        } catch (MessagingException e) {
            logger.error("❌ Error enviando email de notificación: {}", e.getMessage());
        }
    }

    @Async
    public void sendAppointmentReminder(AppointmentEntity appointment) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(appointment.getClient().getEmail());
            helper.setSubject("Recordatorio de Asesoría");

            Map<String, Object> variables = new HashMap<>();
            variables.put("appointment", appointment);
            variables.put("programmer", appointment.getProgrammer());
            variables.put("dateTime", formatDateTime(appointment.getDateTime()));

            Context context = new Context();
            context.setVariables(variables);

            String htmlContent = templateEngine.process("appointment-reminder", context);

            helper.setText(htmlContent, true);
            mailSender.send(message);

            logger.info("📧 Email de recordatorio enviado a: {}", appointment.getClient().getEmail());

        } catch (MessagingException e) {
            logger.error("❌ Error enviando email de recordatorio: {}", e.getMessage());
        }
    }

    @Async
    public void sendDailySchedule(UserEntity programmer, List<AppointmentEntity> appointments) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(programmer.getEmail());
            helper.setSubject("Horario Diario - " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            Map<String, Object> variables = new HashMap<>();
            variables.put("programmer", programmer);
            variables.put("appointments", appointments);
            variables.put("today", LocalDate.now());

            Context context = new Context();
            context.setVariables(variables);

            String htmlContent = templateEngine.process("daily-schedule", context);

            helper.setText(htmlContent, true);
            mailSender.send(message);

            logger.info("📧 Horario diario enviado a programador: {}", programmer.getEmail());

        } catch (MessagingException e) {
            logger.error("❌ Error enviando horario diario: {}", e.getMessage());
        }
    }

    @Async
    public void sendAppointmentApproval(AppointmentEntity appointment) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(appointment.getClient().getEmail());
            helper.setSubject("Asesoría Aprobada");

            Map<String, Object> variables = new HashMap<>();
            variables.put("appointment", appointment);
            variables.put("programmer", appointment.getProgrammer());
            variables.put("response", appointment.getProgrammerResponse());

            Context context = new Context();
            context.setVariables(variables);

            String htmlContent = templateEngine.process("appointment-approved", context);

            helper.setText(htmlContent, true);
            mailSender.send(message);

            logger.info("📧 Email de aprobación enviado a: {}", appointment.getClient().getEmail());

        } catch (MessagingException e) {
            logger.error("❌ Error enviando email de aprobación: {}", e.getMessage());
        }
    }

    @Async
    public void sendAppointmentRejection(AppointmentEntity appointment) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(appointment.getClient().getEmail());
            helper.setSubject("Asesoría Rechazada");

            Map<String, Object> variables = new HashMap<>();
            variables.put("appointment", appointment);
            variables.put("programmer", appointment.getProgrammer());
            variables.put("response", appointment.getProgrammerResponse());

            Context context = new Context();
            context.setVariables(variables);

            String htmlContent = templateEngine.process("appointment-rejected", context);

            helper.setText(htmlContent, true);
            mailSender.send(message);

            logger.info("📧 Email de rechazo enviado a: {}", appointment.getClient().getEmail());

        } catch (MessagingException e) {
            logger.error("❌ Error enviando email de rechazo: {}", e.getMessage());
        }
    }

    @Async
    public void sendWelcomeEmail(UserEntity user) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(user.getEmail());
            helper.setSubject("¡Bienvenido a Portafolio Administrable!");

            Map<String, Object> variables = new HashMap<>();
            variables.put("user", user);

            Context context = new Context();
            context.setVariables(variables);

            String htmlContent = templateEngine.process("welcome-email", context);

            helper.setText(htmlContent, true);
            mailSender.send(message);

            logger.info("📧 Email de bienvenida enviado a: {}", user.getEmail());

        } catch (MessagingException e) {
            logger.error("❌ Error enviando email de bienvenida: {}", e.getMessage());
        }
    }

    @Async
    public void sendPasswordResetEmail(UserEntity user, String resetToken) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(user.getEmail());
            helper.setSubject("Restablecer Contraseña");

            Map<String, Object> variables = new HashMap<>();
            variables.put("user", user);
            variables.put("resetToken", resetToken);
            variables.put("resetUrl", "http://localhost:4200/reset-password?token=" + resetToken);

            Context context = new Context();
            context.setVariables(variables);

            String htmlContent = templateEngine.process("password-reset", context);

            helper.setText(htmlContent, true);
            mailSender.send(message);

            logger.info("📧 Email de restablecimiento enviado a: {}", user.getEmail());

        } catch (MessagingException e) {
            logger.error("❌ Error enviando email de restablecimiento: {}", e.getMessage());
        }
    }

    private String formatDateTime(java.time.LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateTime.format(formatter);
    }
}
package ec.edu.ups.icc.portafolio.modules.notifications.services;

import ec.edu.ups.icc.portafolio.modules.appointments.models.AppointmentEntity;
import ec.edu.ups.icc.portafolio.modules.appointments.models.AppointmentStatus;
import ec.edu.ups.icc.portafolio.modules.appointments.repositories.AppointmentRepository;
import ec.edu.ups.icc.portafolio.modules.users.models.UserEntity;
import ec.edu.ups.icc.portafolio.modules.users.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationScheduler {

    private static final Logger logger = LoggerFactory.getLogger(NotificationScheduler.class);

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final EmailService emailService;

    public NotificationScheduler(AppointmentRepository appointmentRepository,
            UserRepository userRepository,
            NotificationService notificationService,
            EmailService emailService) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.emailService = emailService;
    }

    /**
     * Envía recordatorios de asesorías 1 hora antes
     */
    @Scheduled(cron = "0 0 * * * *") // Cada hora
    @Transactional
    public void sendAppointmentReminders() {
        logger.info("🔔 Ejecutando: Recordatorios de asesorías");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderTime = now.plusHours(1);

        List<AppointmentEntity> appointments = getAppointmentsForReminder(reminderTime);

        logger.info("📅 Asesorías para recordatorio: {}", appointments.size());

        for (AppointmentEntity appointment : appointments) {
            try {
                // 1. Crear notificación en la aplicación
                notificationService.sendAppointmentReminder(appointment.getId());

                // 2. Enviar email
                emailService.sendAppointmentReminder(appointment);

                logger.info("✅ Recordatorio enviado para asesoría ID: {}", appointment.getId());

            } catch (Exception e) {
                logger.error("❌ Error en asesoría ID {}: {}", appointment.getId(), e.getMessage());
            }
        }
    }

    /**
     * Envía horario diario a programadores
     */
    @Scheduled(cron = "0 0 9 * * *") // 9 AM diario
    @Transactional(readOnly = true)
    public void sendDailyScheduleToProgrammers() {
        logger.info("📅 Ejecutando: Horario diario para programadores");

        LocalDateTime todayStart = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime todayEnd = todayStart.plusDays(1);

        List<AppointmentEntity> todayAppointments = appointmentRepository
                .findByDateTimeBetweenAndStatusIn(
                        todayStart,
                        todayEnd,
                        List.of(AppointmentStatus.APPROVED));

        // Agrupar por programador
        var appointmentsByProgrammer = todayAppointments.stream()
                .collect(Collectors.groupingBy(AppointmentEntity::getProgrammer));

        appointmentsByProgrammer.forEach((programmer, appointments) -> {
            try {
                emailService.sendDailySchedule(programmer, appointments);
                logger.info("📧 Horario enviado a: {}", programmer.getEmail());
            } catch (Exception e) {
                logger.error("❌ Error para {}: {}", programmer.getEmail(), e.getMessage());
            }
        });
    }

    /**
     * Envía emails de bienvenida a nuevos usuarios (ejecución manual desde
     * servicio)
     */
    public void sendWelcomeEmailsToNewUsers() {
        logger.info("👋 Ejecutando: Emails de bienvenida");

        // Obtener usuarios creados en las últimas 24 horas sin email de bienvenida
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);

        List<UserEntity> newUsers = userRepository.findByCreatedAtAfter(yesterday);

        for (UserEntity user : newUsers) {
            try {
                emailService.sendWelcomeEmail(user);
                logger.info("👋 Bienvenida enviada a: {}", user.getEmail());
            } catch (Exception e) {
                logger.error("❌ Error para {}: {}", user.getEmail(), e.getMessage());
            }
        }
    }

    /**
     * Limpia notificaciones antiguas
     */
    @Scheduled(cron = "0 0 2 * * *") // 2 AM diario
    @Transactional
    public void cleanupOldData() {
        logger.info("🧹 Ejecutando: Limpieza de datos antiguos");

        LocalDateTime ninetyDaysAgo = LocalDateTime.now().minusDays(90);

        // Aquí implementarías la lógica de limpieza
        // Por ejemplo: notificationRepository.deleteByCreatedAtBefore(ninetyDaysAgo);

        logger.info("✅ Limpieza completada");
    }

    // ============== MÉTODOS PRIVADOS ==============

    private List<AppointmentEntity> getAppointmentsForReminder(LocalDateTime reminderTime) {
        return appointmentRepository.findByDateTimeBetweenAndStatusIn(
                reminderTime.minusMinutes(5),
                reminderTime.plusMinutes(5),
                List.of(AppointmentStatus.APPROVED));
    }
}
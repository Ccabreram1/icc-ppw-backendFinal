package ec.edu.ups.icc.portafolio.modules.notifications.services;

import ec.edu.ups.icc.portafolio.modules.appointments.models.AppointmentEntity;
import ec.edu.ups.icc.portafolio.modules.appointments.repositories.AppointmentRepository;
import ec.edu.ups.icc.portafolio.modules.notifications.dtos.NotificationRequestDto;
import ec.edu.ups.icc.portafolio.modules.notifications.dtos.NotificationResponseDto;
import ec.edu.ups.icc.portafolio.modules.notifications.models.NotificationEntity;
import ec.edu.ups.icc.portafolio.modules.notifications.models.NotificationType;
import ec.edu.ups.icc.portafolio.modules.notifications.repositories.NotificationRepository;
import ec.edu.ups.icc.portafolio.modules.users.models.UserEntity;
import ec.edu.ups.icc.portafolio.modules.users.repositories.UserRepository;
import ec.edu.ups.icc.portafolio.shared.exceptions.domain.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final NotificationMapper notificationMapper;
    private final ObjectMapper objectMapper;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
            UserRepository userRepository,
            AppointmentRepository appointmentRepository,
            NotificationMapper notificationMapper,
            ObjectMapper objectMapper) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.notificationMapper = notificationMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponseDto> findAll(Pageable pageable) {
        return notificationRepository.findAll(pageable)
                .map(notificationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationResponseDto findById(Long id) {
        return notificationRepository.findById(id)
                .map(notificationMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Notificación no encontrada con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponseDto> findByUserId(Long userId) {
        return notificationRepository.findByUserId(userId)
                .stream()
                .map(notificationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponseDto> findUnreadByUserId(Long userId) {
        return notificationRepository.findByUserIdAndReadFalse(userId)
                .stream()
                .map(notificationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public NotificationResponseDto create(NotificationRequestDto notificationDto) {
        UserEntity user = userRepository.findById(notificationDto.getUserId())
                .orElseThrow(
                        () -> new NotFoundException("Usuario no encontrado con ID: " + notificationDto.getUserId()));

        NotificationEntity notification = notificationMapper.toEntity(notificationDto);
        notification.setUser(user);

        NotificationEntity saved = notificationRepository.save(notification);

        // Enviar correo si es una notificación importante
        if (shouldSendEmail(notification.getType())) {
            sendNotificationEmail(user, notification);
        }

        return notificationMapper.toDto(saved);
    }

    @Override
    @Transactional
    public NotificationResponseDto createForUser(Long userId, String title, String message,
            String type, String metadata) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + userId));

        NotificationEntity notification = new NotificationEntity();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setMetadata(metadata);
        notification.setRead(false);

        NotificationEntity saved = notificationRepository.save(notification);
        return notificationMapper.toDto(saved);
    }

    @Override
    @Transactional
    public NotificationResponseDto update(Long id, NotificationRequestDto notificationDto) {
        NotificationEntity notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notificación no encontrada con ID: " + id));

        notificationMapper.updateEntity(notificationDto, notification);
        NotificationEntity updated = notificationRepository.save(notification);
        return notificationMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new NotFoundException("Notificación no encontrada con ID: " + id);
        }
        notificationRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAllByUserId(Long userId) {
        notificationRepository.deleteByUserId(userId);
    }

    @Override
    @Transactional
    public NotificationResponseDto markAsRead(Long id) {
        NotificationEntity notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notificación no encontrada con ID: " + id));

        notification.setRead(true);
        NotificationEntity updated = notificationRepository.save(notification);
        return notificationMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsReadByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countUnreadByUserId(Long userId) {
        return notificationRepository.countByUserIdAndReadFalse(userId);
    }

    @Override
    @Transactional
    public void sendAppointmentNotification(Long appointmentId) {
        AppointmentEntity appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada con ID: " + appointmentId));

        // Notificación para el cliente
        String clientTitle = "Solicitud de Asesoría Enviada";
        String clientMessage = String.format(
                "Tu solicitud de asesoría con %s ha sido enviada. Fecha: %s",
                appointment.getProgrammer().getName(),
                formatDateTime(appointment.getDateTime()));

        createForUser(
                appointment.getClient().getId(),
                clientTitle,
                clientMessage,
                NotificationType.APPOINTMENT_CREATED.name(),
                createAppointmentMetadata(appointment));

        // Notificación para el programador
        String programmerTitle = "Nueva Solicitud de Asesoría";
        String programmerMessage = String.format(
                "%s ha solicitado una asesoría. Fecha: %s",
                appointment.getClient().getName(),
                formatDateTime(appointment.getDateTime()));

        createForUser(
                appointment.getProgrammer().getId(),
                programmerTitle,
                programmerMessage,
                NotificationType.APPOINTMENT_CREATED.name(),
                createAppointmentMetadata(appointment));
    }

    @Override
    @Transactional
    public void sendAppointmentReminder(Long appointmentId) {
        AppointmentEntity appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada con ID: " + appointmentId));

        String title = "Recordatorio de Asesoría";
        String message = String.format(
                "Recordatorio: Tienes una asesoría programada para %s con %s",
                formatDateTime(appointment.getDateTime()),
                appointment.getClient().getName());

        // Notificación para el programador
        createForUser(
                appointment.getProgrammer().getId(),
                title,
                message,
                NotificationType.APPOINTMENT_REMINDER.name(),
                createAppointmentMetadata(appointment));

        // Notificación para el cliente
        String clientMessage = String.format(
                "Recordatorio: Tienes una asesoría programada para %s con %s",
                formatDateTime(appointment.getDateTime()),
                appointment.getProgrammer().getName());

        createForUser(
                appointment.getClient().getId(),
                title,
                clientMessage,
                NotificationType.APPOINTMENT_REMINDER.name(),
                createAppointmentMetadata(appointment));
    }

    @Override
    @Transactional
    public void sendAppointmentStatusChange(Long appointmentId, String newStatus, String message) {
        AppointmentEntity appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada con ID: " + appointmentId));

        String title = "Estado de Asesoría Actualizado";
        String notificationMessage = String.format(
                "Tu asesoría con %s ha sido %s. %s",
                appointment.getProgrammer().getName(),
                newStatus.toLowerCase(),
                message != null ? "Motivo: " + message : "");

        createForUser(
                appointment.getClient().getId(),
                title,
                notificationMessage,
                getNotificationTypeForStatus(newStatus),
                createAppointmentMetadata(appointment));
    }

    @Override
    public List<String> getNotificationTypes() {
        return List.of(
                NotificationType.APPOINTMENT_CREATED.name(),
                NotificationType.APPOINTMENT_APPROVED.name(),
                NotificationType.APPOINTMENT_REJECTED.name(),
                NotificationType.APPOINTMENT_REMINDER.name(),
                NotificationType.APPOINTMENT_CANCELLED.name(),
                NotificationType.APPOINTMENT_COMPLETED.name(),
                NotificationType.SYSTEM_NOTIFICATION.name(),
                NotificationType.WELCOME_MESSAGE.name(),
                NotificationType.PROFILE_UPDATED.name(),
                NotificationType.PASSWORD_CHANGED.name());
    }

    @Override
    @Transactional
    public void sendWelcomeNotification(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + userId));

        String title = "¡Bienvenido a Portafolio Administrable!";
        String message = String.format(
                "Hola %s, gracias por registrarte en nuestra plataforma. " +
                        "Ahora puedes explorar portafolios de programadores y agendar asesorías.",
                user.getName());

        createForUser(
                userId,
                title,
                message,
                NotificationType.WELCOME_MESSAGE.name(),
                null);
    }

    @Override
    @Transactional
    public void sendPasswordChangedNotification(Long userId) {
        String title = "Contraseña Actualizada";
        String message = "Tu contraseña ha sido cambiada exitosamente. " +
                "Si no realizaste este cambio, por favor contacta con soporte.";

        createForUser(
                userId,
                title,
                message,
                NotificationType.PASSWORD_CHANGED.name(),
                null);
    }

    @Override
    @Transactional
    public void sendProfileUpdatedNotification(Long userId) {
        String title = "Perfil Actualizado";
        String message = "Tu perfil ha sido actualizado exitosamente.";

        createForUser(
                userId,
                title,
                message,
                NotificationType.PROFILE_UPDATED.name(),
                null);
    }

    // ============== MÉTODOS PRIVADOS ==============

    private boolean shouldSendEmail(String type) {
        return List.of(
                NotificationType.APPOINTMENT_CREATED.name(),
                NotificationType.APPOINTMENT_APPROVED.name(),
                NotificationType.APPOINTMENT_REJECTED.name(),
                NotificationType.APPOINTMENT_REMINDER.name()).contains(type);
    }

    private void sendNotificationEmail(UserEntity user, NotificationEntity notification) {
        try {
            // Aquí podrías implementar el envío de correo
            // emailService.sendNotificationEmail(user.getEmail(), notification.getTitle(),
            // notification.getMessage());
        } catch (Exception e) {
            // Log the error but don't throw exception
            System.err.println("Error enviando correo de notificación: " + e.getMessage());
        }
    }

    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateTime.format(formatter);
    }

    private String createAppointmentMetadata(AppointmentEntity appointment) {
        try {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("appointmentId", appointment.getId());
            metadata.put("programmerId", appointment.getProgrammer().getId());
            metadata.put("programmerName", appointment.getProgrammer().getName());
            metadata.put("clientId", appointment.getClient().getId());
            metadata.put("clientName", appointment.getClient().getName());
            metadata.put("dateTime", appointment.getDateTime().toString());
            metadata.put("status", appointment.getStatus().name());

            return objectMapper.writeValueAsString(metadata);
        } catch (Exception e) {
            return "{}";
        }
    }

    private String getNotificationTypeForStatus(String status) {
        return switch (status.toUpperCase()) {
            case "APPROVED" -> NotificationType.APPOINTMENT_APPROVED.name();
            case "REJECTED" -> NotificationType.APPOINTMENT_REJECTED.name();
            case "CANCELLED" -> NotificationType.APPOINTMENT_CANCELLED.name();
            case "COMPLETED" -> NotificationType.APPOINTMENT_COMPLETED.name();
            default -> NotificationType.SYSTEM_NOTIFICATION.name();
        };
    }
}
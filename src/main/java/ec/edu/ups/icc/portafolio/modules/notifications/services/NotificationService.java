package ec.edu.ups.icc.portafolio.modules.notifications.services;

import ec.edu.ups.icc.portafolio.modules.notifications.dtos.NotificationRequestDto;
import ec.edu.ups.icc.portafolio.modules.notifications.dtos.NotificationResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {

    // CRUD operations
    Page<NotificationResponseDto> findAll(Pageable pageable);

    NotificationResponseDto findById(Long id);

    List<NotificationResponseDto> findByUserId(Long userId);

    List<NotificationResponseDto> findUnreadByUserId(Long userId);

    NotificationResponseDto create(NotificationRequestDto notificationDto);

    NotificationResponseDto createForUser(Long userId, String title, String message, String type, String metadata);

    NotificationResponseDto update(Long id, NotificationRequestDto notificationDto);

    void delete(Long id);

    void deleteAllByUserId(Long userId);

    // Status operations
    NotificationResponseDto markAsRead(Long id);

    void markAllAsRead(Long userId);

    long countUnreadByUserId(Long userId);

    // Business logic
    void sendAppointmentNotification(Long appointmentId);

    void sendAppointmentReminder(Long appointmentId);

    void sendAppointmentStatusChange(Long appointmentId, String newStatus, String message);

    // Utility
    List<String> getNotificationTypes();

    void sendWelcomeNotification(Long userId);

    void sendPasswordChangedNotification(Long userId);

    void sendProfileUpdatedNotification(Long userId);
}
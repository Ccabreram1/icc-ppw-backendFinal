package ec.edu.ups.icc.portafolio.modules.notifications.controllers;

import ec.edu.ups.icc.portafolio.modules.notifications.dtos.NotificationRequestDto;
import ec.edu.ups.icc.portafolio.modules.notifications.dtos.NotificationResponseDto;
import ec.edu.ups.icc.portafolio.modules.notifications.services.NotificationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<NotificationResponseDto>> getAllNotifications(Pageable pageable) {
        return ResponseEntity.ok(notificationService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @notificationSecurity.isOwner(#id)")
    public ResponseEntity<NotificationResponseDto> getNotificationById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.findById(id));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @notificationSecurity.isUserOwner(#userId)")
    public ResponseEntity<List<NotificationResponseDto>> getNotificationsByUserId(
            @PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.findByUserId(userId));
    }

    @GetMapping("/user/{userId}/unread")
    @PreAuthorize("hasRole('ADMIN') or @notificationSecurity.isUserOwner(#userId)")
    public ResponseEntity<List<NotificationResponseDto>> getUnreadNotificationsByUserId(
            @PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.findUnreadByUserId(userId));
    }

    @GetMapping("/user/{userId}/count-unread")
    @PreAuthorize("hasRole('ADMIN') or @notificationSecurity.isUserOwner(#userId)")
    public ResponseEntity<Long> countUnreadNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.countUnreadByUserId(userId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NotificationResponseDto> createNotification(
            @Valid @RequestBody NotificationRequestDto notificationDto) {
        NotificationResponseDto created = notificationService.create(notificationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/send-appointment-notification")
    public ResponseEntity<Void> sendAppointmentNotification(
            @RequestParam Long appointmentId) {
        notificationService.sendAppointmentNotification(appointmentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send-reminder")
    public ResponseEntity<Void> sendReminderNotification(
            @RequestParam Long appointmentId) {
        notificationService.sendAppointmentReminder(appointmentId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/mark-as-read")
    @PreAuthorize("hasRole('ADMIN') or @notificationSecurity.isOwner(#id)")
    public ResponseEntity<NotificationResponseDto> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    @PutMapping("/user/{userId}/mark-all-as-read")
    @PreAuthorize("hasRole('ADMIN') or @notificationSecurity.isUserOwner(#userId)")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @notificationSecurity.isUserOwner(#userId)")
    public ResponseEntity<Void> deleteAllByUserId(@PathVariable Long userId) {
        notificationService.deleteAllByUserId(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/types")
    public ResponseEntity<List<String>> getNotificationTypes() {
        return ResponseEntity.ok(notificationService.getNotificationTypes());
    }
}
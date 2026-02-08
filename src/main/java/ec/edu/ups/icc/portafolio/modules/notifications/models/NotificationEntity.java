package ec.edu.ups.icc.portafolio.modules.notifications.models;

import java.time.LocalDateTime;

import ec.edu.ups.icc.portafolio.modules.users.models.UserEntity;
import ec.edu.ups.icc.portafolio.shared.entities.BaseModel;
import jakarta.persistence.*;

@Entity
@Table(name = "notifications")
public class NotificationEntity extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(nullable = false, length = 50)
    private String type; // APPOINTMENT_CREATED, APPOINTMENT_APPROVED, APPOINTMENT_REJECTED,
                         // APPOINTMENT_REMINDER, SYSTEM

    @Column(name = "is_read")
    private boolean read = false;

    @Column(name = "action_url", length = 500)
    private String actionUrl;

    @Column(columnDefinition = "TEXT")
    private String metadata; // JSON con datos adicionales

    @Column(name = "read_at")
    private LocalDateTime readAt;

    // Constructores
    public NotificationEntity() {
    }

    public NotificationEntity(UserEntity user, String title, String message, String type) {
        this.user = user;
        this.title = title;
        this.message = message;
        this.type = type;
    }

    // Getters y Setters
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
        if (read && this.readAt == null) {
            this.readAt = LocalDateTime.now();
        }
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }
}
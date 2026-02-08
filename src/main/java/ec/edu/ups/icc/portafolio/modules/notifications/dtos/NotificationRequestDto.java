package ec.edu.ups.icc.portafolio.modules.notifications.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class NotificationRequestDto {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "El título no puede exceder 200 caracteres")
    private String title;

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(max = 1000, message = "El mensaje no puede exceder 1000 caracteres")
    private String message;

    @NotBlank(message = "El tipo de notificación es obligatorio")
    private String type;

    private String actionUrl;
    private String metadata;

    // Getters y Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
}
package ec.edu.ups.icc.portafolio.modules.notifications.models;

public enum NotificationType {
    APPOINTMENT_CREATED("Nueva solicitud de asesoría"),
    APPOINTMENT_APPROVED("Asesoría aprobada"),
    APPOINTMENT_REJECTED("Asesoría rechazada"),
    APPOINTMENT_REMINDER("Recordatorio de asesoría"),
    APPOINTMENT_CANCELLED("Asesoría cancelada"),
    APPOINTMENT_COMPLETED("Asesoría completada"),
    SYSTEM_NOTIFICATION("Notificación del sistema"),
    WELCOME_MESSAGE("Mensaje de bienvenida"),
    PROFILE_UPDATED("Perfil actualizado"),
    PASSWORD_CHANGED("Contraseña cambiada");

    private final String description;

    NotificationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static NotificationType fromString(String type) {
        for (NotificationType notificationType : NotificationType.values()) {
            if (notificationType.name().equalsIgnoreCase(type)) {
                return notificationType;
            }
        }
        return SYSTEM_NOTIFICATION;
    }
}
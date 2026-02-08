package ec.edu.ups.icc.portafolio.modules.notifications.security;

import ec.edu.ups.icc.portafolio.modules.notifications.repositories.NotificationRepository;
import ec.edu.ups.icc.portafolio.modules.users.services.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("notificationSecurity")
public class NotificationSecurity {

    private final NotificationRepository notificationRepository;

    public NotificationSecurity(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public boolean isOwner(Long notificationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetailsImpl)) {
            return false;
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) principal;
        Long userId = userDetails.getId();

        return notificationRepository.findById(notificationId)
                .map(notification -> notification.getUser().getId().equals(userId))
                .orElse(false);
    }

    public boolean isUserOwner(Long targetUserId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetailsImpl)) {
            return false;
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) principal;
        Long currentUserId = userDetails.getId();

        // El usuario puede ver sus propias notificaciones
        // O si es ADMIN puede ver cualquier notificación
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        return currentUserId.equals(targetUserId) || isAdmin;
    }
}
package ec.edu.ups.icc.portafolio.config.security;

import ec.edu.ups.icc.portafolio.modules.users.services.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {
    
    public boolean isSelf(Long targetUserId) {
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
        
        // Verificar si el usuario actual es el mismo que el objetivo
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        
        return currentUserId.equals(targetUserId) || isAdmin;
    }
}
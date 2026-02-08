package ec.edu.ups.icc.portafolio.config.security;

import ec.edu.ups.icc.portafolio.modules.availabilities.repositories.AvailabilityRepository;
import ec.edu.ups.icc.portafolio.modules.users.services.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("availabilitySecurity")
public class AvailabilitySecurity {
    
    private final AvailabilityRepository availabilityRepository;
    
    public AvailabilitySecurity(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }
    
    public boolean isOwner(Long availabilityId) {
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
        
        // Verificar si el usuario actual es el programador de la disponibilidad
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        
        return availabilityRepository.findById(availabilityId)
                .map(availability -> availability.getProgrammer().getId().equals(currentUserId) || isAdmin)
                .orElse(false);
    }
}
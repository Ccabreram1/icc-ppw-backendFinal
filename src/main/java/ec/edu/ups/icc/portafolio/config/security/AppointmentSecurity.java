package ec.edu.ups.icc.portafolio.config.security;

import ec.edu.ups.icc.portafolio.modules.appointments.repositories.AppointmentRepository;
import ec.edu.ups.icc.portafolio.modules.users.services.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("appointmentSecurity")
public class AppointmentSecurity {
    
    private final AppointmentRepository appointmentRepository;
    
    public AppointmentSecurity(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }
    
    public boolean isProgrammer(Long programmerId) {
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
        
        // Verificar si el usuario actual es el programador
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        
        return currentUserId.equals(programmerId) || isAdmin;
    }
    
    public boolean isProgrammerForAppointment(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .map(appointment -> isProgrammer(appointment.getProgrammer().getId()))
                .orElse(false);
    }
    
    public boolean isClientForAppointment(Long appointmentId) {
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
        
        return appointmentRepository.findById(appointmentId)
                .map(appointment -> appointment.getClient().getId().equals(currentUserId))
                .orElse(false);
    }
    
    public boolean isClient(Long clientId) {
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
        
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        
        return currentUserId.equals(clientId) || isAdmin;
    }
}
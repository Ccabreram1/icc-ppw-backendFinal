package ec.edu.ups.icc.portafolio.config.security;

import ec.edu.ups.icc.portafolio.modules.projects.repositories.ProjectRepository;
import ec.edu.ups.icc.portafolio.modules.users.services.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("projectSecurity")
public class ProjectSecurity {
    
    private final ProjectRepository projectRepository;
    
    public ProjectSecurity(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
    
    public boolean isPortfolioOwner(Long projectId) {
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
        
        // Verificar si el usuario actual es el dueño del portfolio del proyecto
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        
        return projectRepository.findById(projectId)
                .map(project -> project.getPortfolio().getUser().getId().equals(currentUserId) || isAdmin)
                .orElse(false);
    }
}
package ec.edu.ups.icc.portafolio.config.security;

import ec.edu.ups.icc.portafolio.modules.portfolios.repositories.PortfolioRepository;
import ec.edu.ups.icc.portafolio.modules.users.services.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("portfolioSecurity")
public class PortfolioSecurity {
    
    private final PortfolioRepository portfolioRepository;
    
    public PortfolioSecurity(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }
    
    public boolean isOwner(Long portfolioId) {
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
        
        // Verificar si el usuario actual es el dueño del portfolio
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        
        return portfolioRepository.findById(portfolioId)
                .map(portfolio -> portfolio.getUser().getId().equals(currentUserId) || isAdmin)
                .orElse(false);
    }
}
package ec.edu.ups.icc.portafolio.config.security;

import ec.edu.ups.icc.portafolio.modules.users.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtProperties jwtProperties;

    public JwtAuthenticationFilter(JwtUtil jwtUtil,
            UserDetailsServiceImpl userDetailsService,
            JwtProperties jwtProperties) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        
        try {
            // ✅ AGREGAR: Verificar si es un endpoint público ANTES de procesar JWT
            String path = request.getServletPath();
            
            // Lista de endpoints públicos que NO requieren autenticación
            if (isPublicEndpoint(path)) {
                filterChain.doFilter(request, response);
                return;
            }
            
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && jwtUtil.validateToken(jwt)) {
                String email = jwtUtil.getEmailFromToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("Usuario autenticado: {}", email);
            }

        } catch (Exception ex) {
            logger.error("No se pudo establecer la autenticación del usuario", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtProperties.getHeader());

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtProperties.getPrefix())) {
            return bearerToken.substring(jwtProperties.getPrefix().length());
        }

        return null;
    }
    
    // ✅ MÉTODO NUEVO: Identificar endpoints públicos
    private boolean isPublicEndpoint(String path) {
        // Lista de endpoints que NO requieren token
        return path.startsWith("/api/auth/") ||
               path.equals("/api/public/") ||
               path.startsWith("/swagger-ui/") ||
               path.startsWith("/v3/api-docs/");
    }
}
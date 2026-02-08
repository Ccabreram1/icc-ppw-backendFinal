package ec.edu.ups.icc.portafolio.shared.exceptions.handler;

import ec.edu.ups.icc.portafolio.shared.exceptions.base.ApplicationException;
import ec.edu.ups.icc.portafolio.shared.exceptions.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(
            ApplicationException ex,
            HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                ex.getStatus(),
                ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult()
            .getFieldErrors()
            .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Datos de entrada inválidos",
                request.getRequestURI(),
                errors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(
            BadCredentialsException ex,
            HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Credenciales inválidas",
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex,
            HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.FORBIDDEN,
                "Acceso denegado. No tienes permisos para esta acción",
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException ex,
            HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Autenticación requerida",
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(
            Exception ex,
            HttpServletRequest request) {
        ex.printStackTrace(); // Log the exception
        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor",
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
package ec.edu.ups.icc.portafolio.shared.exceptions.domain;

import ec.edu.ups.icc.portafolio.shared.exceptions.base.ApplicationException;
import org.springframework.http.HttpStatus;

public class ConflictException extends ApplicationException {
    
    public ConflictException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
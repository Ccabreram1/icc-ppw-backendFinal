package ec.edu.ups.icc.portafolio.shared.exceptions.domain;

import ec.edu.ups.icc.portafolio.shared.exceptions.base.ApplicationException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends ApplicationException {
    
    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
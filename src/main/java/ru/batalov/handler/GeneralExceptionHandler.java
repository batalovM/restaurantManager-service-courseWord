package ru.batalov.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.batalov.exception.ConstraintException;
import ru.batalov.exception.EntityNotExistException;
import ru.batalov.exception.ServiceException;

/**
 * @author batal
 * @Date 08.11.2025
 */
@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(EntityNotExistException.class)
    public ResponseEntity<Object> handleEntityNotExistException(final EntityNotExistException e) {
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }
    @ExceptionHandler(ConstraintException.class)
    public ResponseEntity<Object> handleConstraintException(final ConstraintException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> handleServiceException(final ServiceException e) {
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }
}


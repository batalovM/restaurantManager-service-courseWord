package ru.batalov.exception;

import org.springframework.http.HttpStatus;

/**
 * @author batal
 * @Date 08.11.2025
 */
public class EntityNotExistException extends RuntimeException {

    private final HttpStatus status;

    public HttpStatus getStatus() {
        return status;
    }

    public EntityNotExistException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public EntityNotExistException(HttpStatus status, String format, Object... args) {
        super(String.format(format, args));
        this.status = status;
    }
}

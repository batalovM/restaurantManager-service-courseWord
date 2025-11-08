package ru.batalov.exception;

import org.springframework.http.HttpStatus;

import java.util.Collection;

/**
 * @author batal
 * @Date 08.11.2025
 */
public class ServiceException extends Exception {
    private final HttpStatus status;

    public HttpStatus getStatus() {
        return status;
    }

    public ServiceException(HttpStatus status, Collection<String> errors) {
        super(String.join("\n", errors));
        this.status = status;
    }

    public ServiceException(HttpStatus status, String format, Object... args) {
        super(String.format(format, args));
        this.status = status;
    }

    public ServiceException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}

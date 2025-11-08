package ru.batalov.exception;

/**
 * @author batal
 * @Date 08.11.2025
 */
public class ConstraintException extends RuntimeException {

    public ConstraintException(String message) {
        super(message);
    }

    public ConstraintException(String format, Object... args) {
        super(String.format(format, args));
    }
}


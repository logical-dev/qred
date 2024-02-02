package se.qred.loan.exception;

/**
 * @author shahbazhussain
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
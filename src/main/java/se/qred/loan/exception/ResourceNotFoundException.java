package se.qred.loan.exception;

/**
 * @author shahbazhussain
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
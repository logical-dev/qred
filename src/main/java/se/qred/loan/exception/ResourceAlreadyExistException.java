package se.qred.loan.exception;

/**
 * @author shahbazhussain
 */
public class ResourceAlreadyExistException extends RuntimeException {
    public ResourceAlreadyExistException(String message) {
        super(message);
    }
}
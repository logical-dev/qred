package se.qred.loan.exception;

/**
 * @author shahbazhussain
 */
public class OperationFailureException extends RuntimeException {
    public OperationFailureException(String message) {
        super(message);
    }
}
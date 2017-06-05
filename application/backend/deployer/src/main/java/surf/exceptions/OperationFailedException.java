package surf.exceptions;

public class OperationFailedException extends RuntimeException {
    public OperationFailedException(Throwable cause) {
        super(cause);
    }
}

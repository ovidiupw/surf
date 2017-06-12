package models.exceptions;

import javax.annotation.Nonnull;

public class InternalServerException extends RuntimeException {
    private static final String ERROR_500 = "Error.500 ";


    public InternalServerException(@Nonnull final String message) {
        super(ERROR_500 + message);
    }
}

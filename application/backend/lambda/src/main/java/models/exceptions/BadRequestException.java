package models.exceptions;

import javax.annotation.Nonnull;

public class BadRequestException extends RuntimeException {
    private static final String ERROR_400 = "Error.400 ";

    public BadRequestException(@Nonnull final String message) {
        super(ERROR_400 + message);
    }
}

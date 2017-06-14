package models.validators;

import models.exceptions.BadRequestException;
import models.exceptions.InternalServerException;

import javax.annotation.Nonnull;

public interface Validator<T> {
    void validate(@Nonnull final T object) throws BadRequestException, InternalServerException;
}

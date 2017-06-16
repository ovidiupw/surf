package interpolators;

import models.exceptions.BadRequestException;
import models.exceptions.InternalServerException;

import javax.annotation.Nonnull;

public interface Interpolator<T> {
    T interpolate(@Nonnull final T object) throws BadRequestException, InternalServerException;
}

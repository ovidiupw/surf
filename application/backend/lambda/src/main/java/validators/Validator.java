package validators;

import javax.annotation.Nonnull;

public interface Validator<T> {
    void validate(@Nonnull final T object) throws RuntimeException;
}

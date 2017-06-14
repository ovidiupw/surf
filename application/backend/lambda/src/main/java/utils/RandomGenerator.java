package utils;

import javax.annotation.Nonnull;
import java.util.UUID;

public class RandomGenerator {

    private RandomGenerator() {
        throw new UnsupportedOperationException("This class is not instantiable!");
    }

    public static String randomUUIDWithSeparatedSuffix(@Nonnull final String separator, @Nonnull final String suffix) {
        return String.format("%s%s%s", UUID.randomUUID(), separator, suffix);
    }

}

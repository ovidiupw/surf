package utils;

import java.util.UUID;

public class RandomGenerator {

    private RandomGenerator() {
        throw new UnsupportedOperationException("This class is not instantiable!");
    }

    public static String randomUUID() {
        return String.join("-", UUID.randomUUID().toString(), String.valueOf(System.currentTimeMillis()));
    }

}

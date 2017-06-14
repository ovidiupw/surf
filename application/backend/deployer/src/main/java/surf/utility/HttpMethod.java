package surf.utility;

import com.google.common.base.Preconditions;
import org.apache.logging.log4j.util.Strings;

import javax.annotation.Nonnull;

public enum HttpMethod {
    PUT("PUT"),
    POST("POST"),
    GET("GET"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS");

    private final String name;

    HttpMethod(@Nonnull final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static HttpMethod fromName(@Nonnull final String name) {
        for (final HttpMethod httpMethod : HttpMethod.values()) {
            if (httpMethod.getName().equals(name)) {
                return httpMethod;
            }
        }

        throw new IllegalArgumentException("There is no associated HttpMethod for the supplied value!");
    }

    @Override
    public String toString() {
        return "HttpMethod{" +
                "name='" + name + '\'' +
                '}';
    }
}

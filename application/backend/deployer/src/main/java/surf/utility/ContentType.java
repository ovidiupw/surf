package surf.utility;

import com.google.common.base.Preconditions;
import org.apache.logging.log4j.util.Strings;

import javax.annotation.Nonnull;

public enum ContentType {
    JSON("application/json"),
    JSON_UTF8("application/json; charset=utf-8");

    private final String name;

    ContentType(@Nonnull final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ContentType fromName(@Nonnull final String name) {
        for (final ContentType contentType : ContentType.values()) {
            if (contentType.getName().equals(name)) {
                return contentType;
            }
        }

        throw new IllegalArgumentException("There is no associated ContentType for the supplied value!");
    }

    @Override
    public String toString() {
        return "ContentType{" +
                "name='" + name + '\'' +
                '}';
    }
}

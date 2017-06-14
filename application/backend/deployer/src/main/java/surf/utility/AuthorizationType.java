package surf.utility;

import com.google.common.base.Preconditions;
import org.apache.logging.log4j.util.Strings;

import javax.annotation.Nonnull;

public enum AuthorizationType {
    AWS_IAM("AWS_IAM"),
    CUSTOM("CUSTOM"),
    NONE("NONE");

    private final String name;

    AuthorizationType(@Nonnull final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static AuthorizationType fromName(@Nonnull final String name) {
        Preconditions.checkArgument(Strings.isNotBlank(name));

        for (final AuthorizationType authType : AuthorizationType.values()) {
            if (authType.getName().equals(name)) {
                return authType;
            }
        }

        throw new IllegalArgumentException("There is no associated AuthorizationType for the supplied value!");
    }

    @Override
    public String toString() {
        return "AuthorizationType{" +
                "name='" + name + '\'' +
                '}';
    }
}

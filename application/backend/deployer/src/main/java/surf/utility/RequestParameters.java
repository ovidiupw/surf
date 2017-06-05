package surf.utility;

import com.google.common.base.Preconditions;
import org.apache.logging.log4j.util.Strings;

import javax.annotation.Nonnull;

public final class RequestParameters {
    public static final boolean REQUIRED = true;
    public static final boolean NOT_REQUIRED = !REQUIRED;

    public static String queryStringParameter(@Nonnull final String parameterName) {
        Preconditions.checkArgument(Strings.isNotBlank(parameterName));
        return String.format("method.request.querystring.%s", parameterName);
    }

    public static String headerParameter(@Nonnull final String parameterName) {
        Preconditions.checkArgument(Strings.isNotBlank(parameterName));
        return String.format("method.response.header.%s", parameterName);
    }
}

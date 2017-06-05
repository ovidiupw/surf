package surf.utility;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ApiMethodsHelper {

    private ApiMethodsHelper() {
        throw new UnsupportedOperationException("Use static methods instead of constructing an instance of this class");
    }

    public static Map<String, String> buildVelocityTemplatesForIntegration(
            @Nonnull final List<ContentType> contentTypes,
            @Nonnull final String methodIntegration) {
        final Map<String, String> templates = new HashMap<>();

        for (final ContentType contentType : contentTypes) {
            templates.put(contentType.getName(), methodIntegration);
        }
        return templates;
    }

    public static Map<String, Boolean> buildCrossOriginMethodResponseParameters() {
        return Collections.singletonMap(
                RequestParameters.headerParameter("Access-Control-Allow-Origin"), false);
    }

    public static Map<String, String> buildCrossOriginIntegrationResponseParameters() {
        return Collections.singletonMap(
                RequestParameters.headerParameter("Access-Control-Allow-Origin"), "'*'");
    }
}

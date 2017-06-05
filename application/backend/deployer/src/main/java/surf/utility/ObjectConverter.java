package surf.utility;

import com.amazonaws.services.apigateway.model.CreateResourceResult;
import com.amazonaws.services.apigateway.model.Resource;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import java.util.List;

public class ObjectConverter {

    private ObjectConverter() {
        throw new UnsupportedOperationException("This class is non instantiable. Use the static methods instead.");
    }

    public static Resource toApiResource(@Nonnull final CreateResourceResult createResult) {
        Preconditions.checkNotNull(createResult);

        Resource resource = new Resource();
        resource.setId(createResult.getId());
        resource.setParentId(createResult.getParentId());
        resource.setPath(createResult.getPath());
        resource.setPathPart(createResult.getPathPart());
        resource.setResourceMethods(createResult.getResourceMethods());

        return resource;
    }

    public static String toCSVInnerString(@Nonnull final List<HttpMethod> allowedMethods) {
        Preconditions.checkNotNull(allowedMethods);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("'");
        for (final HttpMethod allowedMethod : allowedMethods) {
            stringBuilder.append(allowedMethod.getName());
            stringBuilder.append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1); // Remove the last dangling comma
        stringBuilder.append("'");
        return stringBuilder.toString();
    }
}

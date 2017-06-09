package surf.deployers.api.resources;

import com.amazonaws.services.apigateway.model.Resource;

import javax.annotation.Nonnull;

public interface ResourceCreator {
    Resource create(@Nonnull final Resource parentResource, @Nonnull final String pathPart);
}

package surf.deployers.api;

import com.amazonaws.services.apigateway.model.Resource;

import javax.annotation.Nonnull;

interface ResourceCreator {
    Resource create(@Nonnull final Resource parentResource, @Nonnull final String pathPart);
}

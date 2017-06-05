package surf.deployers.api;

import com.amazonaws.services.apigateway.AmazonApiGateway;
import com.amazonaws.services.apigateway.model.*;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.exceptions.OperationFailedException;
import surf.utility.HttpMethod;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class SkeletalResourceCreator implements ResourceCreator {

    private static final Logger LOG = LoggerFactory.getLogger(SkeletalResourceCreator.class);

    protected final CreateRestApiResult restApi;
    protected final AmazonApiGateway apiClient;

    public SkeletalResourceCreator(
            @Nonnull final CreateRestApiResult restApi,
            @Nonnull final AmazonApiGateway apiClient) {
        Preconditions.checkNotNull(restApi);
        Preconditions.checkNotNull(apiClient);

        this.restApi = restApi;
        this.apiClient = apiClient;
    }

    protected CreateResourceResult createApiResource(
            @Nonnull final Resource parentResource,
            @Nonnull final String pathPart) {
        try {
            LOG.info("Trying to create api resource: apiId='{}', apiName='{}', parentPath='{}', resourcePathPart='{}'",
                    restApi.getId(),
                    restApi.getName(),
                    parentResource.getPath(),
                    pathPart);
            return apiClient.createResource(new CreateResourceRequest()
                    .withParentId(parentResource.getId())
                    .withRestApiId(restApi.getId())
                    .withPathPart(pathPart));
        } catch (UnauthorizedException
                | NotFoundException
                | ConflictException
                | LimitExceededException
                | BadRequestException
                | TooManyRequestsException e) {
            LOG.error(String.format("Could not create api resource with path-part='%s'!", pathPart), e);
            throw new OperationFailedException(e);
        }
    }

    protected GetMethodResult createOptionsMethod(
            @Nonnull final Resource resource,
            @Nonnull final List<HttpMethod> corsAllowedMethods) {
        LOG.info("Trying to create OPTIONS method for the '{}' api resource...", resource.getPath());
        final OptionsMethodCreator optionsMethodCreator = new OptionsMethodCreator(restApi, apiClient);
        return optionsMethodCreator.create(resource, corsAllowedMethods);
    }
}

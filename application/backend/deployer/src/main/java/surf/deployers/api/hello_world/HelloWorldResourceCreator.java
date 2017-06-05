package surf.deployers.api.hello_world;

import com.amazonaws.services.apigateway.AmazonApiGateway;
import com.amazonaws.services.apigateway.model.CreateRestApiResult;
import com.amazonaws.services.apigateway.model.GetMethodResult;
import com.amazonaws.services.apigateway.model.Resource;
import com.google.common.base.Preconditions;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.deployers.DeployerConfiguration;
import surf.deployers.api.SkeletalResourceCreator;
import surf.deployment.Context;
import surf.utility.HttpMethod;
import surf.utility.ObjectConverter;

import javax.annotation.Nonnull;
import java.util.Collections;

public class HelloWorldResourceCreator extends SkeletalResourceCreator {

    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldResourceCreator.class);

    private final DeployerConfiguration deployerConfiguration;
    private final Context context;

    public HelloWorldResourceCreator(@Nonnull final CreateRestApiResult restApi,
                                     @Nonnull final AmazonApiGateway apiClient,
                                     @Nonnull final DeployerConfiguration deployerConfiguration,
                                     @Nonnull final Context context) {
        super(restApi, apiClient);
        this.deployerConfiguration = deployerConfiguration;
        this.context = context;
    }

    @Override
    public Resource create(@Nonnull final Resource parentResource, @Nonnull final String pathPart) {
        Preconditions.checkNotNull(pathPart);
        Preconditions.checkArgument(Strings.isNotBlank(pathPart));

        // Use the inherited skeletal implementation of createApiResource in order to create this resource
        final Resource resource = ObjectConverter.toApiResource(
                this.createApiResource(parentResource, pathPart));

        // Use the skeletal implementation of createOptionsMethod in order to add CORS support to this resource
        final GetMethodResult optionsMethod = this.createOptionsMethod(
                resource, Collections.singletonList(HttpMethod.GET));

        final GetMethodResult getMethod = this.createGetMethod(resource);

        return resource;
    }

    private GetMethodResult createGetMethod(@Nonnull final Resource resource) {
        LOG.info("Trying to create GET method for the '{}' api resource...", resource.getPath());

        final GetMethodCreator getMethodCreator = new GetMethodCreator(restApi, apiClient);
        final GetMethodResult getMethodResult = getMethodCreator.create(
                resource,
                deployerConfiguration.getApiGatewayLambdaFunctionsPath(),
                context.getLambdaFunctionsData());

        LOG.info("Successfully created GET method for the '{}' api resource!", resource.getPath());
        return getMethodResult;
    }
}

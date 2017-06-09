package surf.deployers.api.resources;

import com.amazonaws.services.apigateway.AmazonApiGateway;
import com.amazonaws.services.apigateway.model.CreateRestApiResult;
import com.amazonaws.services.apigateway.model.Resource;
import com.google.common.base.Preconditions;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.deployers.DeployerConfiguration;
import surf.deployment.Context;
import surf.utility.HttpMethod;
import surf.utility.ObjectConverter;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;

public class WorkflowsResourceCreator extends SkeletalResourceCreator {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowsResourceCreator.class);

    private static final String GET_INTEGRATION_TEMPLATE_FILE_PATH
            = "src/main/resources/api/integration_templates/workflows.get.template";
    private static final String GET_METHOD_FRIENDLY_NAME
            = "ListWorkflows";

    private static final String POST_INTEGRATION_TEMPLATE_FILE_PATH
            = "src/main/resources/api/integration_templates/workflows.post.template";
    private static final String POST_METHOD_FRIENDLY_NAME
            = "StartWorkflow";

    private final DeployerConfiguration deployerConfiguration;
    private final Context context;

    public WorkflowsResourceCreator(@Nonnull final CreateRestApiResult restApi,
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
        this.createOptionsMethod(resource, Arrays.asList(HttpMethod.GET, HttpMethod.POST));
        this.createGetMethod(
                deployerConfiguration,
                resource,
                GET_INTEGRATION_TEMPLATE_FILE_PATH,
                GET_METHOD_FRIENDLY_NAME,
                context.getLambdaFunctionsData().getListWorkflowsData());
        this.createPostMethod(deployerConfiguration,
                resource,
                POST_INTEGRATION_TEMPLATE_FILE_PATH,
                POST_METHOD_FRIENDLY_NAME,
                context.getLambdaFunctionsData().getStartWorkflowData());

        return resource;
    }
}

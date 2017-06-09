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
import java.util.Collections;

public class WorkflowsByIdResourceCreator extends SkeletalResourceCreator {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowsResourceCreator.class);

    private static final String GET_INTEGRATION_TEMPLATE_FILE_PATH
            = "src/main/resources/api/integration_templates/workflows_by_id.get.template";
    private static final String GET_METHOD_FRIENDLY_NAME
            = "GetWorkflow";

    private final DeployerConfiguration deployerConfiguration;
    private final Context context;

    public WorkflowsByIdResourceCreator(@Nonnull final CreateRestApiResult restApi,
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
        this.createOptionsMethod(resource, Collections.singletonList(HttpMethod.GET));
        this.createGetMethod(
                deployerConfiguration,
                resource,
                GET_INTEGRATION_TEMPLATE_FILE_PATH,
                GET_METHOD_FRIENDLY_NAME,
                context.getLambdaFunctionsData().getGetWorkflowData());

        return resource;
    }
}

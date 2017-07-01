package surf.deployers.api.resources;

import com.amazonaws.services.apigateway.AmazonApiGateway;
import com.amazonaws.services.apigateway.model.CreateRestApiResult;
import com.amazonaws.services.apigateway.model.Resource;
import com.google.common.base.Preconditions;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.deployers.DeployerConfiguration;
import surf.deployment.Context;
import surf.utility.HttpMethod;
import surf.utility.ObjectConverter;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class S3CrawledDataResourceCreator extends SkeletalResourceCreator {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowsResourceCreator.class);

    private static final String GET_INTEGRATION_TEMPLATE_FILE_PATH
            = "src/main/resources/api/integration_templates/s3_crawled_data.get.template";
    private static final String GET_METHOD_FRIENDLY_NAME
            = "GetCrawledDataS3Url";

    private final DeployerConfiguration deployerConfiguration;
    private final Context context;

    public S3CrawledDataResourceCreator(@Nonnull final CreateRestApiResult restApi,
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

        final Map<String, Boolean> requestParameters = new HashMap<>();
        requestParameters.put("method.request.path.id", true);
        requestParameters.put("method.request.querystring.pageUrl", true);

        this.createGetMethod(
                deployerConfiguration,
                resource,
                GET_INTEGRATION_TEMPLATE_FILE_PATH,
                GET_METHOD_FRIENDLY_NAME,
                context.getLambdaFunctionsData().getGetS3PresignedUrlData(),
                context.getApiAuthorizer().getId(),
                requestParameters);

        return resource;
    }
}

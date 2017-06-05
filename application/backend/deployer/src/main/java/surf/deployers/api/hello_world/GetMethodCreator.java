package surf.deployers.api.hello_world;

import com.amazonaws.services.apigateway.AmazonApiGateway;
import com.amazonaws.services.apigateway.model.*;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.deployment.LambdaFunctionsData;
import surf.exceptions.OperationFailedException;
import surf.utility.*;

import javax.annotation.Nonnull;
import java.util.*;

class GetMethodCreator {

    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldResourceCreator.class);
    private static final String GET_METHOD_FRIENDLY_NAME = "GetHelloWorld";
    public static final String INTEGRATION_TEMPLATE_FILE_PATH
            = "src/main/resources/api/integration_templates/hello-world.get.template";

    private final CreateRestApiResult restApi;
    private final AmazonApiGateway apiClient;

    GetMethodCreator(@Nonnull final CreateRestApiResult restApi, @Nonnull final AmazonApiGateway apiClient) {
        Preconditions.checkNotNull(restApi);
        Preconditions.checkNotNull(apiClient);

        this.restApi = restApi;
        this.apiClient = apiClient;
    }

    GetMethodResult create(@Nonnull final Resource resource,
                           @Nonnull final String lambdaFunctionsPath,
                           @Nonnull final LambdaFunctionsData lambdaFunctionsData) {
        Preconditions.checkNotNull(resource);
        Preconditions.checkNotNull(lambdaFunctionsPath);
        Preconditions.checkNotNull(lambdaFunctionsData);

        try {
            putMethod(resource);
            putMethodIntegration(resource, lambdaFunctionsPath, lambdaFunctionsData);
            putMethodResponses(resource);
            putMethodIntegrationResponses(resource);

            return apiClient.getMethod(new GetMethodRequest()
                    .withRestApiId(restApi.getId())
                    .withResourceId(resource.getId())
                    .withHttpMethod(HttpMethod.GET.getName()));
        } catch (BadRequestException
                | UnauthorizedException
                | NotFoundException
                | ConflictException
                | TooManyRequestsException
                | LimitExceededException e) {
            LOG.error("Exception while creating the GET method for the '{}' api resource!", resource.getPath());
            LOG.error("Exception was...", e);
            throw new OperationFailedException(e);
        }

    }

    private List<PutIntegrationResponseResult> putMethodIntegrationResponses(@Nonnull Resource resource) {
        LOG.info("Trying to put GET method integration response for resource wth path='{}'", resource.getPath());
        List<PutIntegrationResponseResult> results = new ArrayList<>();

        final Map<String, String> dataPassThroughTemplate = new HashMap<>();
        dataPassThroughTemplate.put(ContentType.JSON.getName(), "");

        results.add(apiClient.putIntegrationResponse(new PutIntegrationResponseRequest()
                .withRestApiId(restApi.getId())
                .withResourceId(resource.getId())
                .withHttpMethod(HttpMethod.GET.getName())
                .withStatusCode("200")
                .withResponseParameters(ApiMethodsHelper.buildCrossOriginIntegrationResponseParameters())
                .withResponseTemplates(dataPassThroughTemplate)));

        LOG.info("Successfully put GET 200 method integration response for resource with path='{}'",
                resource.getPath());

        results.add(apiClient.putIntegrationResponse(new PutIntegrationResponseRequest()
                .withRestApiId(restApi.getId())
                .withResourceId(resource.getId())
                .withHttpMethod(HttpMethod.GET.getName())
                .withStatusCode("400")
                .withSelectionPattern("Error.400.*")
                .withResponseParameters(ApiMethodsHelper.buildCrossOriginIntegrationResponseParameters())
                .withResponseTemplates(dataPassThroughTemplate)));

        LOG.info("Successfully put GET 400 method integration response for resource with path='{}'",
                resource.getPath());

        return results;

    }

    private List<PutMethodResponseResult> putMethodResponses(@Nonnull Resource resource) {
        LOG.info("Trying to put GET method method-response for resource with path='{}'", resource.getPath());
        List<PutMethodResponseResult> results = new ArrayList<>();

        results.add(apiClient.putMethodResponse(new PutMethodResponseRequest()
                .withRestApiId(restApi.getId())
                .withResourceId(resource.getId())
                .withHttpMethod(HttpMethod.GET.getName())
                .withStatusCode("200")
                .withResponseModels(Collections.singletonMap(ContentType.JSON.getName(), "Empty"))
                .withResponseParameters(ApiMethodsHelper.buildCrossOriginMethodResponseParameters())));

        LOG.info("Successfully created GET 200 method method response for resource with path='{}'",
                resource.getPath());

        results.add(apiClient.putMethodResponse(new PutMethodResponseRequest()
                .withRestApiId(restApi.getId())
                .withResourceId(resource.getId())
                .withHttpMethod(HttpMethod.GET.getName())
                .withStatusCode("400")
                .withResponseModels(Collections.singletonMap(ContentType.JSON.getName(), "Empty"))
                .withResponseParameters(ApiMethodsHelper.buildCrossOriginMethodResponseParameters())));

        LOG.info("Successfully created GET 400 method method response for resource with path='{}'",
                resource.getPath());

        return results;
    }


    private PutIntegrationResult putMethodIntegration(@Nonnull Resource resource,
                                                      @Nonnull String lambdaFunctionsPath,
                                                      @Nonnull LambdaFunctionsData lambdaFunctionsData) {
        LOG.info("Trying to put GET method integration for resource with path='{}'", resource.getPath());
        final IntegrationType integrationType = IntegrationType.AWS;
        final String integrationEndpoint = lambdaFunctionsData.getHelloWorldLambdaData().getFunctionArn();
        final String integrationUri = String.format("%s%s/invocations", lambdaFunctionsPath, integrationEndpoint);
        LOG.info("Using integration with type='{}', endpoint='{}', uri='{}'",
                integrationType, integrationEndpoint, integrationUri);

        final PutIntegrationResult putIntegrationResult = apiClient.putIntegration(new PutIntegrationRequest()
                .withRestApiId(restApi.getId())
                .withResourceId(resource.getId())
                .withHttpMethod(HttpMethod.GET.getName())
                .withIntegrationHttpMethod(HttpMethod.POST.getName())
                .withType(integrationType)
                .withRequestTemplates(
                        ApiMethodsHelper.buildVelocityTemplatesForIntegration(
                                Arrays.asList(ContentType.values()),
                                readIntegrationTemplateFromFile()))
                .withUri(integrationUri));

        LOG.info("Successfully created GET method integration for resource with path='{}', " +
                        "integrationType='{}', integrationUri='{}'",
                resource.getPath(), putIntegrationResult.getType(), putIntegrationResult.getUri());

        return putIntegrationResult;
    }

    private PutMethodResult putMethod(@Nonnull Resource resource) {
        LOG.info("Trying to create GET method for the '{}' api resource...", resource.getPath());

        final PutMethodResult putMethodResult = apiClient.putMethod(new PutMethodRequest()
                .withRestApiId(restApi.getId())
                .withResourceId(resource.getId())
                .withHttpMethod(HttpMethod.GET.getName())
                .withOperationName(GET_METHOD_FRIENDLY_NAME)
                .withApiKeyRequired(true)
                .withAuthorizationType(AuthorizationType.AWS_IAM.getName())
                .withRequestParameters(getRequestParameters()));

        LOG.info("Successfully created GET method for resource with path='{}', authType='{}'",
                resource.getPath(), putMethodResult.getAuthorizationType());

        return putMethodResult;
    }

    private Map<String, Boolean> getRequestParameters() {
        final Map<String, Boolean> requestParameters = new HashMap<>();
        requestParameters.put(
                RequestParameters.queryStringParameter("message"),
                RequestParameters.REQUIRED);
        return requestParameters;
    }

    private String readIntegrationTemplateFromFile() {
        return FileReader.readFile(INTEGRATION_TEMPLATE_FILE_PATH);
    }
}

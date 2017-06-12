package surf.deployers.api.methods;

import com.amazonaws.services.apigateway.AmazonApiGateway;
import com.amazonaws.services.apigateway.model.*;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.deployment.LambdaData;
import surf.exceptions.OperationFailedException;
import surf.utility.*;

import javax.annotation.Nonnull;
import java.util.*;

public class GetMethodCreator {

    private static final Logger LOG = LoggerFactory.getLogger(GetMethodCreator.class);

    private final CreateRestApiResult restApi;
    private final AmazonApiGateway apiClient;

    public GetMethodCreator(@Nonnull final CreateRestApiResult restApi, @Nonnull final AmazonApiGateway apiClient) {
        Preconditions.checkNotNull(restApi);
        Preconditions.checkNotNull(apiClient);

        this.restApi = restApi;
        this.apiClient = apiClient;
    }

    public GetMethodResult create(@Nonnull final Resource resource,
                           @Nonnull final String lambdaFunctionsPath,
                           @Nonnull final LambdaData functionData,
                           @Nonnull final String integrationTemplateFilePath,
                           @Nonnull final String methodFriendlyName) {
        Preconditions.checkNotNull(resource);
        Preconditions.checkNotNull(lambdaFunctionsPath);
        Preconditions.checkNotNull(functionData);
        Preconditions.checkNotNull(integrationTemplateFilePath);
        Preconditions.checkNotNull(methodFriendlyName);

        try {
            putMethod(resource, methodFriendlyName);
            putMethodIntegration(resource, lambdaFunctionsPath, functionData, integrationTemplateFilePath);
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

    private PutMethodResult putMethod(
            @Nonnull final Resource resource,
            @Nonnull final String methodFriendlyName) {
        LOG.info("Trying to create GET method for the '{}' api resource...", resource.getPath());

        final PutMethodResult putMethodResult = apiClient.putMethod(new PutMethodRequest()
                .withRestApiId(restApi.getId())
                .withResourceId(resource.getId())
                .withHttpMethod(HttpMethod.GET.getName())
                .withOperationName(methodFriendlyName)
                .withApiKeyRequired(true)
                .withAuthorizationType(AuthorizationType.AWS_IAM.getName())
                .withRequestParameters(getRequestParameters()));

        LOG.info("Successfully created GET method for resource with path='{}', authType='{}'",
                resource.getPath(), putMethodResult.getAuthorizationType());

        return putMethodResult;
    }

    private PutIntegrationResult putMethodIntegration(@Nonnull final Resource resource,
                                                      @Nonnull final String lambdaFunctionsPath,
                                                      @Nonnull final LambdaData functionData,
                                                      @Nonnull final String integrationTemplateFilePath) {
        LOG.info("Trying to put GET method integration for resource with path='{}'", resource.getPath());
        final IntegrationType integrationType = IntegrationType.AWS;
        final String integrationEndpoint = functionData.getFunctionArn();
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
                                readIntegrationTemplateFromFile(integrationTemplateFilePath)))
                .withUri(integrationUri));

        LOG.info("Successfully created GET method integration for resource with path='{}', " +
                        "integrationType='{}', integrationUri='{}'",
                resource.getPath(), putIntegrationResult.getType(), putIntegrationResult.getUri());

        return putIntegrationResult;
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

        results.add(apiClient.putMethodResponse(new PutMethodResponseRequest()
                .withRestApiId(restApi.getId())
                .withResourceId(resource.getId())
                .withHttpMethod(HttpMethod.GET.getName())
                .withStatusCode("500")
                .withResponseModels(Collections.singletonMap(ContentType.JSON.getName(), "Empty"))
                .withResponseParameters(ApiMethodsHelper.buildCrossOriginMethodResponseParameters())));

        LOG.info("Successfully created GET 500 method method response for resource with path='{}'",
                resource.getPath());

        return results;
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
                .withSelectionPattern("Error\\.400.*")
                .withResponseParameters(ApiMethodsHelper.buildCrossOriginIntegrationResponseParameters())
                .withResponseTemplates(dataPassThroughTemplate)));

        LOG.info("Successfully put GET 400 method integration response for resource with path='{}'",
                resource.getPath());

        results.add(apiClient.putIntegrationResponse(new PutIntegrationResponseRequest()
                .withRestApiId(restApi.getId())
                .withResourceId(resource.getId())
                .withHttpMethod(HttpMethod.GET.getName())
                .withStatusCode("500")
                .withSelectionPattern("Error\\.500.*")
                .withResponseParameters(ApiMethodsHelper.buildCrossOriginIntegrationResponseParameters())
                .withResponseTemplates(dataPassThroughTemplate)));

        LOG.info("Successfully put GET 500 method integration response for resource with path='{}'",
                resource.getPath());

        return results;

    }

    private Map<String, Boolean> getRequestParameters() {
        final Map<String, Boolean> requestParameters = new HashMap<>();
        return requestParameters;
    }

    private String readIntegrationTemplateFromFile(@Nonnull final String integrationTemplateFilePath) {
        return FileReader.readFile(integrationTemplateFilePath);
    }
}

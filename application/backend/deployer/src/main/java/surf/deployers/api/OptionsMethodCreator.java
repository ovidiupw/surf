package surf.deployers.api;

import com.amazonaws.services.apigateway.AmazonApiGateway;
import com.amazonaws.services.apigateway.model.*;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.exceptions.OperationFailedException;
import surf.utility.*;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class OptionsMethodCreator {

    private static final Logger LOG = LoggerFactory.getLogger(OptionsMethodCreator.class);

    // High throughput endpoint for CORS integration
    private static final String OPTIONS_INTEGRATION_ENDPOINT = "http://s3.amazonaws.com";

    private final CreateRestApiResult restApi;
    private final AmazonApiGateway apiClient;

    OptionsMethodCreator(@Nonnull final CreateRestApiResult restApi, @Nonnull final AmazonApiGateway apiClient) {
        Preconditions.checkNotNull(restApi);
        Preconditions.checkNotNull(apiClient);

        this.restApi = restApi;
        this.apiClient = apiClient;
    }

    GetMethodResult create(@Nonnull final Resource resource, @Nonnull final List<HttpMethod> corsAllowedMethods) {
        try {
            putMethod(resource);
            putMethodIntegration(resource);
            putMethodResponse(resource, getMethodResponseParameters());
            putMethodIntegrationResponse(resource, getIntegrationResponseParameters(corsAllowedMethods));

            return apiClient.getMethod(new GetMethodRequest()
                    .withRestApiId(restApi.getId())
                    .withResourceId(resource.getId())
                    .withHttpMethod(HttpMethod.OPTIONS.getName()));
        } catch (BadRequestException
                | UnauthorizedException
                | NotFoundException
                | ConflictException
                | TooManyRequestsException
                | LimitExceededException e) {
            LOG.error("Exception while creating the OPTIONS method for the '{}' api resource!", resource.getPath());
            LOG.error("Exception was...", e);
            throw new OperationFailedException(e);
        }
    }

    private PutMethodResult putMethod(@Nonnull final Resource resource) {
        LOG.info("Trying to create OPTIONS method for resource with path='{}'...", resource.getPath());
        final String authType = AuthorizationType.NONE.getName();

        final PutMethodResult putMethodResult = apiClient.putMethod(new PutMethodRequest()
                .withRestApiId(restApi.getId())
                .withApiKeyRequired(false)
                .withResourceId(resource.getId())
                .withHttpMethod(HttpMethod.OPTIONS.getName())
                .withAuthorizationType(authType));

        LOG.info("Successfully created OPTIONS method for resource with path='{}', authType='{}'",
                resource.getPath(), putMethodResult.getAuthorizationType());

        return putMethodResult;
    }

    private PutIntegrationResult putMethodIntegration(@Nonnull final Resource resource) {
        LOG.info("Trying to put OPTIONS method integration for resource with path='{}'", resource.getPath());
        final String integrationEndpoint = OPTIONS_INTEGRATION_ENDPOINT;
        final IntegrationType integrationType = IntegrationType.HTTP;

        final PutIntegrationResult putIntegrationResult = apiClient.putIntegration(new PutIntegrationRequest()
                .withRestApiId(restApi.getId())
                .withResourceId(resource.getId())
                .withHttpMethod(HttpMethod.OPTIONS.getName())
                .withIntegrationHttpMethod(HttpMethod.OPTIONS.getName())
                .withType(integrationType)
                .withUri(integrationEndpoint));

        LOG.info("Successfully created OPTIONS method integration for resource with path='{}', " +
                        "integrationType='{}', integrationUri='{}'",
                resource.getPath(), putIntegrationResult.getType(), putIntegrationResult.getUri());

        return putIntegrationResult;
    }

    private PutMethodResponseResult putMethodResponse(
            @Nonnull final Resource resource,
            @Nonnull final Map<String, Boolean> methodResponseParameters) {
        LOG.info("Trying to put OPTIONS method method-response for resource with path='{}'", resource.getPath());

        final PutMethodResponseResult putMethodResponseResult = apiClient.putMethodResponse(new PutMethodResponseRequest()
                .withRestApiId(restApi.getId())
                .withResourceId(resource.getId())
                .withHttpMethod(HttpMethod.OPTIONS.getName())
                .withStatusCode("200")
                .withResponseModels(Collections.singletonMap(ContentType.JSON.getName(), "Empty"))
                .withResponseParameters(methodResponseParameters));

        LOG.info("Successfully created OPTIONS method method response for resource with path='{}'",
                resource.getPath());

        return putMethodResponseResult;
    }

    private PutIntegrationResponseResult putMethodIntegrationResponse(
            @Nonnull final Resource resource,
            @Nonnull final Map<String, String> corsParameters) {
        LOG.info("Trying to put OPTIONS method integration response for resource wth path='{}'", resource.getPath());

        final Map<String, String> dataPassThroughTemplate = new HashMap<>();
        dataPassThroughTemplate.put(ContentType.JSON.getName(), "");

        final PutIntegrationResponseResult putIntegrationResponseResult
                = apiClient.putIntegrationResponse(new PutIntegrationResponseRequest()
                .withRestApiId(restApi.getId())
                .withResourceId(resource.getId())
                .withHttpMethod(HttpMethod.OPTIONS.getName())
                .withStatusCode("200")
                .withResponseParameters(corsParameters)
                .withResponseTemplates(dataPassThroughTemplate));

        LOG.info("Successfully put OPTIONS method integration response for resource with path='{}'",
                resource.getPath());

        return putIntegrationResponseResult;
    }

    private Map<String, String> getIntegrationResponseParameters(@Nonnull final List<HttpMethod> corsAllowedMethods) {
        final Map<String, String> integrationResponseParameters = new HashMap<>();

        integrationResponseParameters.put(
                RequestParameters.headerParameter("Access-Control-Allow-Origin"),
                "'*'");

        integrationResponseParameters.put(
                RequestParameters.headerParameter("Access-Control-Allow-Headers"),
                "'Content-Type,X-Amz-Date,Authorization,x-api-key,x-amz-security-token'");

        integrationResponseParameters.put(
                RequestParameters.headerParameter("Access-Control-Allow-Methods"),
                ObjectConverter.toCSVInnerString(corsAllowedMethods));

        return integrationResponseParameters;
    }

    private Map<String, Boolean> getMethodResponseParameters() {
        final Map<String, Boolean> methodResponseParameters = new HashMap<>();
        methodResponseParameters.put(RequestParameters.headerParameter("Access-Control-Allow-Origin"), true);
        methodResponseParameters.put(RequestParameters.headerParameter("Access-Control-Allow-Headers"), true);
        methodResponseParameters.put(RequestParameters.headerParameter("Access-Control-Allow-Methods"), true);
        return methodResponseParameters;
    }
}

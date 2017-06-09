package surf.deployers.api;

import com.amazonaws.services.apigateway.AmazonApiGateway;
import com.amazonaws.services.apigateway.AmazonApiGatewayClientBuilder;
import com.amazonaws.services.apigateway.model.*;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.deployers.Deployer;
import surf.deployers.DeployerConfiguration;
import surf.deployers.api.resources.ResourceCreator;
import surf.deployers.api.resources.WorkersResourceCreator;
import surf.deployers.api.resources.WorkflowsByIdResourceCreator;
import surf.deployers.api.resources.WorkflowsResourceCreator;
import surf.deployment.Context;
import surf.exceptions.OperationFailedException;
import surf.utility.FileHelper;
import surf.utility.FileWriter;

import javax.annotation.Nonnull;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;

public class ApiDeployer implements Deployer {

    private static final Logger LOG = LoggerFactory.getLogger(ApiDeployer.class);

    private static final String DEPLOYER_NAME = "ApiDeployer";
    private static final String API_NAME = "Surf";
    private static final String API_DESCRIPTION =
            "Api for Surf distributed web crawler. Created at " + LocalDateTime.now(DateTimeZone.UTC);
    private static final String API_VERSION = "v1";
    private static final String USER_KEY_DESCRIPTION = "Api key for Surf Api for standard users";
    private static final String USER_KEY_NAME = "StandardUserApiKey";

    private final DeployerConfiguration deployerConfiguration;

    @Inject
    public ApiDeployer(@Nonnull final DeployerConfiguration deployerConfiguration) {
        Preconditions.checkNotNull(deployerConfiguration);
        this.deployerConfiguration = deployerConfiguration;
    }

    @Override
    public String getName() {
        return DEPLOYER_NAME;
    }

    @Override
    public Context deploy(@Nonnull final Context context) {
        Preconditions.checkNotNull(context);
        final String cloudWatchLogsRoleArn = context.getIAMRoles().getApiGatewayPushToCloudWatchLogsRole().getArn();
        Preconditions.checkNotNull(cloudWatchLogsRoleArn);

        final AmazonApiGateway apiClient = initializeApiClient();

        updateAccountWithCloudWatchLogsRoleArn(apiClient, cloudWatchLogsRoleArn);
        final CreateRestApiResult restApi = createRestApi(apiClient);
        final CreateApiKeyResult apiKey = createEnabledApiKey(apiClient, USER_KEY_NAME, USER_KEY_DESCRIPTION);
        final Resource rootResource = getRootResource(restApi, apiClient);

        final ResourceCreator workersResourceCreator = new WorkersResourceCreator(
                restApi, apiClient, deployerConfiguration, context);
        final Resource workersResource = workersResourceCreator.create(rootResource, "workers");

        final ResourceCreator workflowsResourceCreator = new WorkflowsResourceCreator(
                restApi, apiClient, deployerConfiguration, context);
        final Resource workflowsResource = workflowsResourceCreator.create(rootResource, "workflows");

        final ResourceCreator workflowsByIdResourceCreator = new WorkflowsByIdResourceCreator(
                restApi, apiClient, deployerConfiguration, context);
        final Resource workflowsByIdResource = workflowsByIdResourceCreator.create(workflowsResource, "{id}");
        // Add other resources here by abiding the general interface conventions

        createApiDeployment(apiClient, restApi);
        configureDeploymentStageSettings(apiClient, restApi);

        createApiUsagePlan(apiClient, restApi, apiKey);

        final GetSdkResult getSdkResult = generateJavascriptSdk(apiClient, restApi);
        extractGeneratedSdkToFolder(getSdkResult);

        context.setApiResources(Arrays.asList(
                workersResource,
                workflowsResource,
                workflowsByIdResource
        ));
        // Add other resources to the context as you create them in the API
        context.setApiKey(apiKey.getValue());

        return context;
    }

    private AmazonApiGateway initializeApiClient() {
        return AmazonApiGatewayClientBuilder.standard()
                .withClientConfiguration(deployerConfiguration.getClientConfiguration())
                .withRegion(deployerConfiguration.getAwsClientRegion())
                .build();
    }

    private UpdateAccountResult updateAccountWithCloudWatchLogsRoleArn(
            @Nonnull final AmazonApiGateway apiClient,
            @Nonnull final String roleArn) {
        try {
            LOG.info("Trying to set the CloudWatch logs role ARN to api account...");
            final UpdateAccountResult updateAccountResult = apiClient.updateAccount(new UpdateAccountRequest()
                    .withPatchOperations(new PatchOperation()
                            .withPath("/cloudwatchRoleArn")
                            .withOp(Op.Replace)
                            .withValue(roleArn)));
            LOG.info("Successfully set the CloudWatch logs role ARN to api account!");
            return updateAccountResult;
        } catch (UnauthorizedException
                | BadRequestException
                | NotFoundException
                | TooManyRequestsException e) {
            LOG.error("Exception while trying to set CloudWatch logs role ARN to api !", e);
            throw new OperationFailedException(e);
        }
    }

    private CreateRestApiResult createRestApi(@Nonnull final AmazonApiGateway apiClient) {
        try {
            LOG.info("Trying to create the Surf REST Api with name='{}', description='{}', version='{}'...",
                    API_NAME,
                    API_DESCRIPTION,
                    API_VERSION);
            final CreateRestApiResult restApi = apiClient.createRestApi(new CreateRestApiRequest()
                    .withName(API_NAME)
                    .withDescription(API_DESCRIPTION)
                    .withVersion(API_VERSION));

            LOG.info("Successfully created api with id='{}'!", restApi.getId());
            LOG.warn("The created api with name='{}' supports only binary media types of type '{}'!",
                    restApi.getName(), restApi.getBinaryMediaTypes());

            return restApi;
        } catch (UnauthorizedException
                | LimitExceededException
                | BadRequestException
                | TooManyRequestsException e) {
            LOG.error("Exception while trying to create the REST Api! Exiting...", e);
            throw new OperationFailedException(e);
        }
    }

    private CreateApiKeyResult createEnabledApiKey(
            @Nonnull final AmazonApiGateway apiClient,
            @Nonnull final String name,
            @Nonnull final String description) {
        try {
            LOG.info("Trying to create enabled api key with name='{}'...", name);
            final CreateApiKeyResult apiKey = apiClient.createApiKey(new CreateApiKeyRequest()
                    .withName(name)
                    .withDescription(description)
                    .withEnabled(true));

            LOG.info("Successfully created apiKey with name='{}' and value='{}'", apiKey.getName(), apiKey.getValue());

            return apiKey;
        } catch (UnauthorizedException
                | NotFoundException
                | TooManyRequestsException
                | LimitExceededException
                | BadRequestException
                | ConflictException e) {
            LOG.error("Exception while trying to create enabled api key with name='{}', description='{}'",
                    name,
                    description);
            LOG.error("Exception was...", e);
            throw new OperationFailedException(e);
        }
    }

    private Resource getRootResource(
            @Nonnull final CreateRestApiResult restApi,
            @Nonnull final AmazonApiGateway apiClient) {
        try {
            LOG.info("Trying to get root resources for apiId='{}' and apiName='{}'",
                    restApi.getId(), restApi.getName());

            final GetResourcesResult resources = apiClient.getResources(new GetResourcesRequest()
                    .withRestApiId(restApi.getId())
                    .withLimit(1));

            final Resource rootResource = resources.getItems().get(0);
            LOG.info("Identified the api root resource: path='{}', id='{}'", rootResource.getPath(), rootResource.getId());

            return rootResource;
        } catch (BadRequestException
                | UnauthorizedException
                | NotFoundException
                | TooManyRequestsException e) {
            LOG.error("Exception while trying to get the root resource of the REST Api! Exiting...", e);
            throw new OperationFailedException(e);
        }
    }

    private CreateDeploymentResult createApiDeployment(
            @Nonnull final AmazonApiGateway apiClient,
            @Nonnull final CreateRestApiResult restApi) {
        try {
            LOG.info("Trying to deploy the Surf REST Api with id='{}'...", restApi.getId());
            final CreateDeploymentResult deployment = apiClient.createDeployment(new CreateDeploymentRequest()
                    .withRestApiId(restApi.getId())
                    .withStageName(deployerConfiguration.getApiStageName())
                    .withStageDescription(deployerConfiguration.getApiStageDescription())
                    .withDescription("Api deployment created at " + LocalDateTime.now(DateTimeZone.UTC)));

            LOG.info("Successfully deployed api with id='{}'!", restApi.getId());

            return deployment;
        } catch (UnauthorizedException
                | LimitExceededException
                | BadRequestException
                | TooManyRequestsException
                | NotFoundException
                | ConflictException
                | ServiceUnavailableException e) {
            LOG.error("Exception while trying to deploy the REST Api with id='{}'!", restApi.getId());
            LOG.error("Exception was...", e);
            throw new OperationFailedException(e);
        }

    }

    private UpdateStageResult configureDeploymentStageSettings(
            @Nonnull final AmazonApiGateway apiClient,
            @Nonnull final CreateRestApiResult restApi) {
        try {
            LOG.info("Trying to set deployment stage settings for stage with name='{}'",
                    deployerConfiguration.getApiStageName());

            final PatchOperation burstLimitPath = new PatchOperation()
                    .withOp(Op.Replace)
                    .withPath("/*/*/throttling/burstLimit")
                    .withValue(String.valueOf(deployerConfiguration.getApiStageThrottlingBurstLimit()));

            final PatchOperation rateLimitPatch = new PatchOperation()
                    .withOp(Op.Replace)
                    .withPath("/*/*/throttling/rateLimit")
                    .withValue(String.valueOf(deployerConfiguration.getApiStageThrottlingRateLimit()));

            final PatchOperation metricsEnabledPatch = new PatchOperation()
                    .withOp(Op.Replace)
                    .withPath("/*/*/metrics/enabled")
                    .withValue(String.valueOf(deployerConfiguration.getApiStageMetricsEnabled()));

            final PatchOperation loggingLevelPatch = new PatchOperation()
                    .withOp(Op.Replace)
                    .withPath("/*/*/logging/loglevel")
                    .withValue(deployerConfiguration.getApiLoggingLevel());

            final PatchOperation dataTracePatch = new PatchOperation()
                    .withOp(Op.Replace)
                    .withPath("/*/*/logging/dataTrace")
                    .withValue(String.valueOf(deployerConfiguration.getApiDataTraceEnabled()));

            final UpdateStageResult updateStageResult = apiClient.updateStage(new UpdateStageRequest()
                    .withRestApiId(restApi.getId())
                    .withStageName(deployerConfiguration.getApiStageName())
                    .withPatchOperations(Arrays.asList(
                            burstLimitPath,
                            rateLimitPatch,
                            metricsEnabledPatch,
                            loggingLevelPatch,
                            dataTracePatch)));

            LOG.info("Successfully set method settings for stage with name='{}': " +
                            "metricsEnabled='{}', " +
                            "throttlingRateLimit='{}', " +
                            "throttlingBurstLimit='{}'",
                    deployerConfiguration.getApiStageName(),
                    deployerConfiguration.getApiStageMetricsEnabled(),
                    deployerConfiguration.getApiStageThrottlingRateLimit(),
                    deployerConfiguration.getApiStageThrottlingBurstLimit());

            return updateStageResult;

        } catch (UnauthorizedException
                | BadRequestException
                | NotFoundException
                | TooManyRequestsException
                | ConflictException e) {
            LOG.error("Exception while trying to set method settings for stage with name='{}' !",
                    deployerConfiguration.getApiStageName());
            LOG.error("Exception was...", e);
            throw new OperationFailedException(e);
        }
    }

    private CreateUsagePlanResult createApiUsagePlan(
            @Nonnull final AmazonApiGateway apiClient,
            @Nonnull final CreateRestApiResult restApi,
            @Nonnull final CreateApiKeyResult apiKey) {

        try {
            LOG.info("Trying to create an usage plan for apiId={} and keyId={}...", restApi.getId(), apiKey.getId());
            final CreateUsagePlanResult usagePlan = apiClient.createUsagePlan(new CreateUsagePlanRequest()
                    .withApiStages(new ApiStage()
                            .withApiId(restApi.getId())
                            .withStage(deployerConfiguration.getApiStageName()))
                    .withName("UsagePlan apiID=" + restApi.getId())
                    .withDescription("Api usage plan for associated api key with id=" + apiKey.getId())
                    .withThrottle(new ThrottleSettings()
                            .withRateLimit(deployerConfiguration.getApiStageThrottlingRateLimit())
                            .withBurstLimit(deployerConfiguration.getApiStageThrottlingBurstLimit())));
            LOG.info("Successfully created API usage plan!");

            LOG.info("Adding API key with apiKeyId={} to usage plan with usagePlanId={}",
                    apiKey.getId(), usagePlan.getId());

            apiClient.createUsagePlanKey(new CreateUsagePlanKeyRequest()
                    .withUsagePlanId(usagePlan.getId())
                    .withKeyId(apiKey.getId())
                    .withKeyType("API_KEY"));

            LOG.info("Successfully added API key to usage plan!");

            return usagePlan;
        } catch (BadRequestException
                | UnauthorizedException
                | TooManyRequestsException
                | LimitExceededException
                | ConflictException
                | NotFoundException e) {
            LOG.error("Exception while trying to create API usage plan!", e);
            throw new OperationFailedException(e);
        }

    }

    private GetSdkResult generateJavascriptSdk(
            @Nonnull final AmazonApiGateway apiClient,
            @Nonnull final CreateRestApiResult restApi) {
        try {
            LOG.info("Trying to generate the javascript sdk for the API with id='{}'", restApi.getId());
            final GetSdkResult sdk = apiClient.getSdk(new GetSdkRequest()
                    .withRestApiId(restApi.getId())
                    .withStageName(deployerConfiguration.getApiStageName())
                    .withSdkType(deployerConfiguration.getApiGeneratedSdkType()));
            LOG.info("Successfully generated the javascript sdk for the API with id='{}'", restApi.getId());
            return sdk;
        } catch (UnauthorizedException
                | BadRequestException
                | NotFoundException
                | TooManyRequestsException e) {
            LOG.error("Exception while generating the javascript sdk for the API with id='{}'", restApi.getId());
            LOG.error("Exception was...", e);
            throw new OperationFailedException(e);
        }
    }

    private void extractGeneratedSdkToFolder(@Nonnull final GetSdkResult getSdkResult) {
        String zipFilePath = deployerConfiguration.getApiGeneratedSdkFolderName() + ".zip";

        LOG.info("Writing the generated sdk zip file to folder='{}' with fileName='{}'",
                deployerConfiguration.getApiGeneratedSdkOutputPath(),
                zipFilePath);

        final Path sdkFilePath = FileWriter.writeZipFile(
                getSdkResult.getBody().array(),
                zipFilePath,
                deployerConfiguration.getApiGeneratedSdkOutputPath());

        LOG.info("Successfully written generated sdk zip file to disk!");

        LOG.info("Extracting the generated sdk zip file to folder'{}' with extractName='{}'",
                deployerConfiguration.getApiGeneratedSdkOutputPath(),
                deployerConfiguration.getApiGeneratedSdkFolderName());

        FileHelper.extractZip(sdkFilePath, new File(
                deployerConfiguration.getApiGeneratedSdkOutputPath(),
                deployerConfiguration.getApiGeneratedSdkFolderName())
                .getPath());

        LOG.info("Successfully written generated sdk zip file to disk!");
    }


}

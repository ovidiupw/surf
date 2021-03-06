package surf.deployers.lambda;

import com.amazonaws.services.identitymanagement.model.ServiceFailureException;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.*;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.deployers.Deployer;
import surf.deployers.DeployerConfiguration;
import surf.deployers.lambda.config.*;
import surf.deployment.Context;
import surf.exceptions.OperationFailedException;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class LambdaDeployer implements Deployer {

    private static final Logger LOG = LoggerFactory.getLogger(LambdaDeployer.class);
    private static final String DEPLOYER_NAME = "LambdaDeployer";
    private static final String LAMBDA_API_GATEWAY_INVOKE_STATEMENT_ID = "apigateway-invoke-lambda-4457c439bb0b";
    private static final String LAMBDA_INVOKE_FUNCTION_ACTION = "lambda:invokeFunction";

    private DeployerConfiguration deployerConfiguration;

    @Inject
    public LambdaDeployer(@Nonnull final DeployerConfiguration deployerConfiguration) {
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

        final AWSLambda lambdaClient = initializeLambdaClient();

        final List<LambdaData> lambdaNeedingApiGatewayInvokePermissions = new ArrayList<>();

        /* API Gateway Lambda functions */

        final LambdaData listCoreWorkersData = createFunction(lambdaClient, new ListCoreWorkersLambdaConfig(context), context);
        lambdaNeedingApiGatewayInvokePermissions.add(listCoreWorkersData);

        final LambdaData listWorkflowsData = createFunction(lambdaClient, new ListWorkflowsLambdaConfig(context), context);
        lambdaNeedingApiGatewayInvokePermissions.add(listWorkflowsData);

        final LambdaData createWorkflowData = createFunction(lambdaClient, new CreateWorkflowLambdaConfig(context), context);
        lambdaNeedingApiGatewayInvokePermissions.add(createWorkflowData);

        final LambdaData startWorkflowData = createFunction(lambdaClient, new StartWorkflowLambdaConfig(context), context);
        lambdaNeedingApiGatewayInvokePermissions.add(startWorkflowData);

        final LambdaData getWorkflowData = createFunction(lambdaClient, new GetWorkflowLambdaConfig(context), context);
        lambdaNeedingApiGatewayInvokePermissions.add(getWorkflowData);

        final LambdaData listWorkflowExecutionsData = createFunction(lambdaClient, new ListWorkflowExecutionsLambdaConfig(context), context);
        lambdaNeedingApiGatewayInvokePermissions.add(listWorkflowExecutionsData);

        final LambdaData listVisitedPagesForWorkflowExecutionData = createFunction(lambdaClient, new ListVisitedPagesForWorkflowExecutionLambdaConfig(context), context);
        lambdaNeedingApiGatewayInvokePermissions.add(listVisitedPagesForWorkflowExecutionData);

        final LambdaData getS3PresignedUrlData = createFunction(lambdaClient, new GetS3PresignedUrlConfig(context), context);
        lambdaNeedingApiGatewayInvokePermissions.add(getS3PresignedUrlData);

        cleanupApiGatewayInvokePermissions(lambdaClient, lambdaNeedingApiGatewayInvokePermissions);
        setupApiGatewayInvokePermissions(lambdaClient, lambdaNeedingApiGatewayInvokePermissions);

        /* Non API Gateway Lambda functions */

        final LambdaData initializeCrawlSessionData
                = createFunction(lambdaClient, new InitializeCrawlSessionLambdaConfig(context), context);
        final LambdaData crawlWebPageData
                = createFunction(lambdaClient, new CrawlWebPageLambdaConfig(context), context);
        final LambdaData finalizeCrawlSessionData
                = createFunction(lambdaClient, new FinalizeCrawlSessionLambdaConfig(context), context);
        final LambdaData apiAuthorizerData
                = createFunction(lambdaClient, new ApiAuthorizerLambdaConfig(context), context);

        LOG.info("Updating context with the created/existing lambda arns.");
        final LambdaFunctionsData lambdaFunctionsData = new LambdaFunctionsData.Builder()
                .withListCoreWorkersFunctionData(listCoreWorkersData)
                .withListWorkflowsData(listWorkflowsData)
                .withCreateWorkflowData(createWorkflowData)
                .withStartWorkflowData(startWorkflowData)
                .withGetWorkflowData(getWorkflowData)
                .withInitializeCrawlSessionData(initializeCrawlSessionData)
                .withCrawlWebPageData(crawlWebPageData)
                .withFinalizeCrawlSessionData(finalizeCrawlSessionData)
                .withListWorkflowExecutionsData(listWorkflowExecutionsData)
                .withApiAuthorizerData(apiAuthorizerData)
                .withListVisitedPagesForWorkflowExecutionData(listVisitedPagesForWorkflowExecutionData)
                .withGetS3PresignedUrlData(getS3PresignedUrlData)
                .build();
        context.setLambdaFunctionsData(lambdaFunctionsData);

        return context;
    }

    private AWSLambda initializeLambdaClient() {
        return AWSLambdaClientBuilder.standard()
                .withClientConfiguration(deployerConfiguration.getClientConfiguration())
                .withRegion(deployerConfiguration.getAwsClientRegion())
                .build();
    }

    private LambdaData createFunction(
            @Nonnull final AWSLambda lambdaClient,
            @Nonnull final LambdaFunctionConfig functionConfig,
            @Nonnull final Context context) {

        final FunctionCode functionCode = new FunctionCode()
                .withS3Bucket(context.getS3AppConfigBucketName())
                .withS3Key(context.getS3LambdaCodeKey());

        LOG.info("Set function code to s3Bucket='{}' and s3Key='{}'",
                context.getS3AppConfigBucketName(), context.getS3LambdaCodeKey());

        final CreateFunctionRequest createFunctionRequest = new CreateFunctionRequest()
                .withFunctionName(functionConfig.getFunctionName())
                .withDescription(functionConfig.getDescription())
                .withHandler(functionConfig.getHandlerName())
                .withMemorySize(functionConfig.getMemoryMegabytes())
                .withRole(functionConfig.getIAMRole().getArn())
                .withTimeout(functionConfig.getTimeoutSeconds())
                .withRuntime(deployerConfiguration.getLambdaRuntime())
                .withCode(functionCode);

        try {
            LOG.info("Trying to create lambda function with name='{}', description='{}', handler='{}', " +
                            "memoryMegabytes='{}', iamRoleName='{}', iamRoleArn='{}' timeoutSeconds='{}', " +
                            "runtime='{}'...",
                    functionConfig.getFunctionName(),
                    functionConfig.getDescription(),
                    functionConfig.getHandlerName(),
                    functionConfig.getMemoryMegabytes(),
                    functionConfig.getIAMRole().getRoleName(),
                    functionConfig.getIAMRole().getArn(),
                    functionConfig.getTimeoutSeconds(),
                    deployerConfiguration.getLambdaRuntime());

            final CreateFunctionResult createFunctionResult = lambdaClient.createFunction(createFunctionRequest);

            LOG.info("Successfully created lambda function with name='{}'", functionConfig.getFunctionName());
            return new LambdaData(
                    createFunctionResult.getFunctionName(),
                    createFunctionResult.getFunctionArn(),
                    createFunctionResult.getDescription());
        } catch (ResourceConflictException ignored) {
            LOG.warn("Lambda function with name '{}' already exists! Will update its metadata instead.",
                    functionConfig.getFunctionName());
            return updateFunction(lambdaClient, functionConfig, context);
        } catch (InvalidParameterValueException
                | ServiceFailureException
                | ResourceNotFoundException
                | TooManyRequestsException
                | CodeStorageExceededException e) {
            LOG.error("Exception while trying to create Lambda function! Exiting...", e);
            throw new OperationFailedException(e);
        }
    }

    private LambdaData updateFunction(
            @Nonnull final AWSLambda lambdaClient,
            @Nonnull final LambdaFunctionConfig functionConfig,
            @Nonnull final Context context) {
        final GetFunctionResult getFunctionResult = getFunction(lambdaClient, functionConfig);
        LOG.info("Existing lambda function was found! Proceeding with updating function code and configuration.");

        updateFunctionCode(lambdaClient, functionConfig, context);
        updateFunctionConfiguration(lambdaClient, functionConfig);

        return new LambdaData(
                getFunctionResult.getConfiguration().getFunctionName(),
                getFunctionResult.getConfiguration().getFunctionArn(),
                getFunctionResult.getConfiguration().getDescription());
    }

    private GetFunctionResult getFunction(
            @Nonnull final AWSLambda lambdaClient,
            @Nonnull final LambdaFunctionConfig functionConfig) {
        try {
            LOG.info("Getting lambda function code and configuration for function name '{}'...",
                    functionConfig.getFunctionName());
            return lambdaClient.getFunction(new GetFunctionRequest()
                    .withFunctionName(functionConfig.getFunctionName()));
        } catch (ServiceFailureException
                | ResourceNotFoundException
                | TooManyRequestsException
                | InvalidParameterValueException e) {
            LOG.error("Exception while trying to get Lambda function! Exiting...", e);
            throw new OperationFailedException(e);
        }
    }

    private UpdateFunctionCodeResult updateFunctionCode(
            @Nonnull final AWSLambda lambdaClient,
            @Nonnull final LambdaFunctionConfig functionConfig,
            @Nonnull final Context context) {

        try {
            LOG.info("Updating lambda function code for function name '{}'", functionConfig.getFunctionName());
            return lambdaClient.updateFunctionCode(new UpdateFunctionCodeRequest()
                    .withFunctionName(functionConfig.getFunctionName())
                    .withS3Bucket(context.getS3AppConfigBucketName())
                    .withS3Key(context.getS3LambdaCodeKey()));
        } catch (ServiceFailureException
                | ResourceNotFoundException
                | TooManyRequestsException
                | InvalidParameterValueException
                | CodeStorageExceededException e) {
            LOG.error("Exception while trying to update Lambda function code! Exiting...", e);
            throw new OperationFailedException(e);
        }
    }

    private UpdateFunctionConfigurationResult updateFunctionConfiguration(
            @Nonnull final AWSLambda lambdaClient,
            @Nonnull final LambdaFunctionConfig config) {
        try {
            LOG.info("Updating lambda function configuration for function name '{}'", config.getFunctionName());
            return lambdaClient.updateFunctionConfiguration(new UpdateFunctionConfigurationRequest()
                    .withRuntime(deployerConfiguration.getLambdaRuntime())
                    .withFunctionName(config.getFunctionName())
                    .withDescription(config.getDescription())
                    .withHandler(config.getHandlerName())
                    .withMemorySize(config.getMemoryMegabytes())
                    .withRole(config.getIAMRole().getArn())
                    .withTimeout(config.getTimeoutSeconds()));
        } catch (InvalidParameterValueException
                | ServiceFailureException
                | ResourceNotFoundException
                | TooManyRequestsException
                | CodeStorageExceededException e) {
            LOG.error("Exception while trying to update Lambda function! Exiting...", e);
            throw new OperationFailedException(e);
        }
    }

    private void cleanupApiGatewayInvokePermissions(
            @Nonnull final AWSLambda lambdaClient,
            @Nonnull final List<LambdaData> lambdasData) {
        LOG.info("Trying to cleanup Api Gateway invoke permissions for all lambda functions in order to recreate them");
        for (final LambdaData lambdaData : lambdasData) {
            try {
                LOG.info("Cleaning up Api Gateway invoke permission with statementId='{}' for lambda with name='{}'",
                        LAMBDA_API_GATEWAY_INVOKE_STATEMENT_ID,
                        lambdaData.getFunctionName());

                lambdaClient.removePermission(new RemovePermissionRequest()
                        .withFunctionName(lambdaData.getFunctionName())
                        .withStatementId(LAMBDA_API_GATEWAY_INVOKE_STATEMENT_ID));

                LOG.info("Successfully cleaned up Api Gateway invoke permission for lambda with name='{}'",
                        lambdaData.getFunctionName());
            } catch (ResourceNotFoundException ignored) {
            } catch (ServiceException
                    | InvalidParameterValueException
                    | TooManyRequestsException e) {
                LOG.error("Exception while trying to cleanup lambda api-gateway invoke permissions!", e);
                throw new OperationFailedException(e);
            }
        }

    }

    private void setupApiGatewayInvokePermissions(
            @Nonnull final AWSLambda lambdaClient,
            @Nonnull final List<LambdaData> lambdasData) {
        LOG.info("Trying to setup api gateway invoke permissions for lambdaFunctions...");
        for (final LambdaData lambdaData : lambdasData) {

            final String sourceEndpoint = deployerConfiguration.getApiGatewayEndpoint();
            final String sourceArn = String.format(
                    "arn:aws:execute-api:%s:%s:*/*/*/*",
                    deployerConfiguration.getAwsClientRegion().getName(),
                    deployerConfiguration.getAwsAccountId());

            addInvokePermissionToLambdaFunction(lambdaClient, lambdaData, sourceEndpoint, sourceArn);
        }
    }

    private void addInvokePermissionToLambdaFunction(
            @Nonnull final AWSLambda lambdaClient,
            @Nonnull final LambdaData lambdaData,
            @Nonnull final String sourceEndpoint,
            @Nonnull final String sourceArn) {
        LOG.info("Adding invoke permission on lambdaName='{}' to sourceEndpoint='{}' with sourceArn='{}'...",
                lambdaData.getFunctionName(),
                sourceEndpoint,
                sourceArn);
        try {
            lambdaClient.addPermission(new AddPermissionRequest()
                    .withFunctionName(lambdaData.getFunctionName())
                    .withStatementId(LAMBDA_API_GATEWAY_INVOKE_STATEMENT_ID)
                    .withAction(LAMBDA_INVOKE_FUNCTION_ACTION)
                    .withPrincipal(sourceEndpoint)
                    .withSourceArn(sourceArn));

            LOG.info("Successfully added invoke permission on lambdaName='{}'", lambdaData.getFunctionName());
        } catch (ServiceException
                | ResourceNotFoundException
                | ResourceConflictException
                | InvalidParameterValueException
                | PolicyLengthExceededException
                | TooManyRequestsException e) {
            LOG.error("Could not add lambda invoke permission on " +
                            "lambdaName='{}' to sourceEndpoint='{}', sourceArn='{}'",
                    lambdaData.getFunctionName(),
                    sourceEndpoint,
                    sourceArn);
            LOG.error("Exception was...", e);
            throw new OperationFailedException(e);
        }
    }
}

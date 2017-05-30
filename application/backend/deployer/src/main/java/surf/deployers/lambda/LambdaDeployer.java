package surf.deployers.lambda;

import com.amazonaws.services.identitymanagement.model.ServiceFailureException;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.*;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.ExitCode;
import surf.deployers.Deployer;
import surf.deployers.DeployerConfiguration;
import surf.deployment.Context;
import surf.deployment.LambdaArns;

import javax.annotation.Nonnull;

public class LambdaDeployer implements Deployer {

    private static final Logger LOG = LoggerFactory.getLogger(LambdaDeployer.class);
    private static final String DEPLOYER_NAME = "LambdaDeployer";

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

        String helloWorldLambdaArn = createFunction(lambdaClient, new HelloWorldLambdaConfig(context));
        /* Add more lambda functions here when needed */

        LOG.info("Updating context with the created/existing lambda arns.");
        final LambdaArns lambdaArns = new LambdaArns.Builder()
                .withHelloWorldLambdaArn(helloWorldLambdaArn)
                .build();
        context.setLambdaArns(lambdaArns);


        return context;
    }

    private AWSLambda initializeLambdaClient() {
        return AWSLambdaClientBuilder.standard()
                .withClientConfiguration(deployerConfiguration.getClientConfiguration())
                .withRegion(deployerConfiguration.getRegion())
                .build();
    }

    private String createFunction(AWSLambda lambdaClient, LambdaFunctionConfig functionConfig) {
        final CreateFunctionRequest createFunctionRequest = new CreateFunctionRequest()
                .withFunctionName(functionConfig.getFunctionName())
                .withDescription(functionConfig.getDescription())
                .withHandler(functionConfig.getHandlerName())
                .withMemorySize(functionConfig.getMemoryMegabytes())
                .withRole(functionConfig.getIAMRole().getArn())
                .withTimeout(functionConfig.getTimeoutSeconds())
                .withRuntime(deployerConfiguration.getLambdaRuntime())
                .withCode(deployerConfiguration.getLambdaFunctionCode());

        try {
            LOG.info("Trying to create lambda function with name='{}', description='{}', handler='{}'," +
                            "memoryMegabytes='{}', iamRoleName='{}', timeoutSeconds='{}', runtime='{}'...",
                    functionConfig.getFunctionName(),
                    functionConfig.getDescription(),
                    functionConfig.getHandlerName(),
                    functionConfig.getMemoryMegabytes(),
                    functionConfig.getIAMRole().getRoleName(),
                    functionConfig.getTimeoutSeconds(),
                    deployerConfiguration.getLambdaRuntime());

            CreateFunctionResult createFunctionResult = lambdaClient.createFunction(createFunctionRequest);
            return createFunctionResult.getFunctionArn();
        } catch (ResourceConflictException ignored) {
            LOG.warn("Lambda function with name '{}' already exists! Will update its metadata instead.",
                    functionConfig.getFunctionName());
            return updateFunction(lambdaClient, functionConfig);
        } catch (InvalidParameterValueException
                | ServiceFailureException
                | ResourceNotFoundException
                | TooManyRequestsException
                | CodeStorageExceededException e) {
            LOG.error("Exception while trying to create Lambda function! Exiting...", e);
            System.exit(ExitCode.Error.getCode());
        }

        throw new IllegalStateException("This code path should not have been reached!");
    }

    private String updateFunction(final AWSLambda lambdaClient, final LambdaFunctionConfig functionConfig) {
        final GetFunctionResult getFunctionResult = getFunction(lambdaClient, functionConfig);
        LOG.info("Existing lambda function was found! Proceeding with updating function code and configuration.");

        updateFunctionCode(lambdaClient, functionConfig);
        updateFunctionConfiguration(lambdaClient, functionConfig);

        return getFunctionResult.getConfiguration().getFunctionArn();
    }

    private GetFunctionResult getFunction(final AWSLambda lambdaClient, final LambdaFunctionConfig functionConfig) {
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
            System.exit(ExitCode.Error.getCode());
        }

        throw new IllegalStateException("This code path should not have been reached!");
    }

    private UpdateFunctionCodeResult updateFunctionCode(
            final AWSLambda lambdaClient, final LambdaFunctionConfig functionConfig) {
        try {
            LOG.info("Updating lambda function code for function name '{}'", functionConfig.getFunctionName());
            return lambdaClient.updateFunctionCode(new UpdateFunctionCodeRequest()
                    .withFunctionName(functionConfig.getFunctionName())
                    .withZipFile(deployerConfiguration.getLambdaFunctionCode().getZipFile()));
        } catch (ServiceFailureException
                | ResourceNotFoundException
                | TooManyRequestsException
                | InvalidParameterValueException
                | CodeStorageExceededException e) {
            LOG.error("Exception while trying to update Lambda function code! Exiting...", e);
            System.exit(ExitCode.Error.getCode());
        }

        throw new IllegalStateException("This code path should not have been reached!");
    }

    private void updateFunctionConfiguration(final AWSLambda lambdaClient, final LambdaFunctionConfig functionConfig) {
        try {
            LOG.info("Updating lambda function configuration for function name '{}'", functionConfig.getFunctionName());
            lambdaClient.updateFunctionConfiguration(new UpdateFunctionConfigurationRequest()
                    .withRuntime(deployerConfiguration.getLambdaRuntime())
                    .withFunctionName(functionConfig.getFunctionName())
                    .withDescription(functionConfig.getDescription())
                    .withHandler(functionConfig.getHandlerName())
                    .withMemorySize(functionConfig.getMemoryMegabytes())
                    .withRole(functionConfig.getIAMRole().getArn())
                    .withTimeout(functionConfig.getTimeoutSeconds()));
        } catch (InvalidParameterValueException
                | ServiceFailureException
                | ResourceNotFoundException
                | TooManyRequestsException
                | CodeStorageExceededException e) {
            LOG.error("Exception while trying to create Lambda function! Exiting...", e);
            System.exit(ExitCode.Error.getCode());
        }
    }
}

package surf.deployers;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.model.FunctionCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.utility.ExitCode;
import surf.utility.FileReader;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

public class DeployerConfigurationModule extends AbstractModule {
    private static final Logger LOG = LoggerFactory.getLogger(DeployerConfigurationModule.class);
    private final String configurationFilePath;

    public DeployerConfigurationModule(@Nonnull final String configurationFilePath) {
        Preconditions.checkNotNull(configurationFilePath);
        this.configurationFilePath = configurationFilePath;
    }

    @Override
    protected void configure() {
        try {
            DeployerConfiguration deployerConfiguration = initializeDeployerConfiguration();
            bind(DeployerConfiguration.class).toInstance(deployerConfiguration);
        } catch (IOException e) {
            LOG.error("Error while initializing deployer configuration!", e);
            System.exit(ExitCode.Error.getCode());
        }
    }

    private DeployerConfiguration initializeDeployerConfiguration() throws IOException {

        final DeployerConfigurationConstants config = loadDeployerConfig();

        final ClientConfiguration clientConfiguration = new ClientConfiguration()
                .withClientExecutionTimeout((int) TimeUnit.SECONDS.toMillis(config.getAwsClientRequestTimeoutSeconds()));

        final FunctionCode lambdaFunctionsCode = new FunctionCode()
                .withZipFile(ByteBuffer.wrap(Files.readAllBytes(new File(config.getLambdaCodePath()).toPath())));

        final String apiGatewayLambdaFunctionsPath = String.format(
                "arn:aws:apigateway:%s:lambda:path/2015-03-31/functions/",
                config.getAwsClientRegion());

        return new DeployerConfiguration.Builder()
                .withClientConfiguration(clientConfiguration)
                .withLambdaFunctionCode(lambdaFunctionsCode)
                .withLambdaRuntime(config.getLambdaRuntime())
                .withRegion(Regions.fromName(config.getAwsClientRegion()))
                .withAwsAccountId(config.getAwsAccountId())
                .withApiGatewayEndpoint(config.getApiGatewayEndpoint())
                .withApiGatewayLambdaFunctionsPath(apiGatewayLambdaFunctionsPath)
                .withApiStageName(config.getApiStageName())
                .withApiStageDescription(config.getApiStageDescription())
                .withApiStageMetricsEnabled(config.getApiStageMetricsEnabled())
                .withApiStageThrottlingRateLimit(config.getApiStageThrottlingRateLimit())
                .withApiStageThrottlingBurstLimit(config.getApiStageThrottlingBurstLimit())
                .withApiLoggingLevel(config.getApiLoggingLevel())
                .withApiDataTraceEnabled(config.getApiDataTraceEnabled())
                .withApiGeneratedSdkFolderName(config.getApiGeneratedSdkFolderName())
                .withApiGeneratedSdkOutputPath(config.getApiGeneratedSdkOutputPath())
                .withApiGeneratedSdkType(config.getApiGeneratedSdkType())
                .build();
    }

    private DeployerConfigurationConstants loadDeployerConfig() throws IOException {
        final String configurationConstantsFileContent = FileReader.readFile(configurationFilePath);
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(configurationConstantsFileContent, DeployerConfigurationConstants.class);
    }
}

package surf.deployers;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.model.FunctionCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
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

public class DeployerConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(DeployerConfiguration.class);

    private ClientConfiguration clientConfiguration;
    private Regions awsClientRegion;
    private FunctionCode lambdaFunctionCode;
    private String lambdaRuntime;
    private String apiGatewayEndpoint;
    private String awsAccountId;
    private String apiGatewayLambdaFunctionsPath;
    private String apiStageName;
    private String apiStageDescription;
    private Boolean apiStageMetricsEnabled;
    private Double apiStageThrottlingRateLimit;
    private Integer apiStageThrottlingBurstLimit;
    private String apiLoggingLevel;
    private Boolean apiDataTraceEnabled;
    private String apiGeneratedSdkFolderName;
    private String apiGeneratedSdkOutputPath;
    private String apiGeneratedSdkType;
    private String clientConfigFilePath;
    private String awsAccessKey;

    public static DeployerConfiguration fromFile(@Nonnull final String configFilePath) {
        try {
            return initializeDeployerConfiguration(configFilePath);
        } catch (IOException e) {
            LOG.error("Error while initializing deployer configuration!", e);
            System.exit(ExitCode.Error.getCode());
        }
        throw new IllegalStateException("This code path should not have been reached!");
    }


    private static DeployerConfiguration initializeDeployerConfiguration(
            @Nonnull final String configFilePath) throws IOException {

        final DeployerConfigurationConstants config = loadDeployerConfig(configFilePath);

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
                .withAWSClientRegion(Regions.fromName(config.getAwsClientRegion()))
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
                .withClientConfigFilepath(config.getClientConfigFilePath())
                .withAwsAccessKey(config.getAwsAccessKey())
                .build();
    }

    private static DeployerConfigurationConstants loadDeployerConfig(
            @Nonnull final String configFilePath) throws IOException {
        final String configurationConstantsFileContent = FileReader.readFile(configFilePath);
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(configurationConstantsFileContent, DeployerConfigurationConstants.class);
    }

    public ClientConfiguration getClientConfiguration() {
        return clientConfiguration;
    }

    private void setClientConfiguration(ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
    }

    public Regions getAwsClientRegion() {
        return awsClientRegion;
    }

    private void setAwsClientRegion(Regions awsClientRegion) {
        this.awsClientRegion = awsClientRegion;
    }

    public FunctionCode getLambdaFunctionCode() {
        return lambdaFunctionCode;
    }

    private void setLambdaFunctionCode(FunctionCode lambdaFunctionCode) {
        this.lambdaFunctionCode = lambdaFunctionCode;
    }

    public String getLambdaRuntime() {
        return lambdaRuntime;
    }

    private void setLambdaRuntime(String lambdaRuntime) {
        this.lambdaRuntime = lambdaRuntime;
    }

    public String getApiGatewayEndpoint() {
        return apiGatewayEndpoint;
    }

    private void setApiGatewayEndpoint(String apiGatewayEndpoint) {
        this.apiGatewayEndpoint = apiGatewayEndpoint;
    }

    public String getAwsAccountId() {
        return awsAccountId;
    }

    private void setAwsAccountId(String awsAccountId) {
        this.awsAccountId = awsAccountId;
    }

    public String getApiGatewayLambdaFunctionsPath() {
        return apiGatewayLambdaFunctionsPath;
    }

    private void setApiGatewayLambdaFunctionsPath(String path) {
        this.apiGatewayLambdaFunctionsPath = path;
    }

    public String getApiStageName() {
        return apiStageName;
    }

    private void setApiStageName(String apiStageName) {
        this.apiStageName = apiStageName;
    }

    public String getApiStageDescription() {
        return apiStageDescription;
    }

    private void setApiStageDescription(String apiStageDescription) {
        this.apiStageDescription = apiStageDescription;
    }

    public Boolean getApiStageMetricsEnabled() {
        return apiStageMetricsEnabled;
    }

    private void setApiStageMetricsEnabled(Boolean apiStageMetricsEnabled) {
        this.apiStageMetricsEnabled = apiStageMetricsEnabled;
    }

    public Double getApiStageThrottlingRateLimit() {
        return apiStageThrottlingRateLimit;
    }

    private void setApiStageThrottlingRateLimit(Double apiStageThrottlingRateLimit) {
        this.apiStageThrottlingRateLimit = apiStageThrottlingRateLimit;
    }

    public Integer getApiStageThrottlingBurstLimit() {
        return apiStageThrottlingBurstLimit;
    }

    private void setApiStageThrottlingBurstLimit(Integer apiStageThrottlingBurstLimit) {
        this.apiStageThrottlingBurstLimit = apiStageThrottlingBurstLimit;
    }

    public String getApiLoggingLevel() {
        return apiLoggingLevel;
    }

    private void setApiLoggingLevel(String apiLoggingLevel) {
        this.apiLoggingLevel = apiLoggingLevel;
    }

    public Boolean getApiDataTraceEnabled() {
        return apiDataTraceEnabled;
    }

    private void setApiDataTraceEnabled(Boolean apiDataTraceEnabled) {
        this.apiDataTraceEnabled = apiDataTraceEnabled;
    }

    public String getApiGeneratedSdkFolderName() {
        return apiGeneratedSdkFolderName;
    }

    private void setApiGeneratedSdkFolderName(String apiGeneratedSdkFolderName) {
        this.apiGeneratedSdkFolderName = apiGeneratedSdkFolderName;
    }

    public String getApiGeneratedSdkOutputPath() {
        return apiGeneratedSdkOutputPath;
    }

    private void setApiGeneratedSdkOutputPath(String apiGeneratedSdkOutputPath) {
        this.apiGeneratedSdkOutputPath = apiGeneratedSdkOutputPath;
    }

    public String getApiGeneratedSdkType() {
        return apiGeneratedSdkType;
    }

    private void setApiGeneratedSdkType(String apiGeneratedSdkType) {
        this.apiGeneratedSdkType = apiGeneratedSdkType;
    }

    public String getClientConfigFilePath() {
        return clientConfigFilePath;
    }

    private void setClientConfigFilePath(String clientConfigFilePath) {
        this.clientConfigFilePath = clientConfigFilePath;
    }

    public String getAwsAccessKey() {
        return awsAccessKey;
    }

    private void setAwsAccessKey(String awsAccessKey) {
        this.awsAccessKey = awsAccessKey;
    }

    public static class Builder {
        private ClientConfiguration clientConfiguration;
        private Regions region;
        private FunctionCode lambdaFunctionCode;
        private String lambdaRuntime;
        private String awsAccountId;
        private String apiGatewayEndpoint;
        private String apiGatewayLambdaFunctionsPath;
        private String apiStageName;
        private String apiStageDescription;
        private Boolean apiStageMetricsEnabled;
        private Double apiStageThrottlingRateLimit;
        private Integer apiStageThrottlingBurstLimit;
        private String apiLoggingLevel;
        private Boolean apiDataTraceEnabled;
        private String apiGeneratedSdkFolderName;
        private String apiGeneratedSdkOutputPath;
        private String apiGeneratedSdkType;
        private String clientConfigFilePath;
        private String awsAccessKey;

        public DeployerConfiguration build() {
            Preconditions.checkNotNull(clientConfiguration);
            Preconditions.checkNotNull(region);
            Preconditions.checkNotNull(lambdaFunctionCode);
            Preconditions.checkNotNull(lambdaRuntime);
            Preconditions.checkNotNull(awsAccountId);
            Preconditions.checkNotNull(apiGatewayEndpoint);
            Preconditions.checkNotNull(apiGatewayLambdaFunctionsPath);
            Preconditions.checkNotNull(apiStageName);
            Preconditions.checkNotNull(apiStageDescription);
            Preconditions.checkNotNull(apiStageMetricsEnabled);
            Preconditions.checkNotNull(apiStageThrottlingRateLimit);
            Preconditions.checkNotNull(apiStageThrottlingBurstLimit);
            Preconditions.checkNotNull(apiDataTraceEnabled);
            Preconditions.checkNotNull(apiLoggingLevel);
            Preconditions.checkNotNull(apiGeneratedSdkFolderName);
            Preconditions.checkNotNull(apiGeneratedSdkOutputPath);
            Preconditions.checkNotNull(apiGeneratedSdkType);
            Preconditions.checkNotNull(clientConfigFilePath);
            Preconditions.checkNotNull(awsAccessKey);

            final DeployerConfiguration deployerConfiguration = new DeployerConfiguration();
            deployerConfiguration.setClientConfiguration(clientConfiguration);
            deployerConfiguration.setLambdaFunctionCode(lambdaFunctionCode);
            deployerConfiguration.setAwsClientRegion(region);
            deployerConfiguration.setLambdaRuntime(lambdaRuntime);
            deployerConfiguration.setAwsAccountId(awsAccountId);
            deployerConfiguration.setApiGatewayEndpoint(apiGatewayEndpoint);
            deployerConfiguration.setApiGatewayLambdaFunctionsPath(apiGatewayLambdaFunctionsPath);
            deployerConfiguration.setApiStageName(apiStageName);
            deployerConfiguration.setApiStageDescription(apiStageDescription);
            deployerConfiguration.setApiStageMetricsEnabled(apiStageMetricsEnabled);
            deployerConfiguration.setApiStageThrottlingBurstLimit(apiStageThrottlingBurstLimit);
            deployerConfiguration.setApiStageThrottlingRateLimit(apiStageThrottlingRateLimit);
            deployerConfiguration.setApiLoggingLevel(apiLoggingLevel);
            deployerConfiguration.setApiDataTraceEnabled(apiDataTraceEnabled);
            deployerConfiguration.setApiGeneratedSdkFolderName(apiGeneratedSdkFolderName);
            deployerConfiguration.setApiGeneratedSdkOutputPath(apiGeneratedSdkOutputPath);
            deployerConfiguration.setApiGeneratedSdkType(apiGeneratedSdkType);
            deployerConfiguration.setClientConfigFilePath(clientConfigFilePath);
            deployerConfiguration.setAwsAccessKey(awsAccessKey);
            return deployerConfiguration;
        }

        public DeployerConfiguration.Builder withClientConfiguration(@Nonnull final ClientConfiguration clientConfig) {
            Preconditions.checkNotNull(clientConfig);
            this.clientConfiguration = clientConfig;
            return this;
        }

        public DeployerConfiguration.Builder withAWSClientRegion(@Nonnull final Regions region) {
            Preconditions.checkNotNull(region);
            this.region = region;
            return this;
        }

        public DeployerConfiguration.Builder withLambdaFunctionCode(@Nonnull final FunctionCode lambdaFunctionCode) {
            Preconditions.checkNotNull(lambdaFunctionCode);
            this.lambdaFunctionCode = lambdaFunctionCode;
            return this;
        }

        public DeployerConfiguration.Builder withLambdaRuntime(@Nonnull final String lambdaRuntime) {
            Preconditions.checkNotNull(lambdaRuntime);
            this.lambdaRuntime = lambdaRuntime;
            return this;
        }

        public DeployerConfiguration.Builder withAwsAccountId(@Nonnull final String awsAccountId) {
            Preconditions.checkNotNull(awsAccountId);
            this.awsAccountId = awsAccountId;
            return this;
        }

        public Builder withApiGatewayEndpoint(@Nonnull final String apiGatewayEndpoint) {
            Preconditions.checkNotNull(apiGatewayEndpoint);
            this.apiGatewayEndpoint = apiGatewayEndpoint;
            return this;
        }

        public DeployerConfiguration.Builder withApiGatewayLambdaFunctionsPath(@Nonnull final String path) {
            Preconditions.checkNotNull(path);
            this.apiGatewayLambdaFunctionsPath = path;
            return this;
        }

        public DeployerConfiguration.Builder withApiStageName(@Nonnull final String apiStageName) {
            Preconditions.checkNotNull(apiStageName);
            this.apiStageName = apiStageName;
            return this;
        }

        public DeployerConfiguration.Builder withApiStageDescription(@Nonnull final String apiStageDescription) {
            Preconditions.checkNotNull(apiStageDescription);
            this.apiStageDescription = apiStageDescription;
            return this;
        }

        public DeployerConfiguration.Builder withApiStageMetricsEnabled(@Nonnull final Boolean apiStageMetricsEnabled) {
            Preconditions.checkNotNull(apiStageMetricsEnabled);
            this.apiStageMetricsEnabled = apiStageMetricsEnabled;
            return this;
        }

        public DeployerConfiguration.Builder withApiStageThrottlingRateLimit(@Nonnull final Double apiStageThrottlingRateLimit) {
            Preconditions.checkNotNull(apiStageThrottlingRateLimit);
            this.apiStageThrottlingRateLimit = apiStageThrottlingRateLimit;
            return this;
        }

        public DeployerConfiguration.Builder withApiStageThrottlingBurstLimit(@Nonnull final Integer apiStageThrottlingBurstLimit) {
            Preconditions.checkNotNull(apiStageThrottlingBurstLimit);
            this.apiStageThrottlingBurstLimit = apiStageThrottlingBurstLimit;
            return this;
        }

        public DeployerConfiguration.Builder withApiLoggingLevel(@Nonnull final String apiLoggingLevel) {
            Preconditions.checkNotNull(apiLoggingLevel);
            this.apiLoggingLevel = apiLoggingLevel;
            return this;
        }

        public DeployerConfiguration.Builder withApiDataTraceEnabled(@Nonnull final Boolean apiDataTraceEnabled) {
            Preconditions.checkNotNull(apiDataTraceEnabled);
            this.apiDataTraceEnabled = apiDataTraceEnabled;
            return this;
        }

        public DeployerConfiguration.Builder withApiGeneratedSdkFolderName(@Nonnull final String apiGeneratedSdkFolderName) {
            Preconditions.checkNotNull(apiGeneratedSdkFolderName);
            this.apiGeneratedSdkFolderName = apiGeneratedSdkFolderName;
            return this;
        }

        public DeployerConfiguration.Builder withApiGeneratedSdkOutputPath(@Nonnull final String apiGeneratedSdkOutputPath) {
            Preconditions.checkNotNull(apiGeneratedSdkOutputPath);
            this.apiGeneratedSdkOutputPath = apiGeneratedSdkOutputPath;
            return this;
        }

        public DeployerConfiguration.Builder withApiGeneratedSdkType(@Nonnull final String apiGeneratedSdkType) {
            Preconditions.checkNotNull(apiGeneratedSdkType);
            this.apiGeneratedSdkType = apiGeneratedSdkType;
            return this;
        }

        public DeployerConfiguration.Builder withClientConfigFilepath(@Nonnull final String clientConfigFilePath) {
            Preconditions.checkNotNull(clientConfigFilePath);
            this.clientConfigFilePath = clientConfigFilePath;
            return this;
        }

        public DeployerConfiguration.Builder withAwsAccessKey(@Nonnull final String awsAccessKey) {
            Preconditions.checkNotNull(awsAccessKey);
            this.awsAccessKey = awsAccessKey;
            return this;
        }
    }
}

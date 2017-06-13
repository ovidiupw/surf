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
    private boolean apiStageMetricsEnabled;
    private double apiStageThrottlingRateLimit;
    private int apiStageThrottlingBurstLimit;
    private String apiLoggingLevel;
    private boolean apiDataTraceEnabled;
    private String apiGeneratedSdkFolderName;
    private String apiGeneratedSdkOutputPath;
    private String apiGeneratedSdkType;
    private String clientConfigFilePath;
    private String lambdaConfigFilePath;
    private String awsAccessKey;
    private long dynamoDBWorkflowsTableReadCapacityUnits;
    private long dynamoDBWorkflowsTableWriteCapacityUnits;
    private long dynamoDBWorkflowsTableOwnerGSIReadCapacityUnits;
    private long dynamoDBWorkflowsTableOwnerGSIWriteCapacityUnits;
    private long dynamoDBWorkflowExecutionsTableReadCapacityUnits;
    private long dynamoDBWorkflowExecutionsTableWriteCapacityUnits;
    private long dynamoDBWorkflowExecutionTasksTableReadCapacityUnits;
    private long dynamoDBWorkflowExecutionTasksTableWriteCapacityUnits;
    private long dynamoDBCrawlMetadataTableReadCapacityUnits;
    private long dynamoDBCrawlMetadataTableWriteCapacityUnits;

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
                .withClientConfigFilePath(config.getClientConfigFilePath())
                .withLambdaConfigFilePath(config.getLambdaConfigFilePath())
                .withAwsAccessKey(config.getAwsAccessKey())
                .withDynamoDBWorkflowsTableReadCapacityUnits(config.getDynamoDBWorkflowsTableReadCapacityUnits())
                .withDynamoDBWorkflowsTableWriteCapacityUnits(config.getDynamoDBWorkflowsTableWriteCapacityUnits())
                .withDynamoDBWorkflowsTableOwnerGSIReadCapacityUnits(config.getDynamoDBWorkflowsTableOwnerGSIReadCapacityUnits())
                .withDynamoDBWorkflowsTableOwnerGSIWriteCapacityUnits(config.getDynamoDBWorkflowsTableOwnerGSIWriteCapacityUnits())
                .withDynamoDBWorkflowExecutionsTableReadCapacityUnits(config.getDynamoDBWorkflowExecutionsTableReadCapacityUnits())
                .withDynamoDBWorkflowExecutionsTableWriteCapacityUnits(config.getDynamoDBWorkflowExecutionsTableWriteCapacityUnits())
                .withDynamoDBWorkflowExecutionTasksTableReadCapacityUnits(config.getDynamoDBWorkflowExecutionTasksTableReadCapacityUnits())
                .withDynamoDBWorkflowExecutionTasksTableWriteCapacityUnits(config.getDynamoDBWorkflowExecutionTasksTableWriteCapacityUnits())
                .withDynamoDBCrawlMetadataTableReadCapacityUnits(config.getDynamoDBCrawlMetadataTableReadCapacityUnits())
                .withDynamoDBCrawlMetadataTableWriteCapacityUnits(config.getDynamoDBCrawlMetadataTableWriteCapacityUnits())
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

    public boolean getApiStageMetricsEnabled() {
        return apiStageMetricsEnabled;
    }

    private void setApiStageMetricsEnabled(boolean apiStageMetricsEnabled) {
        this.apiStageMetricsEnabled = apiStageMetricsEnabled;
    }

    public double getApiStageThrottlingRateLimit() {
        return apiStageThrottlingRateLimit;
    }

    private void setApiStageThrottlingRateLimit(double apiStageThrottlingRateLimit) {
        this.apiStageThrottlingRateLimit = apiStageThrottlingRateLimit;
    }

    public int getApiStageThrottlingBurstLimit() {
        return apiStageThrottlingBurstLimit;
    }

    private void setApiStageThrottlingBurstLimit(int apiStageThrottlingBurstLimit) {
        this.apiStageThrottlingBurstLimit = apiStageThrottlingBurstLimit;
    }

    public String getApiLoggingLevel() {
        return apiLoggingLevel;
    }

    private void setApiLoggingLevel(String apiLoggingLevel) {
        this.apiLoggingLevel = apiLoggingLevel;
    }

    public boolean getApiDataTraceEnabled() {
        return apiDataTraceEnabled;
    }

    private void setApiDataTraceEnabled(boolean apiDataTraceEnabled) {
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

    public long getDynamoDBWorkflowsTableReadCapacityUnits() {
        return dynamoDBWorkflowsTableReadCapacityUnits;
    }

    private void setDynamoDBWorkflowsTableReadCapacityUnits(long dynamoDBWorkflowsTableReadCapacityUnits) {
        this.dynamoDBWorkflowsTableReadCapacityUnits = dynamoDBWorkflowsTableReadCapacityUnits;
    }

    public long getDynamoDBWorkflowsTableWriteCapacityUnits() {
        return dynamoDBWorkflowsTableWriteCapacityUnits;
    }

    private void setDynamoDBWorkflowsTableWriteCapacityUnits(long dynamoDBWorkflowsTableWriteCapacityUnits) {
        this.dynamoDBWorkflowsTableWriteCapacityUnits = dynamoDBWorkflowsTableWriteCapacityUnits;
    }

    public long getDynamoDBWorkflowsTableOwnerGSIReadCapacityUnits() {
        return dynamoDBWorkflowsTableOwnerGSIReadCapacityUnits;
    }

    public long getDynamoDBWorkflowsTableOwnerGSIWriteCapacityUnits() {
        return dynamoDBWorkflowsTableOwnerGSIWriteCapacityUnits;
    }

    public long getDynamoDBWorkflowExecutionsTableReadCapacityUnits() {
        return dynamoDBWorkflowExecutionsTableReadCapacityUnits;
    }

    public long getDynamoDBWorkflowExecutionsTableWriteCapacityUnits() {
        return dynamoDBWorkflowExecutionsTableWriteCapacityUnits;
    }

    public long getDynamoDBWorkflowExecutionTasksTableReadCapacityUnits() {
        return dynamoDBWorkflowExecutionTasksTableReadCapacityUnits;
    }

    public long getDynamoDBWorkflowExecutionTasksTableWriteCapacityUnits() {
        return dynamoDBWorkflowExecutionTasksTableWriteCapacityUnits;
    }

    private void setDynamoDBWorkflowsTableOwnerGSIReadCapacityUnits(long dynamoDBWorkflowsTableOwnerGSIReadCapacityUnits) {
        this.dynamoDBWorkflowsTableOwnerGSIReadCapacityUnits = dynamoDBWorkflowsTableOwnerGSIReadCapacityUnits;
    }

    private void setDynamoDBWorkflowsTableOwnerGSIWriteCapacityUnits(long dynamoDBWorkflowsTableOwnerGSIWriteCapacityUnits) {
        this.dynamoDBWorkflowsTableOwnerGSIWriteCapacityUnits = dynamoDBWorkflowsTableOwnerGSIWriteCapacityUnits;
    }

    private void setDynamoDBWorkflowExecutionsTableReadCapacityUnits(long dynamoDBWorkflowExecutionsTableReadCapacityUnits) {
        this.dynamoDBWorkflowExecutionsTableReadCapacityUnits = dynamoDBWorkflowExecutionsTableReadCapacityUnits;
    }

    private void setDynamoDBWorkflowExecutionsTableWriteCapacityUnits(long dynamoDBWorkflowExecutionsTableWriteCapacityUnits) {
        this.dynamoDBWorkflowExecutionsTableWriteCapacityUnits = dynamoDBWorkflowExecutionsTableWriteCapacityUnits;
    }

    private void setDynamoDBWorkflowExecutionTasksTableReadCapacityUnits(long dynamoDBWorkflowExecutionTasksTableReadCapacityUnits) {
        this.dynamoDBWorkflowExecutionTasksTableReadCapacityUnits = dynamoDBWorkflowExecutionTasksTableReadCapacityUnits;
    }

    private void setDynamoDBWorkflowExecutionTasksTableWriteCapacityUnits(long dynamoDBWorkflowExecutionTasksTableWriteCapacityUnits) {
        this.dynamoDBWorkflowExecutionTasksTableWriteCapacityUnits = dynamoDBWorkflowExecutionTasksTableWriteCapacityUnits;
    }

    public long getDynamoDBCrawlMetadataTableReadCapacityUnits() {
        return dynamoDBCrawlMetadataTableReadCapacityUnits;
    }

    public long getDynamoDBCrawlMetadataTableWriteCapacityUnits() {
        return dynamoDBCrawlMetadataTableWriteCapacityUnits;
    }

    private void setDynamoDBCrawlMetadataTableReadCapacityUnits(long dynamoDBCrawlMetadataTableReadCapacityUnits) {
        this.dynamoDBCrawlMetadataTableReadCapacityUnits = dynamoDBCrawlMetadataTableReadCapacityUnits;
    }

    private void setDynamoDBCrawlMetadataTableWriteCapacityUnits(long dynamoDBCrawlMetadataTableWriteCapacityUnits) {
        this.dynamoDBCrawlMetadataTableWriteCapacityUnits = dynamoDBCrawlMetadataTableWriteCapacityUnits;
    }

    public String getLambdaConfigFilePath() {
        return lambdaConfigFilePath;
    }

    private void setLambdaConfigFilePath(String lambdaConfigFilePath) {
        this.lambdaConfigFilePath = lambdaConfigFilePath;
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
        private boolean apiStageMetricsEnabled;
        private double apiStageThrottlingRateLimit;
        private int apiStageThrottlingBurstLimit;
        private String apiLoggingLevel;
        private boolean apiDataTraceEnabled;
        private String apiGeneratedSdkFolderName;
        private String apiGeneratedSdkOutputPath;
        private String apiGeneratedSdkType;
        private String clientConfigFilePath;
        private String lambdaConfigFilePath;
        private String awsAccessKey;
        private long dynamoDBWorkflowsTableReadCapacityUnits;
        private long dynamoDBWorkflowsTableWriteCapacityUnits;
        private long dynamoDBWorkflowsTableOwnerGSIReadCapacityUnits;
        private long dynamoDBWorkflowsTableOwnerGSIWriteCapacityUnits;
        private long dynamoDBWorkflowExecutionsTableReadCapacityUnits;
        private long dynamoDBWorkflowExecutionsTableWriteCapacityUnits;
        private long dynamoDBWorkflowExecutionTasksTableReadCapacityUnits;
        private long dynamoDBWorkflowExecutionTasksTableWriteCapacityUnits;
        private long dynamoDBCrawlMetadataTableReadCapacityUnits;
        private long dynamoDBCrawlMetadataTableWriteCapacityUnits;

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
            Preconditions.checkNotNull(lambdaConfigFilePath);
            Preconditions.checkNotNull(awsAccessKey);
            Preconditions.checkArgument(dynamoDBWorkflowsTableReadCapacityUnits >= 0);
            Preconditions.checkArgument(dynamoDBWorkflowsTableOwnerGSIReadCapacityUnits >= 0);
            Preconditions.checkArgument(dynamoDBWorkflowsTableOwnerGSIWriteCapacityUnits >= 0);
            Preconditions.checkArgument(dynamoDBWorkflowExecutionsTableReadCapacityUnits >= 0);
            Preconditions.checkArgument(dynamoDBWorkflowExecutionsTableWriteCapacityUnits >= 0);
            Preconditions.checkArgument(dynamoDBWorkflowExecutionTasksTableReadCapacityUnits >= 0);
            Preconditions.checkArgument(dynamoDBWorkflowExecutionTasksTableWriteCapacityUnits >= 0);
            Preconditions.checkArgument(dynamoDBCrawlMetadataTableReadCapacityUnits >= 0);
            Preconditions.checkArgument(dynamoDBCrawlMetadataTableWriteCapacityUnits >= 0);

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
            deployerConfiguration.setLambdaConfigFilePath(lambdaConfigFilePath);
            deployerConfiguration.setAwsAccessKey(awsAccessKey);
            deployerConfiguration.setDynamoDBWorkflowsTableReadCapacityUnits(dynamoDBWorkflowsTableReadCapacityUnits);
            deployerConfiguration.setDynamoDBWorkflowsTableWriteCapacityUnits(dynamoDBWorkflowsTableWriteCapacityUnits);
            deployerConfiguration.setDynamoDBWorkflowsTableOwnerGSIReadCapacityUnits(dynamoDBWorkflowsTableOwnerGSIReadCapacityUnits);
            deployerConfiguration.setDynamoDBWorkflowsTableOwnerGSIWriteCapacityUnits(dynamoDBWorkflowsTableOwnerGSIWriteCapacityUnits);
            deployerConfiguration.setDynamoDBWorkflowExecutionsTableReadCapacityUnits(dynamoDBWorkflowExecutionsTableReadCapacityUnits);
            deployerConfiguration.setDynamoDBWorkflowExecutionsTableWriteCapacityUnits(dynamoDBWorkflowExecutionsTableWriteCapacityUnits);
            deployerConfiguration.setDynamoDBWorkflowExecutionTasksTableReadCapacityUnits(dynamoDBWorkflowExecutionTasksTableReadCapacityUnits);
            deployerConfiguration.setDynamoDBWorkflowExecutionTasksTableWriteCapacityUnits(dynamoDBWorkflowExecutionTasksTableWriteCapacityUnits);
            deployerConfiguration.setDynamoDBCrawlMetadataTableReadCapacityUnits(dynamoDBCrawlMetadataTableReadCapacityUnits);
            deployerConfiguration.setDynamoDBCrawlMetadataTableWriteCapacityUnits(dynamoDBCrawlMetadataTableWriteCapacityUnits);
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

        public DeployerConfiguration.Builder withApiStageMetricsEnabled(@Nonnull final boolean apiStageMetricsEnabled) {
            this.apiStageMetricsEnabled = apiStageMetricsEnabled;
            return this;
        }

        public DeployerConfiguration.Builder withApiStageThrottlingRateLimit(@Nonnull final double apiStageThrottlingRateLimit) {
            Preconditions.checkArgument(apiStageThrottlingRateLimit > 0);
            this.apiStageThrottlingRateLimit = apiStageThrottlingRateLimit;
            return this;
        }

        public DeployerConfiguration.Builder withApiStageThrottlingBurstLimit(@Nonnull final int apiStageThrottlingBurstLimit) {
            Preconditions.checkArgument(apiStageThrottlingBurstLimit > 0);
            this.apiStageThrottlingBurstLimit = apiStageThrottlingBurstLimit;
            return this;
        }

        public DeployerConfiguration.Builder withApiLoggingLevel(@Nonnull final String apiLoggingLevel) {
            Preconditions.checkNotNull(apiLoggingLevel);
            this.apiLoggingLevel = apiLoggingLevel;
            return this;
        }

        public DeployerConfiguration.Builder withApiDataTraceEnabled(@Nonnull final boolean apiDataTraceEnabled) {
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

        public DeployerConfiguration.Builder withClientConfigFilePath(@Nonnull final String filePath) {
            Preconditions.checkNotNull(filePath);
            this.clientConfigFilePath = filePath;
            return this;
        }

        public DeployerConfiguration.Builder withLambdaConfigFilePath(@Nonnull final String filePath) {
            Preconditions.checkNotNull(filePath);
            this.lambdaConfigFilePath = filePath;
            return this;
        }

        public DeployerConfiguration.Builder withAwsAccessKey(@Nonnull final String awsAccessKey) {
            Preconditions.checkNotNull(awsAccessKey);
            this.awsAccessKey = awsAccessKey;
            return this;
        }

        public DeployerConfiguration.Builder withDynamoDBWorkflowsTableReadCapacityUnits(@Nonnull final long units) {
            Preconditions.checkArgument(units > 0);
            this.dynamoDBWorkflowsTableReadCapacityUnits = units;
            return this;
        }

        public DeployerConfiguration.Builder withDynamoDBWorkflowsTableWriteCapacityUnits(@Nonnull final long units) {
            Preconditions.checkArgument(units > 0);
            this.dynamoDBWorkflowsTableWriteCapacityUnits = units;
            return this;
        }

        public DeployerConfiguration.Builder withDynamoDBWorkflowsTableOwnerGSIReadCapacityUnits(@Nonnull final long units) {
            Preconditions.checkArgument(units > 0);
            this.dynamoDBWorkflowsTableOwnerGSIReadCapacityUnits = units;
            return this;
        }

        public DeployerConfiguration.Builder withDynamoDBWorkflowsTableOwnerGSIWriteCapacityUnits(@Nonnull final long units) {
            Preconditions.checkArgument(units > 0);
            this.dynamoDBWorkflowsTableOwnerGSIWriteCapacityUnits = units;
            return this;
        }

        public DeployerConfiguration.Builder withDynamoDBWorkflowExecutionsTableReadCapacityUnits(@Nonnull final long units) {
            Preconditions.checkArgument(units > 0);
            this.dynamoDBWorkflowExecutionsTableReadCapacityUnits = units;
            return this;
        }

        public DeployerConfiguration.Builder withDynamoDBWorkflowExecutionsTableWriteCapacityUnits(@Nonnull final long units) {
            Preconditions.checkArgument(units > 0);
            this.dynamoDBWorkflowExecutionsTableWriteCapacityUnits = units;
            return this;
        }

        public DeployerConfiguration.Builder withDynamoDBWorkflowExecutionTasksTableReadCapacityUnits(@Nonnull final long units) {
            Preconditions.checkArgument(units > 0);
            this.dynamoDBWorkflowExecutionTasksTableReadCapacityUnits = units;
            return this;
        }

        public DeployerConfiguration.Builder withDynamoDBWorkflowExecutionTasksTableWriteCapacityUnits(@Nonnull final long units) {
            Preconditions.checkArgument(units > 0);
            this.dynamoDBWorkflowExecutionTasksTableWriteCapacityUnits = units;
            return this;
        }

        public DeployerConfiguration.Builder withDynamoDBCrawlMetadataTableReadCapacityUnits(@Nonnull final long units) {
            Preconditions.checkArgument(units > 0);
            this.dynamoDBCrawlMetadataTableReadCapacityUnits = units;
            return this;
        }

        public DeployerConfiguration.Builder withDynamoDBCrawlMetadataTableWriteCapacityUnits(@Nonnull final long units) {
            Preconditions.checkArgument(units > 0);
            this.dynamoDBCrawlMetadataTableWriteCapacityUnits = units;
            return this;
        }
    }
}

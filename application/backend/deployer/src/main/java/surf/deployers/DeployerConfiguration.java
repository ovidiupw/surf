package surf.deployers;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.model.FunctionCode;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

public class DeployerConfiguration {
    private ClientConfiguration clientConfiguration;
    private Regions region;
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

    public ClientConfiguration getClientConfiguration() {
        return clientConfiguration;
    }

    private void setClientConfiguration(ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
    }

    public Regions getRegion() {
        return region;
    }

    private void setRegion(Regions region) {
        this.region = region;
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

            final DeployerConfiguration deployerConfiguration = new DeployerConfiguration();
            deployerConfiguration.setClientConfiguration(clientConfiguration);
            deployerConfiguration.setLambdaFunctionCode(lambdaFunctionCode);
            deployerConfiguration.setRegion(region);
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
            return deployerConfiguration;
        }

        public DeployerConfiguration.Builder withClientConfiguration(@Nonnull final ClientConfiguration clientConfig) {
            Preconditions.checkNotNull(clientConfig);
            this.clientConfiguration = clientConfig;
            return this;
        }

        public DeployerConfiguration.Builder withRegion(@Nonnull final Regions region) {
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
    }
}

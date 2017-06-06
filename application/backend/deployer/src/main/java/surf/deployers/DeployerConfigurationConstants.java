package surf.deployers;

public class DeployerConfigurationConstants {
    private String awsAccountId;
    private String awsAccessKey;
    private int awsClientRequestTimeoutSeconds;
    private String awsClientRegion;
    private String lambdaCodePath;
    private String lambdaRuntime;
    private String apiGatewayEndpoint;
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

    public String getClientConfigFilePath() {
        return clientConfigFilePath;
    }

    public void setClientConfigFilePath(String clientConfigFilePath) {
        this.clientConfigFilePath = clientConfigFilePath;
    }

    public void setApiLoggingLevel(String apiLoggingLevel) {
        this.apiLoggingLevel = apiLoggingLevel;
    }

    public void setApiDataTraceEnabled(boolean apiDataTraceEnabled) {
        this.apiDataTraceEnabled = apiDataTraceEnabled;
    }

    public boolean getApiStageMetricsEnabled() {
        return apiStageMetricsEnabled;
    }

    public void setApiStageMetricsEnabled(boolean apiStageMetricsEnabled) {
        this.apiStageMetricsEnabled = apiStageMetricsEnabled;
    }

    public double getApiStageThrottlingRateLimit() {
        return apiStageThrottlingRateLimit;
    }

    public void setApiStageThrottlingRateLimit(double apiStageThrottlingRateLimit) {
        this.apiStageThrottlingRateLimit = apiStageThrottlingRateLimit;
    }

    public int getApiStageThrottlingBurstLimit() {
        return apiStageThrottlingBurstLimit;
    }

    public void setApiStageThrottlingBurstLimit(int apiStageThrottlingBurstLimit) {
        this.apiStageThrottlingBurstLimit = apiStageThrottlingBurstLimit;
    }

    public String getAwsAccountId() {
        return awsAccountId;
    }

    public void setAwsAccountId(String awsAccountId) {
        this.awsAccountId = awsAccountId;
    }

    public int getAwsClientRequestTimeoutSeconds() {
        return awsClientRequestTimeoutSeconds;
    }

    public void setAwsClientRequestTimeoutSeconds(int awsClientRequestTimeoutSeconds) {
        this.awsClientRequestTimeoutSeconds = awsClientRequestTimeoutSeconds;
    }

    public String getAwsClientRegion() {
        return awsClientRegion;
    }

    public void setAwsClientRegion(String awsClientRegion) {
        this.awsClientRegion = awsClientRegion;
    }

    public String getLambdaCodePath() {
        return lambdaCodePath;
    }

    public void setLambdaCodePath(String lambdaCodePath) {
        this.lambdaCodePath = lambdaCodePath;
    }

    public String getLambdaRuntime() {
        return lambdaRuntime;
    }

    public void setLambdaRuntime(String lambdaRuntime) {
        this.lambdaRuntime = lambdaRuntime;
    }

    public String getApiGatewayEndpoint() {
        return apiGatewayEndpoint;
    }

    public void setApiGatewayEndpoint(String apiGatewayEndpoint) {
        this.apiGatewayEndpoint = apiGatewayEndpoint;
    }

    public String getApiStageName() {
        return apiStageName;
    }

    public void setApiStageName(String apiStageName) {
        this.apiStageName = apiStageName;
    }

    public String getApiStageDescription() {
        return apiStageDescription;
    }

    public void setApiStageDescription(String apiStageDescription) {
        this.apiStageDescription = apiStageDescription;
    }

    public String getApiLoggingLevel() {
        return apiLoggingLevel;
    }

    public boolean getApiDataTraceEnabled() {
        return apiDataTraceEnabled;
    }

    public String getApiGeneratedSdkFolderName() {
        return apiGeneratedSdkFolderName;
    }

    public String getApiGeneratedSdkOutputPath() {
        return apiGeneratedSdkOutputPath;
    }

    public String getAwsAccessKey() {
        return awsAccessKey;
    }

    public void setAwsAccessKey(String awsAccessKey) {
        this.awsAccessKey = awsAccessKey;
    }

    public void setApiGeneratedSdkFolderName(String apiGeneratedSdkFolderName) {
        this.apiGeneratedSdkFolderName = apiGeneratedSdkFolderName;
    }

    public void setApiGeneratedSdkOutputPath(String apiGeneratedSdkOutputPath) {
        this.apiGeneratedSdkOutputPath = apiGeneratedSdkOutputPath;
    }

    public void setApiGeneratedSdkType(String apiGeneratedSdkType) {
        this.apiGeneratedSdkType = apiGeneratedSdkType;
    }

    public String getApiGeneratedSdkType() {

        return apiGeneratedSdkType;
    }
}

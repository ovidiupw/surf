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
    private String lambdaConfigFilePath;
    private long dynamoDBWorkflowExecutionsTableWorkflowIdGSIReadCapacityUnits;
    private long dynamoDBWorkflowExecutionsTableWorkflowIdGSIWriteCapacityUnits;
    private long dynamoDBWorkflowExecutionTasksTableStatusDepthGSIReadCapacityUnits;
    private long dynamoDBWorkflowExecutionTasksTableStatusDepthGSIWriteCapacityUnits;

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

    public long getDynamoDBWorkflowsTableReadCapacityUnits() {
        return dynamoDBWorkflowsTableReadCapacityUnits;
    }

    public long getDynamoDBWorkflowsTableWriteCapacityUnits() {
        return dynamoDBWorkflowsTableWriteCapacityUnits;
    }

    public void setDynamoDBWorkflowsTableReadCapacityUnits(long dynamoDBWorkflowsTableReadCapacityUnits) {
        this.dynamoDBWorkflowsTableReadCapacityUnits = dynamoDBWorkflowsTableReadCapacityUnits;
    }

    public void setDynamoDBWorkflowsTableWriteCapacityUnits(long dynamoDBWorkflowsTableWriteCapacityUnits) {
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

    public void setDynamoDBWorkflowsTableOwnerGSIReadCapacityUnits(long dynamoDBWorkflowsTableOwnerGSIReadCapacityUnits) {
        this.dynamoDBWorkflowsTableOwnerGSIReadCapacityUnits = dynamoDBWorkflowsTableOwnerGSIReadCapacityUnits;
    }

    public void setDynamoDBWorkflowsTableOwnerGSIWriteCapacityUnits(long dynamoDBWorkflowsTableOwnerGSIWriteCapacityUnits) {
        this.dynamoDBWorkflowsTableOwnerGSIWriteCapacityUnits = dynamoDBWorkflowsTableOwnerGSIWriteCapacityUnits;
    }

    public void setDynamoDBWorkflowExecutionsTableReadCapacityUnits(long dynamoDBWorkflowExecutionsTableReadCapacityUnits) {
        this.dynamoDBWorkflowExecutionsTableReadCapacityUnits = dynamoDBWorkflowExecutionsTableReadCapacityUnits;
    }

    public void setDynamoDBWorkflowExecutionsTableWriteCapacityUnits(long dynamoDBWorkflowExecutionsTableWriteCapacityUnits) {
        this.dynamoDBWorkflowExecutionsTableWriteCapacityUnits = dynamoDBWorkflowExecutionsTableWriteCapacityUnits;
    }

    public void setDynamoDBWorkflowExecutionTasksTableReadCapacityUnits(long dynamoDBWorkflowExecutionTasksTableReadCapacityUnits) {
        this.dynamoDBWorkflowExecutionTasksTableReadCapacityUnits = dynamoDBWorkflowExecutionTasksTableReadCapacityUnits;
    }

    public void setDynamoDBWorkflowExecutionTasksTableWriteCapacityUnits(long dynamoDBWorkflowExecutionTasksTableWriteCapacityUnits) {
        this.dynamoDBWorkflowExecutionTasksTableWriteCapacityUnits = dynamoDBWorkflowExecutionTasksTableWriteCapacityUnits;
    }

    public long getDynamoDBCrawlMetadataTableReadCapacityUnits() {
        return dynamoDBCrawlMetadataTableReadCapacityUnits;
    }

    public long getDynamoDBCrawlMetadataTableWriteCapacityUnits() {
        return dynamoDBCrawlMetadataTableWriteCapacityUnits;
    }

    public void setDynamoDBCrawlMetadataTableReadCapacityUnits(long dynamoDBCrawlMetadataTableReadCapacityUnits) {
        this.dynamoDBCrawlMetadataTableReadCapacityUnits = dynamoDBCrawlMetadataTableReadCapacityUnits;
    }

    public void setDynamoDBCrawlMetadataTableWriteCapacityUnits(long dynamoDBCrawlMetadataTableWriteCapacityUnits) {
        this.dynamoDBCrawlMetadataTableWriteCapacityUnits = dynamoDBCrawlMetadataTableWriteCapacityUnits;
    }

    public void setLambdaConfigFilePath(String lambdaConfigFilePath) {
        this.lambdaConfigFilePath = lambdaConfigFilePath;
    }

    public String getLambdaConfigFilePath() {
        return lambdaConfigFilePath;
    }

    public long getDynamoDBWorkflowExecutionsTableWorkflowIdGSIReadCapacityUnits() {
        return dynamoDBWorkflowExecutionsTableWorkflowIdGSIReadCapacityUnits;
    }

    public void setDynamoDBWorkflowExecutionsTableWorkflowIdGSIReadCapacityUnits(long dynamoDBWorkflowExecutionsTableWorkflowIdGSIReadCapacityUnits) {
        this.dynamoDBWorkflowExecutionsTableWorkflowIdGSIReadCapacityUnits = dynamoDBWorkflowExecutionsTableWorkflowIdGSIReadCapacityUnits;
    }

    public void setDynamoDBWorkflowExecutionsTableWorkflowIdGSIWriteCapacityUnits(long dynamoDBWorkflowExecutionsTableWorkflowIdGSIWriteCapacityUnits) {
        this.dynamoDBWorkflowExecutionsTableWorkflowIdGSIWriteCapacityUnits = dynamoDBWorkflowExecutionsTableWorkflowIdGSIWriteCapacityUnits;
    }

    public long getDynamoDBWorkflowExecutionsTableWorkflowIdGSIWriteCapacityUnits() {

        return dynamoDBWorkflowExecutionsTableWorkflowIdGSIWriteCapacityUnits;
    }

    public long getDynamoDBWorkflowExecutionTasksTableStatusDepthGSIReadCapacityUnits() {
        return dynamoDBWorkflowExecutionTasksTableStatusDepthGSIReadCapacityUnits;
    }

    public long getDynamoDBWorkflowExecutionTasksTableStatusDepthGSIWriteCapacityUnits() {
        return dynamoDBWorkflowExecutionTasksTableStatusDepthGSIWriteCapacityUnits;
    }

    public void setDynamoDBWorkflowExecutionTasksTableStatusDepthGSIReadCapacityUnits(long dynamoDBWorkflowExecutionTasksTableStatusDepthGSIReadCapacityUnits) {
        this.dynamoDBWorkflowExecutionTasksTableStatusDepthGSIReadCapacityUnits = dynamoDBWorkflowExecutionTasksTableStatusDepthGSIReadCapacityUnits;
    }

    public void setDynamoDBWorkflowExecutionTasksTableStatusDepthGSIWriteCapacityUnits(long dynamoDBWorkflowExecutionTasksTableStatusDepthGSIWriteCapacityUnits) {
        this.dynamoDBWorkflowExecutionTasksTableStatusDepthGSIWriteCapacityUnits = dynamoDBWorkflowExecutionTasksTableStatusDepthGSIWriteCapacityUnits;
    }
}

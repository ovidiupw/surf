package surf.deployment;

import com.amazonaws.services.apigateway.model.CreateAuthorizerResult;
import com.amazonaws.services.apigateway.model.Resource;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.google.common.base.Preconditions;
import surf.deployers.lambda.LambdaFunctionsData;

import javax.annotation.Nonnull;
import java.util.List;

public class Context {
    private IAMRoles IAMRoles;
    private LambdaFunctionsData lambdaFunctionsData;
    private List<Resource> apiResources;
    private String apiKey;
    private TableDescription workflowsDynamoDBTable;
    private TableDescription workflowExecutionsDynamoDBTable;
    private TableDescription workflowExecutionTasksDynamoDBTable;
    private TableDescription crawlMetadataDynamoDBTable;
    private String initializeCrawlSessionSNSTopicArn;
    private CreateAuthorizerResult apiAuthorizer;
    private String s3AppConfigBucketName;
    private String s3LambdaCodeKey;

    public void setIAMRoles(@Nonnull final IAMRoles IAMRoles) {
        Preconditions.checkNotNull(IAMRoles);
        if (this.IAMRoles != null) {
            throw new UnsupportedOperationException("IAMRoles was already set in this context!");
        }
        this.IAMRoles = IAMRoles;
    }

    public void setLambdaFunctionsData(@Nonnull final LambdaFunctionsData lambdaFunctionsData) {
        Preconditions.checkNotNull(lambdaFunctionsData);
        if (this.lambdaFunctionsData != null) {
            throw new UnsupportedOperationException("Lambda ARNs was already set in this context!");
        }
        this.lambdaFunctionsData = lambdaFunctionsData;
    }

    public void setApiResources(@Nonnull final List<Resource> apiResources) {
        Preconditions.checkNotNull(apiResources);
        if (this.apiResources != null) {
            throw new UnsupportedOperationException("Api resources was already set in this context!");
        }
        this.apiResources = apiResources;
    }

    public void setApiKey(@Nonnull final String apiKey) {
        Preconditions.checkNotNull(apiResources);
        if (this.apiKey != null) {
            throw new UnsupportedOperationException("Api key was already set in this context!");
        }
        this.apiKey = apiKey;
    }

    public void setWorkflowsDynamoDBTable(@Nonnull final TableDescription tableDescription) {
        Preconditions.checkNotNull(tableDescription);
        if (this.workflowsDynamoDBTable != null) {
            throw new UnsupportedOperationException(
                    "Workflows DynamoDB table key was already set in this context!");
        }
        this.workflowsDynamoDBTable = tableDescription;
    }


    public void setWorkflowExecutionsDynamoDBTable(@Nonnull final TableDescription tableDescription) {
        Preconditions.checkNotNull(tableDescription);
        if (this.workflowExecutionsDynamoDBTable != null) {
            throw new UnsupportedOperationException(
                    "WorkflowExecutions DynamoDB table key was already set in this context!");
        }
        this.workflowExecutionsDynamoDBTable = tableDescription;
    }

    public void setWorkflowExecutionTasksDynamoDBTable(@Nonnull final TableDescription tableDescription) {
        Preconditions.checkNotNull(tableDescription);
        if (this.workflowExecutionTasksDynamoDBTable != null) {
            throw new UnsupportedOperationException(
                    "WorkflowExecutionTasks DynamoDB table key was already set in this context!");
        }
        this.workflowExecutionTasksDynamoDBTable = tableDescription;
    }

    public void setCrawlMetadataDynamoDBTable(@Nonnull final TableDescription tableDescription) {
        Preconditions.checkNotNull(tableDescription);
        if (this.crawlMetadataDynamoDBTable != null) {
            throw new UnsupportedOperationException(
                    "CrawlMetadata DynamoDB table key was already set in this context!");
        }
        this.crawlMetadataDynamoDBTable = tableDescription;
    }

    public void setInitializeCrawlSessionSNSTopicArn(@Nonnull final String arn) {
        Preconditions.checkNotNull(arn);
        if (this.initializeCrawlSessionSNSTopicArn != null) {
            throw new UnsupportedOperationException(
                    "SNS InitializeCrawlSession Topic ARN was already set in this context!");
        }
        this.initializeCrawlSessionSNSTopicArn = arn;
    }

    public void setApiAuthorizer(@Nonnull final CreateAuthorizerResult apiAuthorizer) {
        Preconditions.checkNotNull(apiAuthorizer);
        if (this.apiAuthorizer != null) {
            throw new UnsupportedOperationException(
                    "API Authorizer was already set in this context!");
        }
        this.apiAuthorizer = apiAuthorizer;
    }

    public void setS3AppConfigBucketName(@Nonnull final String bucketName) {
        Preconditions.checkNotNull(bucketName);
        if (this.s3AppConfigBucketName != null) {
            throw new UnsupportedOperationException(
                    "S3 app config bucket name was already set in this context!");
        }
        this.s3AppConfigBucketName = bucketName;
    }

    public void setS3LambdaCodeKey(@Nonnull final String objectKey) {
        Preconditions.checkNotNull(objectKey);
        if (this.s3LambdaCodeKey != null) {
            throw new UnsupportedOperationException(
                    "S3 lambda code key was already set in this context!");
        }
        this.s3LambdaCodeKey = objectKey;
    }

    public IAMRoles getIAMRoles() {
        return IAMRoles;
    }

    public LambdaFunctionsData getLambdaFunctionsData() {
        return lambdaFunctionsData;
    }

    public List<Resource> getApiResources() {
        return apiResources;
    }

    public String getApiKey() {
        return apiKey;
    }

    public TableDescription getWorkflowsDynamoDBTable() {
        return workflowsDynamoDBTable;
    }

    public TableDescription getWorkflowExecutionsDynamoDBTable() {
        return workflowExecutionsDynamoDBTable;
    }

    public TableDescription getWorkflowExecutionTasksDynamoDBTable() {
        return workflowExecutionTasksDynamoDBTable;
    }

    public TableDescription getCrawlMetadataDynamoDBTable() {
        return crawlMetadataDynamoDBTable;
    }

    public String getInitializeCrawlSessionSNSTopicArn() {
        return initializeCrawlSessionSNSTopicArn;
    }

    public CreateAuthorizerResult getApiAuthorizer() {
        return apiAuthorizer;
    }

    public String getS3AppConfigBucketName() {
        return s3AppConfigBucketName;
    }

    public String getS3LambdaCodeKey() {
        return s3LambdaCodeKey;
    }

    @Override
    public String toString() {
        return "Context{" +
                "IAMRoles=" + IAMRoles +
                ", lambdaFunctionsData=" + lambdaFunctionsData +
                ", apiResources=" + apiResources +
                ", apiKey='" + apiKey + '\'' +
                ", workflowsDynamoDBTable=" + workflowsDynamoDBTable +
                ", workflowExecutionsDynamoDBTable=" + workflowExecutionsDynamoDBTable +
                ", workflowExecutionTasksDynamoDBTable=" + workflowExecutionTasksDynamoDBTable +
                ", crawlMetadataDynamoDBTable=" + crawlMetadataDynamoDBTable +
                ", initializeCrawlSessionSNSTopicArn='" + initializeCrawlSessionSNSTopicArn + '\'' +
                ", apiAuthorizer=" + apiAuthorizer +
                ", s3AppConfigBucketName='" + s3AppConfigBucketName + '\'' +
                ", s3LambdaCodeKey='" + s3LambdaCodeKey + '\'' +
                '}';
    }

}

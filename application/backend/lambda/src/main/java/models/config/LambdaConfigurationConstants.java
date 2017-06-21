package models.config;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import java.util.Objects;

public class LambdaConfigurationConstants {

    private String initializeCrawlSessionSNSTopicArn;
    private String awsClientRegion;
    private int awsClientExecutionTimeoutSeconds;
    private String stepFunctionsInvokeLambdaRoleArn;
    private String crawlWebPageLambdaArn;
    private String finalizeCrawlSessionLambdaArn;
    private String applicationS3BucketName;

    public String getInitializeCrawlSessionSNSTopicArn() {
        return initializeCrawlSessionSNSTopicArn;
    }

    public String getAwsClientRegion() {
        return awsClientRegion;
    }

    public int getAwsClientExecutionTimeoutSeconds() {
        return awsClientExecutionTimeoutSeconds;
    }

    public String getStepFunctionsInvokeLambdaRoleArn() {
        return stepFunctionsInvokeLambdaRoleArn;
    }

    public String getCrawlWebPageLambdaArn() {
        return crawlWebPageLambdaArn;
    }

    public String getFinalizeCrawlSessionLambdaArn() {
        return finalizeCrawlSessionLambdaArn;
    }

    public String getApplicationS3BucketName() {
        return applicationS3BucketName;
    }

    public static class Builder {
        private String initializeCrawlSessionSNSTopicArn;
        private String awsClientRegion;
        private int awsClientExecutionTimeoutSeconds;
        private String stepFunctionsInvokeLambdaRoleArn;
        private String crawlWebPageLambdaArn;
        private String finalizeCrawlSessionLambdaArn;
        private String applicationS3BucketName;

        public LambdaConfigurationConstants build() {
            Preconditions.checkNotNull(initializeCrawlSessionSNSTopicArn);
            Preconditions.checkNotNull(awsClientRegion);
            Preconditions.checkNotNull(awsClientExecutionTimeoutSeconds);
            Preconditions.checkNotNull(stepFunctionsInvokeLambdaRoleArn);
            Preconditions.checkNotNull(crawlWebPageLambdaArn);
            Preconditions.checkNotNull(finalizeCrawlSessionLambdaArn);
            Preconditions.checkNotNull(applicationS3BucketName);

            final LambdaConfigurationConstants clientConstants = new LambdaConfigurationConstants();
            clientConstants.setInitializeCrawlSessionSNSTopicArn(initializeCrawlSessionSNSTopicArn);
            clientConstants.setAwsClientRegion(awsClientRegion);
            clientConstants.setAwsClientExecutionTimeoutSeconds(awsClientExecutionTimeoutSeconds);
            clientConstants.setStepFunctionsInvokeLambdaRoleArn(stepFunctionsInvokeLambdaRoleArn);
            clientConstants.setCrawlWebPageLambdaArn(crawlWebPageLambdaArn);
            clientConstants.setFinalizeCrawlSessionLambdaArn(finalizeCrawlSessionLambdaArn);
            clientConstants.setApplicationS3BucketName(applicationS3BucketName);

            return clientConstants;
        }

        public Builder withInitializeCrawlSessionSNSTopicArn(@Nonnull final String arn) {
            Preconditions.checkNotNull(arn);
            this.initializeCrawlSessionSNSTopicArn = arn;
            return this;
        }

        public Builder withAwsClientRegion(@Nonnull final String awsClientRegion) {
            Preconditions.checkNotNull(awsClientRegion);
            this.awsClientRegion = awsClientRegion;
            return this;
        }

        public Builder withAwsClientExecutionTimeoutSeconds(@Nonnull final int seconds) {
            Preconditions.checkArgument(seconds >= 0);
            this.awsClientExecutionTimeoutSeconds = seconds;
            return this;
        }

        public Builder withStepFunctionsInvokeLambdaRoleArn(@Nonnull final String arn) {
            Preconditions.checkNotNull(arn);
            this.stepFunctionsInvokeLambdaRoleArn = arn;
            return this;
        }

        public Builder withCrawlWebPageLambdaArn(@Nonnull final String arn) {
            Preconditions.checkNotNull(arn);
            this.crawlWebPageLambdaArn = arn;
            return this;
        }

        public Builder withFinalizeCrawlSessionLambdaArn(@Nonnull final String arn) {
            Preconditions.checkNotNull(arn);
            this.finalizeCrawlSessionLambdaArn = arn;
            return this;
        }

        public Builder withApplicationS3BucketName(@Nonnull final String name) {
            Preconditions.checkNotNull(name);
            this.applicationS3BucketName = name;
            return this;
        }
    }

    private void setInitializeCrawlSessionSNSTopicArn(@Nonnull final String arn) {
        this.initializeCrawlSessionSNSTopicArn = arn;
    }

    private void setAwsClientRegion(String awsClientRegion) {
        this.awsClientRegion = awsClientRegion;
    }

    private void setAwsClientExecutionTimeoutSeconds(int clientExecutionTimeout) {
        this.awsClientExecutionTimeoutSeconds = clientExecutionTimeout;
    }

    private void setStepFunctionsInvokeLambdaRoleArn(String stepFunctionsInvokeLambdaRoleArn) {
        this.stepFunctionsInvokeLambdaRoleArn = stepFunctionsInvokeLambdaRoleArn;
    }

    private void setCrawlWebPageLambdaArn(String crawlWebPageLambdaArn) {
        this.crawlWebPageLambdaArn = crawlWebPageLambdaArn;
    }

    private void setFinalizeCrawlSessionLambdaArn(String finalizeCrawlSessionLambdaArn) {
        this.finalizeCrawlSessionLambdaArn = finalizeCrawlSessionLambdaArn;
    }

    private void setApplicationS3BucketName(String applicationS3BucketName) {
        this.applicationS3BucketName = applicationS3BucketName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LambdaConfigurationConstants that = (LambdaConfigurationConstants) o;
        return awsClientExecutionTimeoutSeconds == that.awsClientExecutionTimeoutSeconds &&
                Objects.equals(initializeCrawlSessionSNSTopicArn, that.initializeCrawlSessionSNSTopicArn) &&
                Objects.equals(awsClientRegion, that.awsClientRegion) &&
                Objects.equals(stepFunctionsInvokeLambdaRoleArn, that.stepFunctionsInvokeLambdaRoleArn) &&
                Objects.equals(crawlWebPageLambdaArn, that.crawlWebPageLambdaArn) &&
                Objects.equals(finalizeCrawlSessionLambdaArn, that.finalizeCrawlSessionLambdaArn) &&
                Objects.equals(applicationS3BucketName, that.applicationS3BucketName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(initializeCrawlSessionSNSTopicArn, awsClientRegion, awsClientExecutionTimeoutSeconds, stepFunctionsInvokeLambdaRoleArn, crawlWebPageLambdaArn, finalizeCrawlSessionLambdaArn, applicationS3BucketName);
    }

    @Override
    public String toString() {
        return "LambdaConfigurationConstants{" +
                "initializeCrawlSessionSNSTopicArn='" + initializeCrawlSessionSNSTopicArn + '\'' +
                ", awsClientRegion='" + awsClientRegion + '\'' +
                ", awsClientExecutionTimeoutSeconds=" + awsClientExecutionTimeoutSeconds +
                ", stepFunctionsInvokeLambdaRoleArn='" + stepFunctionsInvokeLambdaRoleArn + '\'' +
                ", crawlWebPageLambdaArn='" + crawlWebPageLambdaArn + '\'' +
                ", finalizeCrawlSessionLambdaArn='" + finalizeCrawlSessionLambdaArn + '\'' +
                ", applicationS3BucketName='" + applicationS3BucketName + '\'' +
                '}';
    }
}

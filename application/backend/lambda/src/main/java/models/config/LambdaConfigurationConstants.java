package models.config;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

public class LambdaConfigurationConstants {

    private String initializeCrawlSessionSNSTopicArn;
    private String awsClientRegion;
    private int awsClientExecutionTimeoutSeconds;
    private String stepFunctionsInvokeLambdaRoleArn;
    private String crawlWebPageLambdaArn;
    private String finalizeCrawlSessionLambdaArn;

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

    public static class Builder {
        private String initializeCrawlSessionSNSTopicArn;
        private String awsClientRegion;
        private int awsClientExecutionTimeoutSeconds;
        private String stepFunctionsInvokeLambdaRoleArn;
        private String crawlWebPageLambdaArn;
        private String finalizeCrawlSessionLambdaArn;

        public LambdaConfigurationConstants build() {
            Preconditions.checkNotNull(initializeCrawlSessionSNSTopicArn);
            Preconditions.checkNotNull(awsClientRegion);
            Preconditions.checkNotNull(awsClientExecutionTimeoutSeconds);
            Preconditions.checkNotNull(stepFunctionsInvokeLambdaRoleArn);
            Preconditions.checkNotNull(crawlWebPageLambdaArn);
            Preconditions.checkNotNull(finalizeCrawlSessionLambdaArn);

            final LambdaConfigurationConstants clientConstants = new LambdaConfigurationConstants();
            clientConstants.setInitializeCrawlSessionSNSTopicArn(initializeCrawlSessionSNSTopicArn);
            clientConstants.setAwsClientRegion(awsClientRegion);
            clientConstants.setAwsClientExecutionTimeoutSeconds(awsClientExecutionTimeoutSeconds);
            clientConstants.setStepFunctionsInvokeLambdaRoleArn(stepFunctionsInvokeLambdaRoleArn);
            clientConstants.setCrawlWebPageLambdaArn(crawlWebPageLambdaArn);
            clientConstants.setFinalizeCrawlSessionLambdaArn(finalizeCrawlSessionLambdaArn);

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

    public void setCrawlWebPageLambdaArn(String crawlWebPageLambdaArn) {
        this.crawlWebPageLambdaArn = crawlWebPageLambdaArn;
    }

    public void setFinalizeCrawlSessionLambdaArn(String finalizeCrawlSessionLambdaArn) {
        this.finalizeCrawlSessionLambdaArn = finalizeCrawlSessionLambdaArn;
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
                '}';
    }
}

package models.config;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

public class LambdaConfigurationConstants {

    private String initializeCrawlSessionSNSTopicArn;
    private String awsClientRegion;
    private int awsClientExecutionTimeoutSeconds;

    public String getInitializeCrawlSessionSNSTopicArn() {
        return initializeCrawlSessionSNSTopicArn;
    }

    public String getAwsClientRegion() {
        return awsClientRegion;
    }

    public int getAwsClientExecutionTimeoutSeconds() {
        return awsClientExecutionTimeoutSeconds;
    }

    public static class Builder {
        private String initializeCrawlSessionSNSTopicArn;
        private String awsClientRegion;
        private int awsClientExecutionTimeoutSeconds;

        public LambdaConfigurationConstants build() {
            Preconditions.checkNotNull(initializeCrawlSessionSNSTopicArn);
            Preconditions.checkNotNull(awsClientRegion);
            Preconditions.checkNotNull(awsClientExecutionTimeoutSeconds);

            final LambdaConfigurationConstants clientConstants = new LambdaConfigurationConstants();
            clientConstants.setInitializeCrawlSessionSNSTopicArn(initializeCrawlSessionSNSTopicArn);
            clientConstants.setAwsClientRegion(awsClientRegion);
            clientConstants.setAwsClientExecutionTimeoutSeconds(awsClientExecutionTimeoutSeconds);

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

        public Builder withAwsClientExecutionTimeoutSeconds(final int seconds) {
            Preconditions.checkNotNull(awsClientExecutionTimeoutSeconds);
            this.awsClientExecutionTimeoutSeconds = awsClientExecutionTimeoutSeconds;
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

    @Override
    public String toString() {
        return "LambdaConfigurationConstants{" +
                "initializeCrawlSessionSNSTopicArn='" + initializeCrawlSessionSNSTopicArn + '\'' +
                ", awsClientRegion='" + awsClientRegion + '\'' +
                ", awsClientExecutionTimeoutSeconds=" + awsClientExecutionTimeoutSeconds +
                '}';
    }
}

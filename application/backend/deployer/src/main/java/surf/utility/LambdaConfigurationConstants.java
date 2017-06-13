package surf.utility;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

public class LambdaConfigurationConstants {


    private String initializeCrawlSessionSNSTopicArn;


    public String getInitializeCrawlSessionSNSTopicArn() {
        return initializeCrawlSessionSNSTopicArn;
    }

    public static class Builder {
        private String initializeCrawlSessionSNSTopicArn;

        public LambdaConfigurationConstants build() {
            Preconditions.checkNotNull(initializeCrawlSessionSNSTopicArn);

            LambdaConfigurationConstants clientConstants = new LambdaConfigurationConstants();
            clientConstants.setInitializeCrawlSessionSNSTopicArn(initializeCrawlSessionSNSTopicArn);

            return clientConstants;
        }

        public Builder withInitializeCrawlSessionSNSTopicArn(@Nonnull final String arn) {
            Preconditions.checkNotNull(arn);
            this.initializeCrawlSessionSNSTopicArn = arn;
            return this;
        }
    }

    private void setInitializeCrawlSessionSNSTopicArn(@Nonnull final String arn) {
        this.initializeCrawlSessionSNSTopicArn = arn;
    }
}

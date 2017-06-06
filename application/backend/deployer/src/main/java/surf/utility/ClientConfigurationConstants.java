package surf.utility;

import com.amazonaws.regions.Regions;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

public class ClientConfigurationConstants {


    private String awsClientRegion;
    private String facebookWebIdentityBasicRoleArn;
    private String apiKey;
    private String awsAccessKey;

    public String getAwsClientRegion() {
        return awsClientRegion;
    }

    public String getFacebookWebIdentityBasicRoleArn() {
        return facebookWebIdentityBasicRoleArn;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getAwsAccessKey() {
        return awsAccessKey;
    }

    public static class Builder {
        private Regions awsClientRegion;
        private String facebookWebIdentityBasicRoleArn;
        private String apiKey;
        private String awsAccessKey;

        public ClientConfigurationConstants build() {
            Preconditions.checkNotNull(awsClientRegion);
            Preconditions.checkNotNull(facebookWebIdentityBasicRoleArn);
            Preconditions.checkNotNull(apiKey);
            Preconditions.checkNotNull(awsAccessKey);

            ClientConfigurationConstants clientConstants = new ClientConfigurationConstants();
            clientConstants.setAwsClientRegion(awsClientRegion.getName());
            clientConstants.setFacebookWebIdentityBasicRoleArn(facebookWebIdentityBasicRoleArn);
            clientConstants.setApiKey(apiKey);
            clientConstants.setAwsAccessKey(awsAccessKey);

            return clientConstants;
        }

        public Builder withAWSRegion(@Nonnull final Regions awsClientRegion) {
            Preconditions.checkNotNull(awsClientRegion);
            this.awsClientRegion = awsClientRegion;
            return this;
        }

        public Builder withFacebookWebIdentityBasicRole(@Nonnull final String roleArn) {
            Preconditions.checkNotNull(roleArn);
            this.facebookWebIdentityBasicRoleArn = roleArn;
            return this;
        }

        public Builder withApiKey(@Nonnull final String apiKey) {
            Preconditions.checkNotNull(apiKey);
            this.apiKey = apiKey;
            return this;
        }

        public Builder withAWSAccessKey(@Nonnull final String awsAccessKey) {
            Preconditions.checkNotNull(awsAccessKey);
            this.awsAccessKey = awsAccessKey;
            return this;
        }
    }

    private void setAwsClientRegion(@Nonnull final String awsClientRegion) {
        this.awsClientRegion = awsClientRegion;
    }

    private void setFacebookWebIdentityBasicRoleArn(@Nonnull final String facebookWebIdentityBasicRoleArn) {
        this.facebookWebIdentityBasicRoleArn = facebookWebIdentityBasicRoleArn;
    }

    private void setApiKey(@Nonnull final String apiKey) {
        this.apiKey = apiKey;
    }

    private void setAwsAccessKey(String awsAccessKey) {
        this.awsAccessKey = awsAccessKey;
    }
}

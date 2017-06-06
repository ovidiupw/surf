package surf.deployment;

import com.amazonaws.services.identitymanagement.model.Role;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

public class IAMRoles {

    private Role helloWorldLambdaRole;
    private Role apiGatewayPushToCloudWatchLogsRole;
    private Role facebookWebIdentityBasicRole;

    public Role getHelloWorldLambdaRole() {
        return helloWorldLambdaRole;
    }

    public Role getApiGatewayPushToCloudWatchLogsRole() {
        return apiGatewayPushToCloudWatchLogsRole;
    }

    public Role getFacebookWebIdentityBasicRole() {
        return facebookWebIdentityBasicRole;
    }

    public static class Builder {

        private Role helloWorldLambdaRole;
        private Role apiGatewayPushToCloudWatchLogsRole;
        private Role facebookWebIdentityBasicRole;

        public IAMRoles build() {
            Preconditions.checkNotNull(helloWorldLambdaRole);
            Preconditions.checkNotNull(apiGatewayPushToCloudWatchLogsRole);
            Preconditions.checkNotNull(facebookWebIdentityBasicRole);

            final IAMRoles IAMRoles = new IAMRoles();
            IAMRoles.setHelloWorldLambdaRole(helloWorldLambdaRole);
            IAMRoles.setApiGatewayPushToCloudWatchLogsRole(apiGatewayPushToCloudWatchLogsRole);
            IAMRoles.setFacebookWebIdentityBasicRole(facebookWebIdentityBasicRole);
            return IAMRoles;
        }

        public Builder withHelloWorldLambdaRole(@Nonnull final Role roleArn) {
            Preconditions.checkNotNull(roleArn);
            this.helloWorldLambdaRole = roleArn;
            return this;
        }

        public Builder withApiGatewayPushToCloudWatchLogsRole(
                @Nonnull final Role apiGatewayPushToCloudWatchLogsRole) {
            Preconditions.checkNotNull(apiGatewayPushToCloudWatchLogsRole);
            this.apiGatewayPushToCloudWatchLogsRole = apiGatewayPushToCloudWatchLogsRole;
            return this;
        }

        public Builder withFacebookWebIdentityBasicRole(@Nonnull final Role role) {
            Preconditions.checkNotNull(role);
            this.facebookWebIdentityBasicRole = role;
            return this;
        }
    }

    private void setHelloWorldLambdaRole(@Nonnull final Role helloWorldLambdaRole) {
        this.helloWorldLambdaRole = helloWorldLambdaRole;
    }

    private void setApiGatewayPushToCloudWatchLogsRole(@Nonnull final Role apiGatewayPushToCloudWatchLogsRole) {
        this.apiGatewayPushToCloudWatchLogsRole = apiGatewayPushToCloudWatchLogsRole;
    }

    public void setFacebookWebIdentityBasicRole(@Nonnull final Role facebookWebIdentityBasicRole) {
        this.facebookWebIdentityBasicRole = facebookWebIdentityBasicRole;
    }

    @Override
    public String toString() {
        return "IAMRoles{" +
                "helloWorldLambdaRole=" + helloWorldLambdaRole +
                ", apiGatewayPushToCloudWatchLogsRole=" + apiGatewayPushToCloudWatchLogsRole +
                ", facebookWebIdentityBasicRole=" + facebookWebIdentityBasicRole +
                '}';
    }
}

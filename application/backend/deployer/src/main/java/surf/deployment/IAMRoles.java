package surf.deployment;

import com.amazonaws.services.identitymanagement.model.Role;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

public class IAMRoles {

    private Role helloWorldLambdaRole;
    private Role apiGatewayPushToCloudWatchLogsRole;

    public Role getHelloWorldLambdaRole() {
        return helloWorldLambdaRole;
    }

    public Role getApiGatewayPushToCloudWatchLogsRole() {
        return apiGatewayPushToCloudWatchLogsRole;
    }

    public static class Builder {

        private Role helloWorldLambdaRole;
        private Role apiGatewayPushToCloudWatchLogsRole;

        public IAMRoles build() {
            Preconditions.checkNotNull(helloWorldLambdaRole);
            Preconditions.checkNotNull(apiGatewayPushToCloudWatchLogsRole);

            final IAMRoles IAMRoles = new IAMRoles();
            IAMRoles.setHelloWorldLambdaRole(helloWorldLambdaRole);
            IAMRoles.setApiGatewayPushToCloudWatchLogsRole(apiGatewayPushToCloudWatchLogsRole);
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
    }

    private void setHelloWorldLambdaRole(final Role helloWorldLambdaRole) {
        this.helloWorldLambdaRole = helloWorldLambdaRole;
    }

    private void setApiGatewayPushToCloudWatchLogsRole(final Role apiGatewayPushToCloudWatchLogsRole) {
        this.apiGatewayPushToCloudWatchLogsRole = apiGatewayPushToCloudWatchLogsRole;
    }

    @Override
    public String toString() {
        return "IAMRoles{" +
                "helloWorldLambdaRole=" + helloWorldLambdaRole +
                ", apiGatewayPushToCloudWatchLogsRole=" + apiGatewayPushToCloudWatchLogsRole +
                '}';
    }
}

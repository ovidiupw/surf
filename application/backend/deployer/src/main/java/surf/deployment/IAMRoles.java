package surf.deployment;

import com.amazonaws.services.identitymanagement.model.Role;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

public class IAMRoles {

    private Role listCoreWorkersLambdaRole;
    private Role apiGatewayPushToCloudWatchLogsRole;
    private Role facebookWebIdentityBasicRole;
    private Role sfnInvokeLambdaRole;
    private Role listWorkflowsLambdaRole;
    private Role startWorkflowLambdaRole;
    private Role getWorkflowLambdaRole;

    public Role getListCoreWorkersLambdaRole() {
        return listCoreWorkersLambdaRole;
    }

    public Role getApiGatewayPushToCloudWatchLogsRole() {
        return apiGatewayPushToCloudWatchLogsRole;
    }

    public Role getFacebookWebIdentityBasicRole() {
        return facebookWebIdentityBasicRole;
    }

    public Role getSfnInvokeLambdaRole() {
        return sfnInvokeLambdaRole;
    }

    public Role getListWorkflowsLambdaRole() {
        return listWorkflowsLambdaRole;
    }

    public Role getStartWorkflowLambdaRole() {
        return startWorkflowLambdaRole;
    }

    public Role getGetWorkflowLambdaRole() {
        return getWorkflowLambdaRole;
    }

    public static class Builder {

        private Role apiGatewayPushToCloudWatchLogsRole;
        private Role facebookWebIdentityBasicRole;
        private Role sfnInvokeLambdaRole;
        private Role listCoreWorkersLambdaRole;
        private Role listWorkflowsLambdaRole;
        private Role startWorkflowLambdaRole;
        private Role getWorkflowLambdaRole;

        public IAMRoles build() {
            Preconditions.checkNotNull(apiGatewayPushToCloudWatchLogsRole);
            Preconditions.checkNotNull(facebookWebIdentityBasicRole);
            Preconditions.checkNotNull(sfnInvokeLambdaRole);
            Preconditions.checkNotNull(listCoreWorkersLambdaRole);
            Preconditions.checkNotNull(listWorkflowsLambdaRole);
            Preconditions.checkNotNull(startWorkflowLambdaRole);
            Preconditions.checkNotNull(getWorkflowLambdaRole);

            final IAMRoles IAMRoles = new IAMRoles();
            IAMRoles.setApiGatewayPushToCloudWatchLogsRole(apiGatewayPushToCloudWatchLogsRole);
            IAMRoles.setFacebookWebIdentityBasicRole(facebookWebIdentityBasicRole);
            IAMRoles.setSfnInvokeLambdaRole(sfnInvokeLambdaRole);
            IAMRoles.setListCoreWorkersLambdaRole(listCoreWorkersLambdaRole);
            IAMRoles.setListWorkflowsLambdaRole(listWorkflowsLambdaRole);
            IAMRoles.setStartWorkflowLambdaRole(startWorkflowLambdaRole);
            IAMRoles.setGetWorkflowLambdaRole(getWorkflowLambdaRole);
            return IAMRoles;
        }

        public Builder withApiGatewayPushToCloudWatchLogsRole(@Nonnull final Role role) {
            Preconditions.checkNotNull(role);
            this.apiGatewayPushToCloudWatchLogsRole = role;
            return this;
        }

        public Builder withFacebookWebIdentityBasicRole(@Nonnull final Role role) {
            Preconditions.checkNotNull(role);
            this.facebookWebIdentityBasicRole = role;
            return this;
        }

        public Builder withSfnInvokeLambdaRole(@Nonnull final Role role) {
            Preconditions.checkNotNull(role);
            this.sfnInvokeLambdaRole = role;
            return this;
        }

        public Builder withListCoreWorkersLambdaRole(@Nonnull final Role role) {
            Preconditions.checkNotNull(role);
            this.listCoreWorkersLambdaRole = role;
            return this;
        }

        public Builder withListWorkflowsLambdaRole(@Nonnull final Role role) {
            Preconditions.checkNotNull(role);
            this.listWorkflowsLambdaRole = role;
            return this;
        }

        public Builder withStartWorkflowLambdaRole(@Nonnull final Role role) {
            Preconditions.checkNotNull(role);
            this.startWorkflowLambdaRole = role;
            return this;
        }

        public Builder withGetWorkflowLambdaRole(@Nonnull final Role role) {
            Preconditions.checkNotNull(role);
            this.getWorkflowLambdaRole = role;
            return this;
        }
    }

    private void setApiGatewayPushToCloudWatchLogsRole(@Nonnull final Role role) {
        this.apiGatewayPushToCloudWatchLogsRole = role;
    }

    private void setFacebookWebIdentityBasicRole(@Nonnull final Role role) {
        this.facebookWebIdentityBasicRole = role;
    }

    private void setSfnInvokeLambdaRole(@Nonnull final Role role) {
        this.sfnInvokeLambdaRole = role;
    }

    private void setListCoreWorkersLambdaRole(@Nonnull final Role role) {
        this.listCoreWorkersLambdaRole = role;
    }

    private void setListWorkflowsLambdaRole(@Nonnull final Role role) {
        this.listWorkflowsLambdaRole = role;
    }

    private void setStartWorkflowLambdaRole(@Nonnull final Role role) {
        this.startWorkflowLambdaRole = role;
    }

    private void setGetWorkflowLambdaRole(@Nonnull final Role role) {
        this.getWorkflowLambdaRole = role;
    }


    @Override
    public String toString() {
        return "IAMRoles{" +
                "listCoreWorkersLambdaRole=" + listCoreWorkersLambdaRole +
                ", apiGatewayPushToCloudWatchLogsRole=" + apiGatewayPushToCloudWatchLogsRole +
                ", facebookWebIdentityBasicRole=" + facebookWebIdentityBasicRole +
                ", sfnInvokeLambdaRole=" + sfnInvokeLambdaRole +
                ", listWorkflowsLambdaRole=" + listWorkflowsLambdaRole +
                ", startWorkflowLambdaRole=" + startWorkflowLambdaRole +
                ", getWorkflowLambdaRole=" + getWorkflowLambdaRole +
                '}';
    }
}

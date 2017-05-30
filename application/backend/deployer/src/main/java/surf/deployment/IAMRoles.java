package surf.deployment;

import com.amazonaws.services.identitymanagement.model.Role;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

public class IAMRoles {

    private Role helloWorldLambdaRole;

    public Role getHelloWorldLambdaRole() {
        return helloWorldLambdaRole;
    }

    public static class Builder {

        private Role helloWorldLambdaRole;

        public IAMRoles build() {
            Preconditions.checkNotNull(helloWorldLambdaRole);

            final IAMRoles IAMRoles = new IAMRoles();
            IAMRoles.setHelloWorldLambdaRole(helloWorldLambdaRole);
            return IAMRoles;
        }

        public Builder withHelloWorldLambdaRole(@Nonnull final Role roleArn) {
            Preconditions.checkNotNull(roleArn);
            this.helloWorldLambdaRole = roleArn;
            return this;
        }
    }

    private void setHelloWorldLambdaRole(final Role helloWorldLambdaRole) {
        this.helloWorldLambdaRole = helloWorldLambdaRole;
    }

    @Override
    public String toString() {
        return "IAMRoles{" +
                "helloWorldLambdaRole=" + helloWorldLambdaRole +
                '}';
    }
}

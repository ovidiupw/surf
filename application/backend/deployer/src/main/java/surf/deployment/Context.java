package surf.deployment;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

public class Context {
    private IAMRoles IAMRoles;
    private LambdaArns lambdaArns;

    public void setIAMRoles(@Nonnull final IAMRoles IAMRoles) {
        Preconditions.checkNotNull(IAMRoles);
        if (this.IAMRoles != null) {
            throw new UnsupportedOperationException("IAMRoles was already set in this context!");
        }
        this.IAMRoles = IAMRoles;
    }

    public void setLambdaArns(@Nonnull final LambdaArns lambdaArns) {
        Preconditions.checkNotNull(lambdaArns);
        if (this.lambdaArns != null) {
            throw new UnsupportedOperationException("Lambda ARNs was already set in this context!");
        }
        this.lambdaArns = lambdaArns;
    }

    public IAMRoles getIAMRoles() {
        return IAMRoles;
    }

    public LambdaArns getLambdaArns() {
        return lambdaArns;
    }

    @Override
    public String toString() {
        return "Context{" +
                "IAMRoles=" + IAMRoles +
                ", lambdaArns=" + lambdaArns +
                '}';
    }
}

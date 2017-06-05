package surf.deployment;

import com.amazonaws.services.apigateway.model.Resource;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import java.util.List;

public class Context {
    private IAMRoles IAMRoles;
    private LambdaFunctionsData lambdaFunctionsData;
    private List<Resource> apiResources;
    private String apiKey;

    public void setIAMRoles(@Nonnull final IAMRoles IAMRoles) {
        Preconditions.checkNotNull(IAMRoles);
        if (this.IAMRoles != null) {
            throw new UnsupportedOperationException("IAMRoles was already set in this context!");
        }
        this.IAMRoles = IAMRoles;
    }

    public void setLambdaFunctionsData(@Nonnull final LambdaFunctionsData lambdaFunctionsData) {
        Preconditions.checkNotNull(lambdaFunctionsData);
        if (this.lambdaFunctionsData != null) {
            throw new UnsupportedOperationException("Lambda ARNs was already set in this context!");
        }
        this.lambdaFunctionsData = lambdaFunctionsData;
    }

    public void setApiResources(@Nonnull final List<Resource> apiResources) {
        Preconditions.checkNotNull(apiResources);
        if (this.apiResources != null) {
            throw new UnsupportedOperationException("Api resources was already set in this context!");
        }
        this.apiResources = apiResources;
    }

    public void setApiKey(@Nonnull final String apiKey) {
        Preconditions.checkNotNull(apiResources);
        if (this.apiKey != null) {
            throw new UnsupportedOperationException("Api key was already set in this context!");
        }
        this.apiKey = apiKey;
    }

    public IAMRoles getIAMRoles() {
        return IAMRoles;
    }

    public LambdaFunctionsData getLambdaFunctionsData() {
        return lambdaFunctionsData;
    }

    public List<Resource> getApiResources() {
        return apiResources;
    }

    public String getApiKey() {
        return apiKey;
    }

    @Override
    public String toString() {
        return "Context{" +
                "IAMRoles=" + IAMRoles +
                ", lambdaFunctionsData=" + lambdaFunctionsData +
                ", apiResources=" + apiResources +
                ", apiKey='" + apiKey + '\'' +
                '}';
    }
}

package surf.deployers;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.model.FunctionCode;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

public class DeployerConfiguration {
    private ClientConfiguration clientConfiguration;
    private Regions region;
    private FunctionCode lambdaFunctionCode;
    private String lambdaRuntime;

    public ClientConfiguration getClientConfiguration() {
        return clientConfiguration;
    }

    public Regions getRegion() {
        return region;
    }

    public FunctionCode getLambdaFunctionCode() {
        return lambdaFunctionCode;
    }

    public String getLambdaRuntime() {
        return lambdaRuntime;
    }

    public static class Builder {
        private ClientConfiguration clientConfiguration;
        private Regions region;
        private FunctionCode lambdaFunctionCode;
        private String lambdaRuntime;

        public DeployerConfiguration build() {
            Preconditions.checkNotNull(clientConfiguration);
            Preconditions.checkNotNull(region);
            Preconditions.checkNotNull(lambdaFunctionCode);
            Preconditions.checkNotNull(lambdaRuntime);

            final DeployerConfiguration deployerConfiguration = new DeployerConfiguration();
            deployerConfiguration.setClientConfiguration(clientConfiguration);
            deployerConfiguration.setLambdaFunctionCode(lambdaFunctionCode);
            deployerConfiguration.setRegion(region);
            deployerConfiguration.setLambdaRuntime(lambdaRuntime);
            return deployerConfiguration;
        }

        public DeployerConfiguration.Builder withClientConfiguration(@Nonnull final ClientConfiguration clientConfig) {
            Preconditions.checkNotNull(clientConfig);
            this.clientConfiguration = clientConfig;
            return this;
        }

        public DeployerConfiguration.Builder withRegion(@Nonnull final Regions region) {
            Preconditions.checkNotNull(region);
            this.region = region;
            return this;
        }

        public DeployerConfiguration.Builder withLambdaFunctionCode(@Nonnull final FunctionCode lambdaFunctionCode) {
            Preconditions.checkNotNull(lambdaFunctionCode);
            this.lambdaFunctionCode = lambdaFunctionCode;
            return this;
        }

        public DeployerConfiguration.Builder withLambdaRuntime(@Nonnull final String lambdaRuntime) {
            Preconditions.checkNotNull(lambdaRuntime);
            this.lambdaRuntime = lambdaRuntime;
            return this;
        }
    }

    private void setClientConfiguration(ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
    }

    private void setRegion(Regions region) {
        this.region = region;
    }

    private void setLambdaFunctionCode(FunctionCode lambdaFunctionCode) {
        this.lambdaFunctionCode = lambdaFunctionCode;
    }

    public void setLambdaRuntime(String lambdaRuntime) {
        this.lambdaRuntime = lambdaRuntime;
    }
}

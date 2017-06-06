package surf.deployers;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;

import javax.annotation.Nonnull;

public class DeployerConfigurationModule extends AbstractModule {
    private final DeployerConfiguration deployerConfiguration;

    public DeployerConfigurationModule(@Nonnull final DeployerConfiguration deployerConfiguration) {
        Preconditions.checkNotNull(deployerConfiguration);
        this.deployerConfiguration = deployerConfiguration;
    }

    @Override
    protected void configure() {
        bind(DeployerConfiguration.class).toInstance(deployerConfiguration);

    }

}

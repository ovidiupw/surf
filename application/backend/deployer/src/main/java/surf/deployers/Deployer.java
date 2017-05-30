package surf.deployers;

import surf.deployment.Context;

import javax.annotation.Nonnull;

public interface Deployer {
    String getName();
    Context deploy(@Nonnull final Context context);
}

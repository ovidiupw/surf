package surf.deployers;

import surf.deployment.Context;
import surf.exceptions.OperationFailedException;

import javax.annotation.Nonnull;

public interface Deployer {
    String getName();

    Context deploy(@Nonnull final Context context) throws OperationFailedException;
}

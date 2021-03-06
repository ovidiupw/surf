package surf.deployment;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.deployers.Deployer;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

public class Deployment {
    private static final Logger LOG = LoggerFactory.getLogger(Deployment.class);
    private final String name;

    private List<Deployer> deployers = new LinkedList<>();
    private Context context = new Context();

    public Deployment(@Nonnull final String name) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
        this.name = name;
    }

    public Deployment chainDeployer(@Nonnull final Deployer deployer) {
        Preconditions.checkNotNull(deployer);
        deployers.add(deployer);
        return this;
    }

    public void start() {
        LOG.info("Starting deployment with name={}...", name);
        LOG.info("Initialized deployment context to '{}'", context);

        for (final Deployer deployer : deployers) {
            LOG.info("Executing deployer '{}'", deployer.getName());

            /* Deploy using the deployer and then get the updated context */
            context = deployer.deploy(context);
            LOG.info("The updated context after deployment is '{}'", context);

            LOG.info("Finished executing deployer '{}'", deployer.getName());
        }
        LOG.info("Finished deployment with name={}!", name);
    }

    public Context getContext() {
        return context;
    }
}

package surf.deployers.sleep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.deployers.Deployer;
import surf.deployment.Context;
import surf.exceptions.OperationFailedException;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

public final class SleepDeployer implements Deployer {

    private static final Logger LOG = LoggerFactory.getLogger(SleepDeployer.class);
    private static final String DEPLOYER_NAME = "SleepDeployer";

    private final int numberOfTimeUnitsToSleep;
    private final TimeUnit timeUnit;

    public SleepDeployer(int numberOfTimeUnitsToSleep, TimeUnit timeUnit) {
        this.numberOfTimeUnitsToSleep = numberOfTimeUnitsToSleep;
        this.timeUnit = timeUnit;
    }

    @Override
    public String getName() {
        return DEPLOYER_NAME;
    }

    @Override
    public Context deploy(@Nonnull Context context) throws OperationFailedException {
        try {
            LOG.info("Sleeping {} {}...", numberOfTimeUnitsToSleep, timeUnit.toString());
            Thread.sleep(timeUnit.toMillis(numberOfTimeUnitsToSleep));
        } catch (InterruptedException e) {
            LOG.error("Exception while sleeping!", e);
            throw new OperationFailedException(e);
        }

        return context;
    }
}

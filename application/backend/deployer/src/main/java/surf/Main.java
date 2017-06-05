package surf;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.deployers.Deployer;
import surf.deployers.DeployerConfigurationModule;
import surf.deployers.api.ApiDeployer;
import surf.deployers.iam.IAMDeployer;
import surf.deployers.lambda.LambdaDeployer;
import surf.deployment.Deployment;
import surf.exceptions.OperationFailedException;
import surf.utility.ExitCode;

import java.util.concurrent.TimeUnit;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    public static final String DEPLOYER_CONFIG_FILE_PATH = "src/main/resources/deployer/config.json";

    public static void main(String[] args) {
        LOG.info("Starting 'Surf' backend deployer...");
        doMain();
        LOG.info("'Surf' backend deployer successfully completed its execution.");
        System.exit(ExitCode.Success.getCode());
    }

    private static void doMain() {
        try {
            final Deployment deployment = buildDeployment();
            deployment.start();
        } catch (OperationFailedException ignored) {
            System.exit(ExitCode.Error.getCode());
        }
    }

    private static Deployment buildDeployment() {
        final Deployment deployment = new Deployment();
        final Injector injector = Guice.createInjector(new DeployerConfigurationModule(DEPLOYER_CONFIG_FILE_PATH));

        final Deployer iamDeployer = injector.getInstance(IAMDeployer.class);
        final Deployer lambdaDeployer = injector.getInstance(LambdaDeployer.class);
        final Deployer apiDeployer = injector.getInstance(ApiDeployer.class);
        // TODO the sleep deployer should really be set to something between 15 and 30 seconds
        final Deployer sleepDeployer = new SleepDeployer(1, TimeUnit.SECONDS);

        /* The order of chaining the deployers matters. */
        deployment
                .chainDeployer(iamDeployer)
                .chainDeployer(sleepDeployer) // ensure IAM permissions consistency in AWS
                .chainDeployer(lambdaDeployer)
                .chainDeployer(apiDeployer);

        return deployment;
    }
}
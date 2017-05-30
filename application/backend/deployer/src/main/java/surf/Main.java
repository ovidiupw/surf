package surf;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.deployers.Deployer;
import surf.deployers.DeployerConfigurationModule;
import surf.deployers.iam.IAMDeployer;
import surf.deployers.lambda.LambdaDeployer;
import surf.deployment.Deployment;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOG.info("Starting 'Surf' backend deployer...");
        doMain();
        LOG.info("'Surf' backend deployer successfully completed its execution.");
        System.exit(ExitCode.Success.getCode());
    }

    private static void doMain() {
        final Deployment deployment = buildDeployment();
        deployment.start();
    }

    private static Deployment buildDeployment() {
        final Deployment deployment = new Deployment();
        final Injector injector = Guice.createInjector(new DeployerConfigurationModule());

        final Deployer iamDeployer = injector.getInstance(IAMDeployer.class);
        final Deployer lambdaDeployer = injector.getInstance(LambdaDeployer.class);

        /* The order of chaining the deployers matters. */
        deployment
                .chainDeployer(iamDeployer)
                .chainDeployer(lambdaDeployer);

        return deployment;
    }
}

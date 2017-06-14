package surf;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.deployers.Deployer;
import surf.deployers.DeployerConfiguration;
import surf.deployers.DeployerConfigurationModule;
import surf.deployers.api.ApiDeployer;
import surf.deployers.dynamo.DynamoDeployer;
import surf.deployers.iam.IAMDeployer;
import surf.deployers.lambda.LambdaDeployer;
import surf.deployers.sleep.SleepDeployer;
import surf.deployers.sns.SNSDeployer;
import surf.deployment.Deployment;
import surf.deployment.DeploymentFinalizer;
import surf.exceptions.OperationFailedException;
import surf.utility.ExitCode;

import javax.annotation.Nonnull;
import java.io.IOException;
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
            DeployerConfiguration deployerConfiguration = DeployerConfiguration.fromFile(DEPLOYER_CONFIG_FILE_PATH);
            final Deployment completeDeployment = buildCompleteDeployment(deployerConfiguration);
            completeDeployment.start();

            final DeploymentFinalizer deploymentFinalizer = new DeploymentFinalizer(
                    deployerConfiguration,
                    completeDeployment
            );
            deploymentFinalizer.dumpClientConfigurationToFile();
            deploymentFinalizer.dumpLambdaConfigurationToFile();

            /* We need to redeploy lambda in order for the api generated resources to be accessible via
            the generated files from the lambda functions. */
            final Deployment lambdaOnlyDeployment = buildLambdaOnlyDeployment(deployerConfiguration);
            lambdaOnlyDeployment.start();

        } catch (OperationFailedException
                | IOException e) {
            LOG.error("Exception while executing deployment!", e);
            System.exit(ExitCode.Error.getCode());
        }
    }

    private static Deployment buildCompleteDeployment(@Nonnull final DeployerConfiguration deployerConfiguration) {
        final Deployment deployment = new Deployment("Deployment Stage #1: Complete deployment");
        final Injector injector = Guice.createInjector(new DeployerConfigurationModule(deployerConfiguration));

        final Deployer iamDeployer = injector.getInstance(IAMDeployer.class);
        final Deployer lambdaDeployer = injector.getInstance(LambdaDeployer.class);
        final Deployer apiDeployer = injector.getInstance(ApiDeployer.class);
        final Deployer dynamoDeployer = injector.getInstance(DynamoDeployer.class);
        final Deployer snsDeployer = injector.getInstance(SNSDeployer.class);
        // TODO the sleep deployer should really be set to something between 15 and 30 seconds
        final Deployer sleepDeployer = new SleepDeployer(1, TimeUnit.SECONDS);

        /* The order of chaining the deployers matters. */
        deployment
                .chainDeployer(iamDeployer)
                .chainDeployer(sleepDeployer) // ensure IAM permissions consistency in AWS
                .chainDeployer(lambdaDeployer)
                .chainDeployer(sleepDeployer) // ensure Lambda create consistency in AWS
                .chainDeployer(snsDeployer)
                .chainDeployer(dynamoDeployer)
                .chainDeployer(apiDeployer);

        return deployment;
    }

    private static Deployment buildLambdaOnlyDeployment(@Nonnull final DeployerConfiguration deployerConfiguration) {
        final Deployment deployment = new Deployment("Deployment Stage #2: Lambda-only deployment");
        final Injector injector = Guice.createInjector(new DeployerConfigurationModule(deployerConfiguration));

        final Deployer iamDeployer = injector.getInstance(IAMDeployer.class);
        final Deployer lambdaDeployer = injector.getInstance(LambdaDeployer.class);

        /* The order of chaining the deployers matters. */
        deployment
                .chainDeployer(iamDeployer)
                .chainDeployer(lambdaDeployer);

        return deployment;
    }
}

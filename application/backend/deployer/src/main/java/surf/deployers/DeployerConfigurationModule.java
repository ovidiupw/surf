package surf.deployers;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.model.FunctionCode;
import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.ExitCode;
import surf.Main;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

public class DeployerConfigurationModule extends AbstractModule {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static final int CLIENT_EXECUTION_TIMEOUT_SECONDS = 30;
    private static final Regions CLIENT_REGION = Regions.EU_WEST_1;
    private static final String LAMBDA_CODE_LOCATION = "../lambda/target/surf-lambda-backend-1.0-SNAPSHOT.jar";
    private static final String LAMBDA_RUNTIME = "java8";

    @Override
    protected void configure() {
        try {
            DeployerConfiguration deployerConfiguration = initializeDeployerConfiguration();
            bind(DeployerConfiguration.class).toInstance(deployerConfiguration);
        } catch (IOException e) {
            LOG.error("Error while initializing deployer configuration!", e);
            System.exit(ExitCode.Error.getCode());
        } finally {
            LOG.warn("surf.deployers.Deployer exited prematurely!");
        }

    }

    private DeployerConfiguration initializeDeployerConfiguration() throws IOException {
        final ClientConfiguration clientConfiguration = new ClientConfiguration()
                .withClientExecutionTimeout((int) TimeUnit.SECONDS.toMillis(CLIENT_EXECUTION_TIMEOUT_SECONDS));

        final FunctionCode lambdaFunctionsCode = new FunctionCode()
                .withZipFile(ByteBuffer.wrap(Files.readAllBytes(new File(LAMBDA_CODE_LOCATION).toPath())));

        return new DeployerConfiguration.Builder()
                .withClientConfiguration(clientConfiguration)
                .withLambdaFunctionCode(lambdaFunctionsCode)
                .withLambdaRuntime(LAMBDA_RUNTIME)
                .withRegion(CLIENT_REGION)
                .build();
    }
}

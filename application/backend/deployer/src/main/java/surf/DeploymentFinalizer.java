package surf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.deployers.DeployerConfiguration;
import surf.deployment.Deployment;
import surf.utility.ClientConfigurationConstants;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DeploymentFinalizer {
    private static final Logger LOG = LoggerFactory.getLogger(DeploymentFinalizer.class);

    private final DeployerConfiguration deployerConfiguration;
    private final Deployment deployment;

    public DeploymentFinalizer(@Nonnull final DeployerConfiguration deployerConfiguration,
                               @Nonnull final Deployment deployment) {
        Preconditions.checkNotNull(deployerConfiguration);
        Preconditions.checkNotNull(deployment);

        this.deployerConfiguration = deployerConfiguration;
        this.deployment = deployment;
    }

    public void dumpClientConfigurationToFile() throws IOException {
        LOG.info("Dumping client configuration to file with path='{}'", deployerConfiguration.getClientConfigFilePath());

        final ClientConfigurationConstants clientConstants = new ClientConfigurationConstants.Builder()
                .withAWSRegion(deployerConfiguration.getAwsClientRegion())
                .withAWSAccessKey(deployerConfiguration.getAwsAccessKey())
                .withFacebookWebIdentityBasicRole(
                        deployment.getContext().getIAMRoles().getFacebookWebIdentityBasicRole().getArn())
                .withApiKey(deployment.getContext().getApiKey())
                .build();

        final Path clientConfigFileParentPath = Paths.get(deployerConfiguration.getClientConfigFilePath()).getParent();
        File clientConfigDirectory = new File(clientConfigFileParentPath.toUri());

        if (clientConfigDirectory.isFile()) {
            throw new IOException("The parent directory of the client config file should not be a file! Please delete the file "
                    + clientConfigFileParentPath.toString() + " before proceeding!");
        }

        if (!clientConfigDirectory.exists()) {
            boolean mkdirsSuccessful = clientConfigDirectory.mkdirs();
            if (!mkdirsSuccessful) {
                throw new IOException("The directory structure could not be created to support the file path "
                        + clientConfigDirectory.getPath());
            }
        }

        final File clientConfigFile = new File(deployerConfiguration.getClientConfigFilePath());

        if (clientConfigFile.exists() && clientConfigFile.isDirectory()) {
            throw new IOException("Cannot write client config file because a directory with the same name already exists at "
                    + deployerConfiguration.getClientConfigFilePath() + "!");
        }

        LOG.info("Found config file at {}. Deleting it before trying to recreate it.",
                deployerConfiguration.getClientConfigFilePath());


        if (clientConfigFile.exists()) {
            Files.delete(Paths.get(clientConfigFile.getPath()));
        }

        new ObjectMapper().writeValue(clientConfigFile, clientConstants);
    }
}

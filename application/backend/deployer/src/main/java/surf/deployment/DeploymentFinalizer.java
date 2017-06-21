package surf.deployment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import models.config.LambdaConfigurationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.deployers.DeployerConfiguration;
import surf.deployers.s3.S3Deployer;
import surf.utility.ClientConfigurationConstants;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

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

    public void dumpLambdaConfigurationToFile() throws IOException {
        final String lambdaConfigFilePath = deployerConfiguration.getLambdaConfigFilePath();
        LOG.info("Dumping lambda configuration to file with path='{}'", lambdaConfigFilePath);

        final LambdaConfigurationConstants clientConstants = new LambdaConfigurationConstants.Builder()
                .withAwsClientRegion(deployerConfiguration.getAwsClientRegion().getName())
                .withAwsClientExecutionTimeoutSeconds(
                        (int) TimeUnit.MILLISECONDS.toSeconds(
                                deployerConfiguration.getClientConfiguration().getClientExecutionTimeout()))
                .withStepFunctionsInvokeLambdaRoleArn(deployment.getContext().getIAMRoles().getSfnInvokeLambdaRole().getArn())
                .withInitializeCrawlSessionSNSTopicArn(deployment.getContext().getInitializeCrawlSessionSNSTopicArn())
                .withFinalizeCrawlSessionLambdaArn(deployment.getContext().getLambdaFunctionsData().getFinalizeCrawlSessionData().getFunctionArn())
                .withCrawlWebPageLambdaArn(deployment.getContext().getLambdaFunctionsData().getCrawlWebPageData().getFunctionArn())
                .withApplicationS3BucketName(S3Deployer.S3_APPLICATION_BUCKET_NAME)
                .build();

        dumpObjectToFile(clientConstants, lambdaConfigFilePath);
    }

    public void dumpClientConfigurationToFile() throws IOException {
        final String clientConfigFilePath = deployerConfiguration.getClientConfigFilePath();
        LOG.info("Dumping client configuration to file with path='{}'", clientConfigFilePath);

        final ClientConfigurationConstants clientConstants = new ClientConfigurationConstants.Builder()
                .withAWSRegion(deployerConfiguration.getAwsClientRegion())
                .withAWSAccessKey(deployerConfiguration.getAwsAccessKey())
                .withFacebookWebIdentityBasicRole(
                        deployment.getContext().getIAMRoles().getFacebookWebIdentityBasicRole().getArn())
                .withApiKey(deployment.getContext().getApiKey())
                .build();

        dumpObjectToFile(clientConstants, clientConfigFilePath);
    }

    private void dumpObjectToFile(@Nonnull final Object object, @Nonnull final String filePath) throws IOException {
        final Path fileParentDirectoryPath = Paths.get(filePath).getParent();
        final File fileParentDirectory = new File(fileParentDirectoryPath.toUri());

        if (fileParentDirectory.isFile()) {
            throw new IOException("The parent directory of the dumped file should not be a file! Please delete the file "
                    + fileParentDirectoryPath.toString() + " before proceeding!");
        }

        if (!fileParentDirectory.exists()) {
            boolean mkdirsSuccessful = fileParentDirectory.mkdirs();
            if (!mkdirsSuccessful) {
                throw new IOException("The directory structure could not be created to support the file path "
                        + fileParentDirectory.getPath());
            }
        }

        final File file = new File(filePath);

        if (file.exists() && file.isDirectory()) {
            throw new IOException("Cannot write object to file because a directory with the same name already exists at "
                    + filePath + "!");
        }

        LOG.info("Found file at {}. Deleting it before trying to recreate it.", filePath);
        if (file.exists()) {
            Files.delete(Paths.get(file.getPath()));
        }

        new ObjectMapper().writeValue(file, object);
    }
}

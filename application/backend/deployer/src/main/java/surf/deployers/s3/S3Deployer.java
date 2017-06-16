package surf.deployers.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import org.apache.maven.shared.invoker.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.deployers.Deployer;
import surf.deployers.DeployerConfiguration;
import surf.deployment.Context;
import surf.exceptions.OperationFailedException;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Collections;

public class S3Deployer implements Deployer {

    private static final Logger LOG = LoggerFactory.getLogger(S3Deployer.class);

    private static final String DEPLOYER_NAME = "S3Deployer";

    private static final String MAVEN_GOAL = "package";
    private static final String MAVEN_LAMBDA_POM_PATH = "../lambda/pom.xml";
    public static final String MAVEN_HOME = "/usr/local/";

    private static final String S3_APPLICATION_BUCKET_NAME = "surf-web-crawler";
    private static final String S3_LAMBDA_CODE_ZIP_KEY = "config/lambda_code.zip";

    private final DeployerConfiguration deployerConfiguration;

    @Inject
    public S3Deployer(@Nonnull final DeployerConfiguration deployerConfiguration) {
        Preconditions.checkNotNull(deployerConfiguration);
        this.deployerConfiguration = deployerConfiguration;
    }

    @Override
    public String getName() {
        return DEPLOYER_NAME;
    }

    @Override
    public Context deploy(@Nonnull final Context context) throws OperationFailedException {
        Preconditions.checkNotNull(context);
        final AmazonS3 s3Client = initializeS3Client();

        buildLambdaCodeJar(); // Prepare the .jar file

        final String appConfigBucketName = createBucket(s3Client, S3_APPLICATION_BUCKET_NAME);
        putFile(
                s3Client,
                appConfigBucketName,
                S3_LAMBDA_CODE_ZIP_KEY,
                new File(deployerConfiguration.getLambdaCodePath()));

        context.setS3AppConfigBucketName(appConfigBucketName);
        context.setS3LambdaCodeKey(S3_LAMBDA_CODE_ZIP_KEY);

        return context;
    }

    private PutObjectResult putFile(
            @Nonnull final AmazonS3 s3Client,
            @Nonnull final String bucketName,
            @Nonnull final String objectKey,
            @Nonnull final File file) {
        try {
            LOG.info("Trying to put file with key='{}' into S3 bucket with name='{}'", objectKey, bucketName);
            final PutObjectResult putObjectResult = s3Client.putObject(bucketName, objectKey, file);
            LOG.info("Successfully put file into S3 bucket!");
            return putObjectResult;
        } catch (SdkClientException e) {
            LOG.error("Error while trying to put file into S3 bucket!", e);
            throw new OperationFailedException(e);
        }
    }

    private String createBucket(
            @Nonnull final AmazonS3 s3Client,
            @Nonnull final String bucketName) {
        try {
            LOG.info("Trying to create S3 bucket with name='{}'...", bucketName);
            final Bucket bucket = s3Client.createBucket(bucketName);
            LOG.info("Successfully created S3 bucket with name={}!", bucketName);
            return bucket.getName();
        } catch (AmazonServiceException e) {
            if (e.getStatusCode() == 409) {
                LOG.info("!!! S3 bucket with name='{}' already existed. Will use that instead of creating a new one.");
                return bucketName;
            } else {
                LOG.error("Error while trying to create S3 bucket!");
                throw new OperationFailedException(e);
            }
        } catch (SdkClientException e) {
            LOG.error("Error while trying to create S3 bucket!");
            throw new OperationFailedException(e);
        }
    }

    private static void buildLambdaCodeJar() {
        LOG.info("Trying to execute maven goal with name='{}' for pom with path='{}'", MAVEN_GOAL, MAVEN_LAMBDA_POM_PATH);

        final InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(MAVEN_LAMBDA_POM_PATH));
        request.setGoals(Collections.singletonList(MAVEN_GOAL));

        final Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(MAVEN_HOME));
        try {
            invoker.execute(request);
        } catch (MavenInvocationException e) {
            LOG.error("There was an error while trying to execute maven goal!", e);
            throw new OperationFailedException(e);
        }
    }

    private AmazonS3 initializeS3Client() {
        return AmazonS3ClientBuilder.standard()
                .withClientConfiguration(deployerConfiguration.getClientConfiguration())
                .withRegion(deployerConfiguration.getAwsClientRegion())
                .build();
    }
}

package utils.aws.s3;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import models.config.LambdaConfigurationConstants;
import utils.Logger;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

public class S3ClientHelper {
    private final Logger LOG;

    public S3ClientHelper(@Nonnull final LambdaLogger lambdaLogger) {
        this.LOG = new Logger(lambdaLogger);
    }

    public AmazonS3 getS3Client(@Nonnull final LambdaConfigurationConstants config) {
        LOG.info("Trying to initialize S3 client with executionTimeout=%s and region=%s",
                config.getAwsClientExecutionTimeoutSeconds(),
                config.getAwsClientRegion());

        final AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withClientConfiguration(new ClientConfiguration()
                        .withClientExecutionTimeout(
                                (int) TimeUnit.SECONDS.toMillis(config.getAwsClientExecutionTimeoutSeconds())))
                .withRegion(config.getAwsClientRegion())
                .build();

        LOG.info("Successfully initialized S3 client!");
        return s3Client;
    }

}

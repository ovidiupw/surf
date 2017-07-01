package utils.aws.s3;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.google.common.base.Preconditions;
import models.config.LambdaConfigurationConstants;
import utils.Logger;

import javax.annotation.Nonnull;
import java.net.URL;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class S3OperationsHelper {
    private final AmazonS3 s3Client;
    private final Logger LOG;
    private final LambdaConfigurationConstants config;

    public S3OperationsHelper(
            @Nonnull final AmazonS3 s3Client,
            @Nonnull final Logger logger,
            @Nonnull final LambdaConfigurationConstants config) {
        Preconditions.checkNotNull(s3Client, "The S3 client was null!");
        Preconditions.checkNotNull(logger);
        Preconditions.checkNotNull(config);
        Preconditions.checkNotNull(config.getStepFunctionsInvokeLambdaRoleArn());

        this.s3Client = s3Client;
        this.LOG = logger;
        this.config = config;
    }

    public URL generateCrawledDataPresignedUrl(
            @Nonnull final String bucketName,
            @Nonnull final String objectKey) {
        LOG.info("Trying to generate crawled data presigned url for bucketName='%s', objectKey='%s'",
                bucketName, objectKey);
        try {
            final Date urlExpirationDate = Date.from(Instant.now());
            urlExpirationDate.setTime(urlExpirationDate.getTime() + TimeUnit.MINUTES.toMillis(5));

            final URL presignedUrl = s3Client.generatePresignedUrl(bucketName, objectKey, urlExpirationDate);
            LOG.info("Successfully generated pre-signed url='%s' with expirationDate='%s'",
                    presignedUrl, urlExpirationDate);
            return presignedUrl;
        } catch (SdkClientException e) {
            LOG.error("Error while trying to generate S3 pre-signed url for crawled data!", e);
            throw e;
        }
    }

    public PutObjectResult putData(
            @Nonnull final String bucketName,
            @Nonnull final String objectKey,
            @Nonnull final String objectContent) throws SdkClientException {
        try {
            LOG.info("Trying to put data with key='%s' into S3 bucket with name='%s'", objectKey, bucketName);
            final PutObjectResult putObjectResult = s3Client.putObject(bucketName, objectKey, objectContent);
            LOG.info("Successfully put data into S3 bucket!");
            return putObjectResult;
        } catch (SdkClientException e) {
            LOG.error("Error while trying to put file with key='%s' into S3 bucket with name='%s'!",
                    objectContent, bucketName);
            throw e;
        }
    }


}

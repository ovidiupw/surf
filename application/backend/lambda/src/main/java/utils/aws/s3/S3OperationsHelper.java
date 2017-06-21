package utils.aws.s3;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.google.common.base.Preconditions;
import models.config.LambdaConfigurationConstants;
import utils.Logger;

import javax.annotation.Nonnull;

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

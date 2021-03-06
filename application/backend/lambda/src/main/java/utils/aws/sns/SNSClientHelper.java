package utils.aws.sns;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import models.config.LambdaConfigurationConstants;
import utils.Logger;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

public class SNSClientHelper {
    private final Logger LOG;

    public SNSClientHelper(@Nonnull final Logger LOG) {
        this.LOG = LOG;
    }

    public AmazonSNS getSNSClient(@Nonnull final LambdaConfigurationConstants config) {
        LOG.info("Trying to initialize SNS client with executionTimeout=%s and region=%s",
                config.getAwsClientExecutionTimeoutSeconds(),
                config.getAwsClientRegion());

        final AmazonSNS snsClient = AmazonSNSClientBuilder.standard()
                .withClientConfiguration(new ClientConfiguration()
                        .withClientExecutionTimeout(
                                (int) TimeUnit.SECONDS.toMillis(config.getAwsClientExecutionTimeoutSeconds())))
                .withRegion(config.getAwsClientRegion())
                .build();

        LOG.info("Successfully initialized SNS client!");
        return snsClient;
    }

}

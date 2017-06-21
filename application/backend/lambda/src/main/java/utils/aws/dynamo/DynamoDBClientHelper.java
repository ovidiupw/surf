package utils.aws.dynamo;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import models.config.LambdaConfigurationConstants;
import utils.Logger;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

public class DynamoDBClientHelper {
    private final Logger LOG;

    public DynamoDBClientHelper(@Nonnull final Logger logger) {
        this.LOG = logger;
    }

    public AmazonDynamoDB getDynamoDBClient(@Nonnull final LambdaConfigurationConstants config) {
        LOG.info("Trying to initialize Dynamo client with executionTimeout=%s and region=%s",
                config.getAwsClientExecutionTimeoutSeconds(),
                config.getAwsClientRegion());

        final AmazonDynamoDB dynamoClient = AmazonDynamoDBClientBuilder.standard()
                .withClientConfiguration(new ClientConfiguration()
                        .withClientExecutionTimeout(
                                (int) TimeUnit.SECONDS.toMillis(config.getAwsClientExecutionTimeoutSeconds())))
                .withRegion(config.getAwsClientRegion())
                .build();

        LOG.info("Successfully initialized Dynamo client!");
        return dynamoClient;
    }

}

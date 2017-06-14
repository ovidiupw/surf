package utils;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import models.config.LambdaConfigurationConstants;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

public class DynamoDBHelper {
    private final LambdaLogger logger;

    public DynamoDBHelper(@Nonnull final LambdaLogger logger) {
        this.logger = logger;
    }

    public AmazonDynamoDB getDynamoDBClient(@Nonnull final LambdaConfigurationConstants config) {
        Logger.log(logger, "Trying to initialize Dynamo client with executionTimeout=%s and region=%s",
                config.getAwsClientExecutionTimeoutSeconds(),
                config.getAwsClientRegion());

        final AmazonDynamoDB dynamoClient = AmazonDynamoDBClientBuilder.standard()
                .withClientConfiguration(new ClientConfiguration()
                        .withClientExecutionTimeout(
                                (int) TimeUnit.SECONDS.toMillis(config.getAwsClientExecutionTimeoutSeconds())))
                .withRegion(config.getAwsClientRegion())
                .build();

        Logger.log(logger, "Successfully initialized Dynamo client!");
        return dynamoClient;
    }

}

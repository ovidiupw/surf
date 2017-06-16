package utils;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import models.config.LambdaConfigurationConstants;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

public class SNSClientHelper {
    private final LambdaLogger logger;

    public SNSClientHelper(@Nonnull final LambdaLogger logger) {
        this.logger = logger;
    }

    public AmazonSNS getSNSClient(@Nonnull final LambdaConfigurationConstants config) {
        Logger.log(logger, "Trying to initialize SNS client with executionTimeout=%s and region=%s",
                config.getAwsClientExecutionTimeoutSeconds(),
                config.getAwsClientRegion());

        final AmazonSNS snsClient = AmazonSNSClientBuilder.standard()
                .withClientConfiguration(new ClientConfiguration()
                        .withClientExecutionTimeout(
                                (int) TimeUnit.SECONDS.toMillis(config.getAwsClientExecutionTimeoutSeconds())))
                .withRegion(config.getAwsClientRegion())
                .build();

        Logger.log(logger, "Successfully initialized SNS client!");
        return snsClient;
    }

}

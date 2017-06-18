package utils.aws.sfn;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.AWSStepFunctionsClientBuilder;
import models.config.LambdaConfigurationConstants;
import utils.Logger;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

public class StepFunctionsClientHelper {
    private final LambdaLogger logger;

    public StepFunctionsClientHelper(@Nonnull final LambdaLogger logger) {
        this.logger = logger;
    }

    public AWSStepFunctions getStepFunctionsClient(@Nonnull final LambdaConfigurationConstants config) {
        Logger.log(logger, "Trying to initialize StepFunctions client with executionTimeout=%s and region=%s",
                config.getAwsClientExecutionTimeoutSeconds(),
                config.getAwsClientRegion());

        final AWSStepFunctions stepFunctionsClient = AWSStepFunctionsClientBuilder.standard()
                .withClientConfiguration(new ClientConfiguration()
                        .withClientExecutionTimeout(
                                (int) TimeUnit.SECONDS.toMillis(config.getAwsClientExecutionTimeoutSeconds())))
                .withRegion(config.getAwsClientRegion())
                .build();

        Logger.log(logger, "Successfully initialized StepFunctions client!");
        return stepFunctionsClient;
    }
}

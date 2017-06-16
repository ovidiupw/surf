package utils;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import handlers.InitializeCrawlSessionHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SNSOperationsHelper {

    private final AmazonSNS snsClient;
    private final Context context;

    public SNSOperationsHelper(@Nonnull final AmazonSNS snsClient, @Nonnull final Context context) {
        Preconditions.checkNotNull(snsClient, "The SNS client was null!");
        this.snsClient = snsClient;
        this.context = context;
    }


    public String publishMessage(
            @Nonnull final String topicArn,
            @Nullable final InitializeCrawlSessionHandler.Input payload) throws JsonProcessingException {

        Preconditions.checkArgument(!Strings.isNullOrEmpty(topicArn), "The 'topicArn' cannot be null or empty when publishing to SNS");

        final String message = new ObjectMapper().writeValueAsString(payload);

        Logger.log(context.getLogger(), "Trying to publish message='%s' to SNS topic arn='%s'...", message, topicArn);

        snsClient.publish(new PublishRequest()
                .withTopicArn(topicArn)
                .withMessage(message));

        return message;
    }
}

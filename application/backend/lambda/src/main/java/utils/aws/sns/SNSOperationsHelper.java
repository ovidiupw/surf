package utils.aws.sns;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import handlers.InitializeCrawlSessionHandler;
import models.exceptions.InternalServerException;
import utils.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SNSOperationsHelper {

    private final AmazonSNS snsClient;
    private final Logger LOG;

    public SNSOperationsHelper(@Nonnull final AmazonSNS snsClient, @Nonnull final Context context) {
        Preconditions.checkNotNull(snsClient, "The SNS client was null!");
        this.snsClient = snsClient;
        this.LOG = new Logger(context.getLogger());
    }


    public String publishMessage(
            @Nonnull final String topicArn,
            @Nullable final InitializeCrawlSessionHandler.Input payload) {

        Preconditions.checkArgument(!Strings.isNullOrEmpty(topicArn), "The 'topicArn' cannot be null or empty when publishing to SNS");

        String message;
        try {
            LOG.info("Trying to serialize payload='%s' to attach to SNS message...", payload);
            message = new ObjectMapper().writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            final String errorMessage = LOG.error("Exception when serializing payload to attach to SNS message!");
            throw new InternalServerException(errorMessage);
        }

        LOG.info("Trying to publish message='%s' to SNS topic arn='%s'...", message, topicArn);

        snsClient.publish(new PublishRequest()
                .withTopicArn(topicArn)
                .withMessage(message));

        LOG.info("Successfully published SNS message!");

        return message;
    }
}

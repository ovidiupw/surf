package surf.deployers.sns;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.*;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.deployers.Deployer;
import surf.deployers.DeployerConfiguration;
import surf.deployment.Context;
import surf.exceptions.OperationFailedException;

import javax.annotation.Nonnull;

public class SNSDeployer implements Deployer {

    private static final Logger LOG = LoggerFactory.getLogger(SNSDeployer.class);

    private static final String DEPLOYER_NAME = "SNSDeployer";
    private static final String SNS_INITIALIZE_CRAWL_SESSION_TOPIC_NAME = "SurfCrawlSessionInitializers";

    private final DeployerConfiguration deployerConfiguration;

    @Inject
    public SNSDeployer(@Nonnull final DeployerConfiguration deployerConfiguration) {
        Preconditions.checkNotNull(deployerConfiguration);
        this.deployerConfiguration = deployerConfiguration;
    }

    @Override
    public String getName() {
        return DEPLOYER_NAME;
    }

    @Override
    public Context deploy(@Nonnull Context context) throws OperationFailedException {
        Preconditions.checkNotNull(context);
        verifyInitializeCrawlSessionLambdaArnIsInContext(context);
        final AmazonSNS snsClient = initializeSNSClient();

        final CreateTopicResult initCrawlSessionTopic
                = createTopic(snsClient, SNS_INITIALIZE_CRAWL_SESSION_TOPIC_NAME);

        subscribeLambdaToTopic(
                snsClient,
                context.getLambdaFunctionsData().getInitializeCrawlSessionData().getFunctionArn(),
                initCrawlSessionTopic.getTopicArn());

        context.setInitializeCrawlSessionSNSTopicArn(initCrawlSessionTopic.getTopicArn());

        return context;
    }

    private SubscribeResult subscribeLambdaToTopic(AmazonSNS snsClient, String lambdaArn, String topicArn) {
        try {
            LOG.info("Trying to subscribe lambda with lambdaArn={} to SNS topic with topicArn={}",
                    lambdaArn, topicArn);
            final SubscribeResult lambdaSubscription = snsClient.subscribe(new SubscribeRequest()
                    .withTopicArn(topicArn)
                    .withEndpoint(lambdaArn)
                    .withProtocol("lambda"));
            LOG.info("Successfully subscribed lambda to SNS topic!");
            return lambdaSubscription;
        } catch (SubscriptionLimitExceededException
                | InvalidParameterException
                | InternalErrorException
                | NotFoundException
                | AuthorizationErrorException e) {
            LOG.error("Error while trying to subscribe lambda function to SNS topic!", e);
            throw new OperationFailedException(e);
        }

    }

    private void verifyInitializeCrawlSessionLambdaArnIsInContext(@Nonnull final Context context) {
        Preconditions.checkNotNull(context.getIAMRoles());
        Preconditions.checkNotNull(context.getIAMRoles().getInitializeCrawlSessionLambdaRole());
        Preconditions.checkArgument(
                Strings.isNotBlank(
                        context.getIAMRoles().getInitializeCrawlSessionLambdaRole().getArn()));
    }

    private CreateTopicResult createTopic(@Nonnull final AmazonSNS snsClient, @Nonnull final String topicName) {

        try {
            LOG.info("Trying to create SNS topic with name={}", topicName);
            final CreateTopicResult topic = snsClient.createTopic(new CreateTopicRequest()
                    .withName(topicName));
            LOG.info("Successfully created SNS topic with name={}, arn={}!", topicName, topic.getTopicArn());
            return topic;
        } catch (InvalidParameterException
                | TopicLimitExceededException
                | InternalErrorException
                | AuthorizationErrorException e) {
            LOG.error("Error while trying to create SNS topic!", e);
            throw new OperationFailedException(e);
        }
    }

    private AmazonSNS initializeSNSClient() {
        return AmazonSNSClientBuilder.standard()
                .withClientConfiguration(deployerConfiguration.getClientConfiguration())
                .withRegion(deployerConfiguration.getAwsClientRegion())
                .build();
    }
}

package handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sns.AmazonSNS;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import models.Validateable;
import models.config.LambdaConfigurationConstants;
import models.exceptions.BadRequestException;
import models.workflow.PageToBeVisited;
import models.workflow.Workflow;
import models.workflow.WorkflowExecution;
import models.workflow.WorkflowTask;
import utils.*;
import utils.aws.dynamo.DynamoDBClientHelper;
import utils.aws.dynamo.DynamoDBOperationsHelper;
import utils.aws.sns.SNSClientHelper;
import utils.aws.sns.SNSOperationsHelper;
import validators.StartWorkflowInputValidator;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class StartWorkflowHandler implements
        RequestHandler<StartWorkflowHandler.Input, StartWorkflowHandler.Output>,
        WrappableRequestHandler<StartWorkflowHandler.Input, StartWorkflowHandler.Output> {

    private static final int STARTING_DEPTH_LEVEL = 0;

    private LambdaConfigurationConstants config;
    private DynamoDBOperationsHelper dynamoOperationsHelper;
    private SNSOperationsHelper snsOperationsHelper;
    private StartWorkflowInputValidator inputValidator;
    private Logger LOG;

    public StartWorkflowHandler.Output handleRequest(final StartWorkflowHandler.Input input, final Context context) {
        final ExceptionWrapper<Input, Output> exceptionWrapper = new ExceptionWrapper<>(input, context);
        return exceptionWrapper.doHandleRequest(this);
    }

    @Override
    public Output doHandleRequest(Input input, Context context) {
        initializeInstance(context);
        LOG.info("input=%s", input.toString());

        inputValidator.validate(input);

        final Workflow workflow = dynamoOperationsHelper.getWorkflow(input.getWorkflowId());
        if (workflow == null) {
            throw new BadRequestException(
                    "The workflow was deleted while trying to start a new execution based on its data!");
        }

        final WorkflowExecution workflowExecution = SurfObjectMother.createWorkflowExecution(workflow, input.getUserArn());
        final WorkflowExecution savedWorkflowExecution = dynamoOperationsHelper.saveWorkflowExecution(workflowExecution);

        String taskUrl;
        try {
            taskUrl = new URI(workflow.getMetadata().getRootAddress()).normalize().toString();
        } catch (URISyntaxException e) {
            throw new BadRequestException(e.getMessage());
        }

        final WorkflowTask workflowTask;
            workflowTask = SurfObjectMother.createWorkflowTask(
                    workflowExecution.getId(),
                    workflowExecution.getOwnerId(),
                    workflow.getMetadata().getMaxWebPageSizeBytes(),
                    workflow.getMetadata().getSelectionPolicy(),
                    workflow.getMetadata().getUrlMatcher(),
                    taskUrl,
                    STARTING_DEPTH_LEVEL);
        dynamoOperationsHelper.saveWorkflowTask(workflowTask);

        final PageToBeVisited pageToBeVisited = SurfObjectMother.createPageToBeVisited(
                workflowExecution.getId(),
                workflowTask.getUrl()
        );
        dynamoOperationsHelper.savePageToBeVisited(pageToBeVisited);

        final InitializeCrawlSessionHandler.Input initializeCrawlSessionInput
                = SurfObjectMother.generateInitializeCrawlSessionInput(
                savedWorkflowExecution.getId(),
                STARTING_DEPTH_LEVEL);

        LOG.info("Trying to send SNS notification to 'InitializeCrawlSessionHandler' with payload='%s'...",
                initializeCrawlSessionInput);

        final String targetSNSTopicArn = config.getInitializeCrawlSessionSNSTopicArn();
        snsOperationsHelper.publishMessage(targetSNSTopicArn, initializeCrawlSessionInput);

        final StartWorkflowHandler.Output output = new StartWorkflowHandler.Output();
        output.setWorkflowExecution(savedWorkflowExecution);
        return output;
    }

    private void initializeInstance(final Context context) {
        LOG = new Logger(context.getLogger());

        config = FileReader.readObjectFromFile(Constants.CONFIG_FILE_PATH, LambdaConfigurationConstants.class);
        LOG.info("Using lambda config '%s'", config);

        final AmazonDynamoDB dynamoClient = new DynamoDBClientHelper(LOG).getDynamoDBClient(config);
        final AmazonSNS snsClient = new SNSClientHelper(LOG).getSNSClient(config);

        dynamoOperationsHelper = new DynamoDBOperationsHelper(dynamoClient, LOG);
        snsOperationsHelper = new SNSOperationsHelper(snsClient, context);
        inputValidator = new StartWorkflowInputValidator(context, dynamoClient);
    }

    public static class Input implements Validateable {
        private String userArn;
        private String workflowId;

        public String getUserArn() {
            return userArn;
        }

        public void setUserArn(String userArn) {
            this.userArn = userArn;
        }

        public String getWorkflowId() {
            return workflowId;
        }

        public void setWorkflowId(String workflowId) {
            this.workflowId = workflowId;
        }

        @Override
        public String toString() {
            return "Input{" +
                    "userArn='" + userArn + '\'' +
                    ", workflowId='" + workflowId + '\'' +
                    '}';
        }

        public void validate() {
            Preconditions.checkArgument(
                    !Strings.isNullOrEmpty(userArn),
                    "The input 'userArn' must not be null or empty"
            );
            Preconditions.checkArgument(
                    !Strings.isNullOrEmpty(workflowId),
                    "The input 'workflowId' must not be null or empty, but a valid workflow id."
            );
        }
    }

    public static class Output {
        private WorkflowExecution workflowExecution;

        public WorkflowExecution getWorkflowExecution() {
            return workflowExecution;
        }

        public void setWorkflowExecution(WorkflowExecution workflowExecution) {
            this.workflowExecution = workflowExecution;
        }

        @Override
        public String toString() {
            return "Output{" +
                    "workflowExecution=" + workflowExecution +
                    '}';
        }
    }
}

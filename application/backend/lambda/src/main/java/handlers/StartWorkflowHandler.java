package handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sns.AmazonSNS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import models.Validateable;
import models.workflow.Workflow;
import models.workflow.WorkflowExecution;
import models.config.LambdaConfigurationConstants;
import models.exceptions.BadRequestException;
import models.workflow.WorkflowTask;
import utils.*;
import utils.aws.dynamo.DynamoDBClientHelper;
import utils.aws.dynamo.DynamoDBOperationsHelper;
import utils.aws.sns.SNSClientHelper;
import utils.aws.sns.SNSOperationsHelper;
import validators.StartWorkflowInputValidator;

public class StartWorkflowHandler implements
        RequestHandler<StartWorkflowHandler.Input, StartWorkflowHandler.Output>,
        WrappableRequestHandler<StartWorkflowHandler.Input, StartWorkflowHandler.Output> {

    private static final int STARTING_DEPTH_LEVEL = 0;

    private LambdaConfigurationConstants config;
    private DynamoDBOperationsHelper dynamoOperationsHelper;
    private SNSOperationsHelper snsOperationsHelper;
    private StartWorkflowInputValidator inputValidator;

    public StartWorkflowHandler.Output handleRequest(final StartWorkflowHandler.Input input, final Context context) {
        final ExceptionWrapper<Input, Output> exceptionWrapper = new ExceptionWrapper<>(input, context);
        return exceptionWrapper.doHandleRequest(this);
    }

    @Override
    public Output doHandleRequest(Input input, Context context) {
        Logger.log(context.getLogger(), "input=%s", input.toString());
        initializeInstance(context);

        inputValidator.validate(input);

        final Workflow workflow = dynamoOperationsHelper.getWorkflow(input.getWorkflowId());
        if (workflow == null) {
            throw new BadRequestException(
                    "The workflow was deleted while trying to start a new execution based on its data!");
        }

        final WorkflowExecution workflowExecution
                = SurfObjectMother.createWorkflowExecution(workflow, input.getUserArn());

        Logger.log(context.getLogger(), "Trying to save new workflow execution to database...");
        final WorkflowExecution savedWorkflowExecution = dynamoOperationsHelper.saveWorkflowExecution(workflowExecution);
        Logger.log(context.getLogger(), "Successfully saved new workflow execution='%s' to database!", savedWorkflowExecution);

        final WorkflowTask workflowTask = SurfObjectMother.createWorkflowTask(
                        workflowExecution.getId(),
                        workflowExecution.getOwnerId(),
                        workflow.getMetadata(),
                        workflow.getMetadata().getRootAddress(),
                        STARTING_DEPTH_LEVEL);

        Logger.log(context.getLogger(), "Trying to save the task='%s' for crawling the root address in the database...", workflowTask);
        final WorkflowTask savedWorkflowTask = dynamoOperationsHelper.putWorkflowTask(workflowTask);
        Logger.log(context.getLogger(), "Successfully saved the task='%s' in the database!", savedWorkflowTask);

        final InitializeCrawlSessionHandler.Input initializeCrawlSessionInput
                = SurfObjectMother.generateInitializeCrawlSessionInput(
                savedWorkflowExecution.getId(),
                STARTING_DEPTH_LEVEL);

        Logger.log(context.getLogger(),
                "Trying to send SNS notification to 'InitializeCrawlSessionHandler' with payload='%s'...",
                initializeCrawlSessionInput);

        final String targetSNSTopicArn = config.getInitializeCrawlSessionSNSTopicArn();
        try {
            String message = snsOperationsHelper.publishMessage(targetSNSTopicArn, initializeCrawlSessionInput);
            Logger.log(context.getLogger(),
                    "Successfully published message='%s' to SNS topicArn='%s'", message, targetSNSTopicArn);

        } catch (JsonProcessingException | RuntimeException e) {
            Logger.log(context.getLogger(), "Could not publish SNS notification to topicArn='%s'", targetSNSTopicArn);
            throw new RuntimeException(e); // let the ExceptionWrapper deal with this unexpected exception
        }

        final StartWorkflowHandler.Output output = new StartWorkflowHandler.Output();
        output.setWorkflowExecution(savedWorkflowExecution);
        return output;
    }

    private void initializeInstance(final Context context) {
        config = FileReader.readObjectFromFile(Constants.CONFIG_FILE_PATH, LambdaConfigurationConstants.class);
        Logger.log(context.getLogger(), "Using lambda config '%s'", config);

        final AmazonDynamoDB dynamoClient = new DynamoDBClientHelper(context.getLogger()).getDynamoDBClient(config);
        final AmazonSNS snsClient = new SNSClientHelper(context.getLogger()).getSNSClient(config);

        dynamoOperationsHelper = new DynamoDBOperationsHelper(dynamoClient, context.getLogger());
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

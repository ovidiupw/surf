package handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.model.CreateStateMachineResult;
import com.amazonaws.services.stepfunctions.model.StartExecutionResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import models.config.LambdaConfigurationConstants;
import models.exceptions.BadRequestException;
import models.exceptions.InternalServerException;
import models.workflow.*;
import utils.*;
import utils.aws.dynamo.DynamoDBClientHelper;
import utils.aws.dynamo.DynamoDBOperationsHelper;
import utils.aws.sfn.StepFunctionsClientHelper;
import utils.aws.sfn.StepFunctionsOperationsHelper;
import validators.InitializeCrawlSessionInputValidator;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

public class InitializeCrawlSessionHandler implements
        RequestHandler<SNSEvent, InitializeCrawlSessionHandler.Output>,
        WrappableRequestHandler<InitializeCrawlSessionHandler.Input, InitializeCrawlSessionHandler.Output> {

    private LambdaConfigurationConstants config;
    private DynamoDBOperationsHelper dynamoOperationsHelper;
    private StepFunctionsOperationsHelper sfnOperationsHelper;
    private InitializeCrawlSessionInputValidator inputValidator;

    @Override
    public InitializeCrawlSessionHandler.Output handleRequest(
            final SNSEvent snsEvent,
            final Context context) {

        final Input input;

        try {
            input = new ObjectMapper().readValue(
                    snsEvent.getRecords().get(0).getSNS().getMessage(),
                    Input.class
            );
            Logger.log(context.getLogger(), "Input payload from SNS event='%s'", input);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerException("Could not deserialize SNS event's message to Input because of an IOException!");
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new BadRequestException("Could not get SNS event payload! Make sure the SNS object is correct!");
        }

        final ExceptionWrapper<InitializeCrawlSessionHandler.Input, InitializeCrawlSessionHandler.Output> exceptionWrapper
                = new ExceptionWrapper<>(input, context);
        return exceptionWrapper.doHandleRequest(this);
    }

    @Override
    public InitializeCrawlSessionHandler.Output doHandleRequest(
            InitializeCrawlSessionHandler.Input input,
            Context context) {
        Logger.log(context.getLogger(), "input=%s", input.toString());
        initializeInstance(context);

        inputValidator.validate(input);

        final WorkflowExecution workflowExecution = getWorkflowExecution(input, context);
        setWorkflowExecutionToActive(context, workflowExecution);
        final Workflow workflow = getWorkflow(context, workflowExecution);

        int maxTasksToSchedule = (int) workflow.getMetadata().getMaxConcurrentCrawlers();
        final List<WorkflowTask> pendingTasks = dynamoOperationsHelper.getTasks(
                workflowExecution.getId(), Status.Pending, input.getCurrentDepthLevel(), maxTasksToSchedule);

        if (pendingTasks.isEmpty()) {
            Logger.log(context.getLogger(), "Found no remaining tasks!");
            completeWorkflowExecution(context, workflowExecution);
            return null;
        }
        if (input.getCurrentDepthLevel() > workflow.getMetadata().getMaxRecursionDepth()) {
            Logger.log(context.getLogger(), "The recursion depth level exceeded the maxRecursionDepth='%s'!",
                    workflow.getMetadata().getMaxRecursionDepth());
            completeWorkflowExecution(context, workflowExecution);
            return null;
        }

        final StateMachineDefinition stateMachineDefinition = new StateMachineDefinition(workflow, pendingTasks, config);
        final CreateStateMachineResult stateMachine = sfnOperationsHelper.createStateMachine(stateMachineDefinition);

        final StateMachineExecutionDefinition executionDefinition = new StateMachineExecutionDefinition(stateMachineDefinition);
        final StartExecutionResult execution = sfnOperationsHelper.startStateMachine(stateMachine, executionDefinition);

        setWorkflowExecutionStateMachineData(
                context, workflowExecution, stateMachine.getStateMachineArn(), execution.getExecutionArn());

        final InitializeCrawlSessionHandler.Output output = new InitializeCrawlSessionHandler.Output();
        output.setStateMachineExecutionArn(execution.getExecutionArn());

        return output;
    }

    private void initializeInstance(Context context) {
        config = FileReader.readObjectFromFile(Constants.CONFIG_FILE_PATH, LambdaConfigurationConstants.class);
        Logger.log(context.getLogger(), "Using lambda config '%s'", config);

        final AmazonDynamoDB dynamoClient
                = new DynamoDBClientHelper(context.getLogger()).getDynamoDBClient(config);
        final AWSStepFunctions stepFunctionsClient
                = new StepFunctionsClientHelper(context.getLogger()).getStepFunctionsClient(config);

        dynamoOperationsHelper = new DynamoDBOperationsHelper(dynamoClient, context.getLogger());
        sfnOperationsHelper = new StepFunctionsOperationsHelper(stepFunctionsClient, context.getLogger(), config);
        inputValidator = new InitializeCrawlSessionInputValidator(context);
    }

    private void setWorkflowExecutionStateMachineData(
            Context context, WorkflowExecution workflowExecution, String stateMachineArn, String executionArn) {
        Logger.log(context.getLogger(),
                "Trying to set workflow execution state machine data for workflowExecutionId='%s'",
                workflowExecution.getId());

        workflowExecution.setStateMachineArn(stateMachineArn);
        workflowExecution.setStateMachineExecutionArn(executionArn);

        dynamoOperationsHelper.saveWorkflowExecution(workflowExecution);

        Logger.log(context.getLogger(),
                "Successfully updated workflow execution stateMachineArn='%s' and stateMachineExecutionArn='%s'",
                workflowExecution.getStateMachineArn(),
                workflowExecution.getStateMachineExecutionArn());
    }

    private Workflow getWorkflow(Context context, WorkflowExecution workflowExecution) {
        final Workflow workflow
                = dynamoOperationsHelper.getWorkflow(workflowExecution.getWorkflowId());
        Logger.log(context.getLogger(), "workflow='%s'", workflow);

        if (workflow == null) {
            throw new BadRequestException(
                    "The workflow was deleted while trying to perform an execution based on its data!");
        }

        return workflow;
    }

    private WorkflowExecution getWorkflowExecution(Input input, Context context) {
        final WorkflowExecution workflowExecution
                = dynamoOperationsHelper.getWorkflowExecution(input.getWorkflowExecutionId());
        Logger.log(context.getLogger(), "workflowExecution='%s'", workflowExecution);
        return workflowExecution;
    }

    private void setWorkflowExecutionToActive(Context context, WorkflowExecution workflowExecution) {
        Logger.log(context.getLogger(), "Setting workflow execution status to '%s'...", Status.Active);
        workflowExecution.setStartDateMillis(System.currentTimeMillis());
        workflowExecution.setStatus(Status.Active);
        dynamoOperationsHelper.saveWorkflowExecution(workflowExecution);
        Logger.log(context.getLogger(), "Successfully set workflow execution status to '%s'!", Status.Active);
    }

    private void completeWorkflowExecution(Context context, WorkflowExecution workflowExecution) {
        Logger.log(context.getLogger(), "Setting workflow execution status to '%s'", Status.Completed);
        workflowExecution.setStatus(Status.Completed);
        workflowExecution.setEndDateMillis(System.currentTimeMillis());
        dynamoOperationsHelper.saveWorkflowExecution(workflowExecution);
    }

    public static class Input {
        private String workflowExecutionId;
        private int currentDepthLevel;

        public String getWorkflowExecutionId() {
            return workflowExecutionId;
        }

        public void setWorkflowExecutionId(String workflowExecutionId) {
            this.workflowExecutionId = workflowExecutionId;
        }

        public int getCurrentDepthLevel() {
            return currentDepthLevel;
        }

        public void setCurrentDepthLevel(int currentDepthLevel) {
            this.currentDepthLevel = currentDepthLevel;
        }

        @Override
        public String toString() {
            return "Input{" +
                    "workflowExecutionId='" + workflowExecutionId + '\'' +
                    ", currentDepthLevel=" + currentDepthLevel +
                    '}';
        }

        public void validate() {
            Preconditions.checkNotNull(
                    workflowExecutionId,
                    "The input 'workflowExecutionId' must not be null!");
            Preconditions.checkArgument(
                    currentDepthLevel >= 0,
                    "The input 'currentDepthLevel' must be >= 0!"
            );
        }
    }

    public static class Output {
        private String stateMachineExecutionArn;

        public String getStateMachineExecutionArn() {
            return stateMachineExecutionArn;
        }

        public void setStateMachineExecutionArn(String stateMachineExecutionArn) {
            this.stateMachineExecutionArn = stateMachineExecutionArn;
        }
    }
}

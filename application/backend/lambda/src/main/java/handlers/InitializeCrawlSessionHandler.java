package handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughputExceededException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.model.CreateStateMachineResult;
import com.amazonaws.services.stepfunctions.model.StartExecutionResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import models.config.LambdaConfigurationConstants;
import models.exceptions.BadRequestException;
import models.exceptions.InternalServerException;
import models.workflow.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import utils.Constants;
import utils.FileReader;
import utils.Logger;
import utils.SurfObjectMother;
import utils.aws.dynamo.DynamoDBClientHelper;
import utils.aws.dynamo.DynamoDBOperationsHelper;
import utils.aws.sfn.StepFunctionsClientHelper;
import utils.aws.sfn.StepFunctionsOperationsHelper;
import utils.aws.sns.SNSClientHelper;
import utils.aws.sns.SNSOperationsHelper;
import validators.InitializeCrawlSessionInputValidator;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InitializeCrawlSessionHandler implements RequestHandler<SNSEvent, InitializeCrawlSessionHandler.Output> {

    private LambdaConfigurationConstants config;
    private DynamoDBOperationsHelper dynamoOperationsHelper;
    private StepFunctionsOperationsHelper sfnOperationsHelper;
    private SNSOperationsHelper snsOperationsHelper;
    private InitializeCrawlSessionInputValidator inputValidator;
    private Logger LOG;

    @Override
    public InitializeCrawlSessionHandler.Output handleRequest(final SNSEvent snsEvent, final Context context) {
        initializeInstance(context);

        final Input input;
        try {
            LOG.info("Received SNS event: '%s'", snsEvent);
            LOG.info("Trying to deserialize SNS event's message...");
            input = new ObjectMapper().readValue(snsEvent.getRecords().get(0).getSNS().getMessage(), Input.class);
            LOG.info("Successfully deserialized message payload (Input) from SNS event='%s'", input);
        } catch (IOException e) {
            LOG.error("IOException while trying to read SNS event: '%s'", ExceptionUtils.getStackTrace(e));
            throw new InternalServerException("Could not deserialize SNS event's message because of an IOException!");
        } catch (NullPointerException e) {
            LOG.error("NullPointerException while trying to read SNS event: '%s'", ExceptionUtils.getStackTrace(e));
            throw new BadRequestException("Could not get SNS event payload! Make sure the SNS event record is correct!");
        }

        inputValidator.validate(input);
        return doHandleRequest(input);
    }

    private void initializeInstance(Context context) {
        this.LOG = new Logger(context.getLogger());

        this.config = FileReader.readObjectFromFile(Constants.CONFIG_FILE_PATH, LambdaConfigurationConstants.class);
        LOG.info("Using lambda config '%s'", config);

        final AmazonDynamoDB dynamoClient = new DynamoDBClientHelper(LOG).getDynamoDBClient(config);
        final AWSStepFunctions stepFunctionsClient = new StepFunctionsClientHelper(LOG).getStepFunctionsClient(config);
        final AmazonSNS snsClient = new SNSClientHelper(LOG).getSNSClient(config);

        this.snsOperationsHelper = new SNSOperationsHelper(snsClient, context);
        this.dynamoOperationsHelper = new DynamoDBOperationsHelper(dynamoClient, LOG);
        this.sfnOperationsHelper = new StepFunctionsOperationsHelper(stepFunctionsClient, LOG, config);
        this.inputValidator = new InitializeCrawlSessionInputValidator(context);
    }

    private InitializeCrawlSessionHandler.Output doHandleRequest(
            @Nonnull final InitializeCrawlSessionHandler.Input input) {

        final WorkflowExecution workflowExecution
                = dynamoOperationsHelper.getWorkflowExecution(input.getWorkflowExecutionId());
        if (workflowExecution.getStatus() != Status.Active) {
            setWorkflowExecutionToActive(workflowExecution);
        }

        final Workflow workflow = getWorkflow(workflowExecution.getWorkflowId());
        long maxRecursionDepth = workflow.getMetadata().getMaxRecursionDepth();

        if (input.getCurrentDepthLevel() > maxRecursionDepth) {
            LOG.warn("The recursion depth level exceeded the maxRecursionDepth='%s'!", maxRecursionDepth);
            completeWorkflowExecution(workflowExecution);
            return null; // Do not proceed with further workflow execution initialization
        }

        final List<WorkflowTask> pendingTasks = dynamoOperationsHelper.getTasks(
                workflowExecution.getId(), Status.Pending, input.getCurrentDepthLevel());

        long maxTasksToSchedule = workflow.getMetadata().getMaxConcurrentCrawlers();
        final List<WorkflowTask> tasksToSchedule = getTasksToSchedule(
                pendingTasks, workflowExecution.getId(), maxTasksToSchedule);

        if (tasksToSchedule.isEmpty()) {
            if (input.getCurrentDepthLevel() < maxRecursionDepth) {
                final InitializeCrawlSessionHandler.Input initializeCrawlSessionInput
                        = SurfObjectMother.generateInitializeCrawlSessionInput(
                        workflowExecution.getId(),
                        input.getCurrentDepthLevel() + 1);

                LOG.info("Trying to send SNS notification to 'InitializeCrawlSessionHandler' with payload='%s'...",
                        initializeCrawlSessionInput);
                final String targetSNSTopicArn = config.getInitializeCrawlSessionSNSTopicArn();
                snsOperationsHelper.publishMessage(targetSNSTopicArn, initializeCrawlSessionInput);

                return null; // Exit crawl session initialization because there's no point in continuing at
                             // the current depth level
            }

            LOG.warn("Found no remaining tasks to schedule!");
            completeWorkflowExecution(workflowExecution);
            return null; // Do not proceed with further workflow execution initialization
        }

        final StateMachineDefinition stateMachineDefinition
                = new StateMachineDefinition(workflow, workflowExecution, tasksToSchedule, config);
        final CreateStateMachineResult stateMachine = sfnOperationsHelper.createStateMachine(stateMachineDefinition);

        final StateMachineExecutionDefinition executionDefinition
                = new StateMachineExecutionDefinition(stateMachineDefinition);
        final StartExecutionResult execution = sfnOperationsHelper.startStateMachine(stateMachine, executionDefinition);

        setWorkflowExecutionStateMachineData(workflowExecution, stateMachine.getStateMachineArn());

        final InitializeCrawlSessionHandler.Output output = new InitializeCrawlSessionHandler.Output();
        output.setStateMachineExecutionArn(execution.getExecutionArn());

        return output;
    }

    private void setWorkflowExecutionStateMachineData(
            @Nonnull final WorkflowExecution workflowExecution,
            @Nonnull final String stateMachineArn) {
        LOG.info("Trying to set workflow execution state machine data for workflowExecutionId='%s'",
                workflowExecution.getId());

        workflowExecution.setStateMachineArn(stateMachineArn);
        dynamoOperationsHelper.saveWorkflowExecution(workflowExecution);
        LOG.info("Successfully updated workflow execution stateMachineArn='%s'",
                workflowExecution.getStateMachineArn());
    }

    /**
     * Sets the status of the supplied workflow execution to {@link Status#Active} and sets the start date millis
     * of the workflow execution to the current system time ({@link System#currentTimeMillis()}).
     * <p>
     * Then saves the workflow execution to the database.
     *
     * @param workflowExecution The workflow execution that is to be mutated by this method and saved to the database
     */
    private void setWorkflowExecutionToActive(@Nonnull final WorkflowExecution workflowExecution) {
        LOG.info("Setting workflow execution status to '%s'...", Status.Active);

        workflowExecution.setStartDateMillis(System.currentTimeMillis());
        workflowExecution.setStatus(Status.Active);
        dynamoOperationsHelper.saveWorkflowExecution(workflowExecution);

        LOG.info("Successfully set workflow execution status to '%s'!", Status.Active);
    }


    private Workflow getWorkflow(@Nonnull final String workflowId) {
        final Workflow workflow = dynamoOperationsHelper.getWorkflow(workflowId);
        if (workflow == null) {
            throw new BadRequestException("The workflow was deleted while trying to start a new workflow execution!");
        }
        return workflow;
    }

    /**
     * Sets the status of the supplied workflow execution to {@link Status#Completed} and sets the end date millis
     * of the workflow execution to the current system time ({@link System#currentTimeMillis()}).
     * <p>
     * Then saves the workflow execution to the database.
     *
     * @param workflowExecution The workflow execution that is to be mutated by this method and saved to the database
     */
    private void completeWorkflowExecution(@Nonnull final WorkflowExecution workflowExecution) {
        LOG.info("Setting workflow execution status to '%s'...", Status.Completed);

        workflowExecution.setStatus(Status.Completed);
        workflowExecution.setEndDateMillis(System.currentTimeMillis());
        dynamoOperationsHelper.saveWorkflowExecution(workflowExecution);

        LOG.info("Successfully set workflow execution status to '%s'!", Status.Completed);

        LOG.info("Trying to cleanup leftover pagesToBeVisited from the database...");

        try {
            final List<PageToBeVisited> pagesToBeVisited
                    = dynamoOperationsHelper.listPagesToBeVisited(workflowExecution.getId());
            dynamoOperationsHelper.deletePagesToBeVisited(pagesToBeVisited);

            LOG.info("Leftover pagesToBeVisited cleanup succeeded!");
        } catch (ProvisionedThroughputExceededException e) {
            LOG.error("Provisioned throughput exceeded while trying to delete pages to be visited " +
                    "for workflowExecutionId='%s'", workflowExecution.getId());
        }

    }

    private List<WorkflowTask> getTasksToSchedule(
            @Nonnull final List<WorkflowTask> candidateTasksForScheduling,
            @Nonnull final String workflowExecutionId,
            final long maxTasksToSchedule) {
        final List<WorkflowTask> tasksToSchedule = new ArrayList<>();

        for (final WorkflowTask task : candidateTasksForScheduling) {
            if (tasksToSchedule.size() > maxTasksToSchedule) {
                return tasksToSchedule;
            }

            LOG.info("Checking if task's url for task='%s' was already visited...", task);
            final VisitedPage visitedPage = dynamoOperationsHelper.getVisitedPage(
                    workflowExecutionId, task.getUrl());

            if (visitedPage == null) {
                LOG.info("The task's url='%s' was not previously visited. Task will be scheduled!", task.getUrl());
                tasksToSchedule.add(task);
            } else {
                LOG.info("The task's url='%s' was already visited! visitedPage='%s'", task.getUrl(), visitedPage);
                // TODO in crawl webpage handler -> task.setEndDate(System.currentTimeMillis());
            }
        }

        return tasksToSchedule;
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

package handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.config.LambdaConfigurationConstants;
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

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FinalizeCrawlSessionHandler implements RequestStreamHandler {

    private LambdaConfigurationConstants config;
    private DynamoDBOperationsHelper dynamoOperationsHelper;
    private SNSOperationsHelper snsOperationsHelper;
    private StepFunctionsOperationsHelper sfnOperationsHelper;
    private Logger LOG;

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) {
        initializeInstance(context);
        final ObjectMapper objectMapper = new ObjectMapper();

        try {
            byte[] inputStreamBytes = getByteArray(inputStream);
            inputStream.close(); // Prevent inputStream from being used again

            ByteArrayInputStream byteArrayInputStream;

            try {
                byteArrayInputStream = new ByteArrayInputStream(inputStreamBytes);
                LOG.info("Raw input: '%s'", IOUtils.toString(byteArrayInputStream));

                byteArrayInputStream = new ByteArrayInputStream(inputStreamBytes);
                final List<CrawlWebPageStateOutput> crawlWebPageStateOutputs = objectMapper.readValue(
                        byteArrayInputStream,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, CrawlWebPageStateOutput.class));

                finalizeSuccessfulCrawlingSession(crawlWebPageStateOutputs);

            } catch (JsonMappingException e) {
                LOG.error(ExceptionUtils.getStackTrace(e));
                LOG.info("Trying to see if it's an exception we've got from the StepFunctions workflow...");

                byteArrayInputStream = new ByteArrayInputStream(inputStreamBytes);
                final StepFunctionsError stepFunctionsError = objectMapper.readValue(
                        byteArrayInputStream,
                        StepFunctionsError.class);

                finalizeFailedCrawlingSession(stepFunctionsError);
            }
        } catch (IOException e) {
            LOG.error(ExceptionUtils.getStackTrace(e));
            LOG.error("Exception while trying to finalize the crawling session: '%s'", e.getMessage());
        }
    }

    private void initializeInstance(Context context) {
        LOG = new Logger(context.getLogger());

        config = FileReader.readObjectFromFile(Constants.CONFIG_FILE_PATH, LambdaConfigurationConstants.class);
        LOG.info("Using lambda config '%s'", config);

        final AmazonDynamoDB dynamoClient = new DynamoDBClientHelper(LOG).getDynamoDBClient(config);
        final AmazonSNS snsClient = new SNSClientHelper(LOG).getSNSClient(config);
        final AWSStepFunctions stepFunctionsClient = new StepFunctionsClientHelper(LOG).getStepFunctionsClient(config);

        dynamoOperationsHelper = new DynamoDBOperationsHelper(dynamoClient, LOG);
        snsOperationsHelper = new SNSOperationsHelper(snsClient, context);
        sfnOperationsHelper = new StepFunctionsOperationsHelper(stepFunctionsClient, LOG, config);
    }

    private byte[] getByteArray(InputStream inputStream) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * This branch handles the case when no fatal error was encountered during the parallel state execution.
     * {@link #finalizeFailedCrawlingSession(StepFunctionsError)} handles fatal errors.
     */
    private void finalizeSuccessfulCrawlingSession(
            final List<CrawlWebPageStateOutput> crawlWebPageStateOutputs) throws JsonProcessingException {

        LOG.info("Input='%s'", crawlWebPageStateOutputs);
        LOG.info("Crawling session was successful!");

        final String workflowExecutionId = crawlWebPageStateOutputs.get(0).getWorkflowExecutionId();
        final WorkflowExecution workflowExecution = dynamoOperationsHelper.getWorkflowExecution(workflowExecutionId);
        final Workflow workflow = dynamoOperationsHelper.getWorkflow(workflowExecution.getWorkflowId());

        long currentDepthLevel = crawlWebPageStateOutputs.get(0).getTaskDepthLevel();
        final Map<Status, List<WorkflowTask>> tasksByStatus
                = dynamoOperationsHelper.getTasksForEachStatus(workflowExecution.getId(), currentDepthLevel);

        performCommonFinalizingActions(workflow, workflowExecution, currentDepthLevel, tasksByStatus);
    }

    /**
     * This branch handles the fatal errors (i.e. the unexpected ones) that rose during the parallel state execution.
     */
    private void finalizeFailedCrawlingSession(
            final StepFunctionsError stepFunctionError) throws JsonProcessingException {
        LOG.error("Input='%s'", stepFunctionError);
        LOG.error("Error in step functions execution: error='%s', cause='%s', workflowExecutionId='%s'",
                stepFunctionError.getError(),
                stepFunctionError.getCause(),
                stepFunctionError.getStateInputs().get(0).getWorkflowExecutionId());
        LOG.error("Crawling session was UNSUCCESSFUL!");

        final String workflowExecutionId = stepFunctionError.getStateInputs().get(0).getWorkflowExecutionId();
        final WorkflowExecution workflowExecution = dynamoOperationsHelper.getWorkflowExecution(workflowExecutionId);
        final Workflow workflow = dynamoOperationsHelper.getWorkflow(workflowExecution.getWorkflowId());

        long currentDepthLevel = stepFunctionError.getStateInputs().get(0).getDepthLevel();
        final Map<Status, List<WorkflowTask>> tasksByStatus
                = dynamoOperationsHelper.getTasksForEachStatus(workflowExecution.getId(), currentDepthLevel);

        StepFunctionsError.Cause cause = new StepFunctionsError.Cause();
        cause.setErrorMessage("Error while executing parallel crawlers");

        if (stepFunctionError.getError().equals("States.Timeout")) {
            cause.setErrorMessage("One of the parallel workers has timed out");

            if (stepFunctionError.getStateInputs() != null) {
                LOG.info("Will iterate through state inputs and set each task as timed out...");

                for (CrawlWebPageStateInput crawlWebPageStateInput : stepFunctionError.getStateInputs()) {
                    final String workflowTaskId = crawlWebPageStateInput.getWorkflowTaskId();
                    if (workflowTaskId != null) {

                        /* Override eventual consistency problems by having
                        the timed-out task record updated offline, here */
                        for (final WorkflowTask activeTask : tasksByStatus.get(Status.Active)) {
                            if (activeTask.getId().equals(workflowTaskId)) {
                                activeTask.setStatus(Status.TimedOut);
                            }
                        }

                        final WorkflowTask workflowTask = dynamoOperationsHelper.getWorkflowTask(workflowTaskId);
                        workflowTask.setStatus(Status.TimedOut);
                        workflowTask.setEndDate(System.currentTimeMillis());
                        dynamoOperationsHelper.saveWorkflowTask(workflowTask);

                        try {
                            VisitedPage visitedPage = dynamoOperationsHelper.getVisitedPage(
                                    workflowTask.getWorkflowExecutionId(),
                                    workflowTask.getUrl()
                            );
                            if (visitedPage == null) {
                                visitedPage = SurfObjectMother.createVisitedPage(
                                        workflowTask.getWorkflowExecutionId(),
                                        workflowTask.getUrl(),
                                        workflowTask.getDepth());
                                dynamoOperationsHelper.saveVisitedPage(visitedPage);
                            } else {
                                LOG.warn("Visited page was already in the database!");
                            }
                        } catch (ConditionalCheckFailedException e) {
                            LOG.error(e.getMessage());
                            LOG.error("Visited page was already added to database!");
                        }

                        try {
                            PageToBeVisited pageToBeVisited = dynamoOperationsHelper.getPageToBeVisited(
                                    workflowTask.getId(),
                                    workflowTask.getUrl()
                            );
                            if (pageToBeVisited != null) {
                                dynamoOperationsHelper.deletePageToBeVisited(pageToBeVisited);
                            } else {
                                LOG.warn("PageToBeVisited did not exist in database; delete was not performed.");
                            }

                        } catch (ConditionalCheckFailedException e) {
                            LOG.error(e.getMessage());
                            LOG.error("Could not delete page to be visited from database related to taskId='%s'!",
                                    workflowTask.getId());
                        }
                    }
                }
            }
        }

        dynamoOperationsHelper.addErrorToWorkflowExecution(workflowExecution, stepFunctionError);

        performCommonFinalizingActions(workflow, workflowExecution, currentDepthLevel, tasksByStatus);
    }

    /**
     * When calling this method, the {@link InitializeCrawlSessionHandler} is invoked through SNS notification.
     */
    private void performCommonFinalizingActions(
            final Workflow workflow,
            final WorkflowExecution workflowExecution,
            final long currentDepthLevel,
            Map<Status, List<WorkflowTask>> tasksByStatus) throws JsonProcessingException {

        sfnOperationsHelper.deleteStateMachine(workflowExecution.getStateMachineArn());

        for (final WorkflowTask workflowTask : tasksByStatus.get(Status.Active)) {
            LOG.warn(
                    "WARNING! Found active task in workflow finalizer! Will set its status to cancelled and end it!");
            LOG.warn("Trying to end task with cancelled status for task='%s'...", workflowTask);

            workflowTask.setStatus(Status.Cancelled);
            workflowTask.setEndDate(System.currentTimeMillis());
            dynamoOperationsHelper.saveWorkflowTask(workflowTask);
        }

        final Set<String> pendingTaskUrls = new HashSet<>();
        for (final WorkflowTask workflowTask : tasksByStatus.get(Status.Pending)) {
            boolean setDidNotAlreadyContainUrl = pendingTaskUrls.add(workflowTask.getUrl());
            if (!setDidNotAlreadyContainUrl) {
                LOG.info("Deleting task='%s' because pendingTaskUrls='%s' already contained task's url='%s'",
                        workflowTask,
                        pendingTaskUrls,
                        workflowTask.getUrl());
                dynamoOperationsHelper.deleteWorkflowTask(workflowTask);
            }
        }

        int numberOfTasksExecuted
                = tasksByStatus.get(Status.Active).size()
                + tasksByStatus.get(Status.Failed).size()
                + tasksByStatus.get(Status.Cancelled).size()
                + tasksByStatus.get(Status.Completed).size();
        LOG.info("Number of tasks executed so far for depthLevel='%d': 'ds'",
                currentDepthLevel,
                numberOfTasksExecuted);

        final List<VisitedPage> visitedPages
                = dynamoOperationsHelper.listVisitedPages(workflowExecution.getId(), currentDepthLevel);
        LOG.info("Pages visited on depthLevel='%d': '%d'", currentDepthLevel, visitedPages.size());
        LOG.info("Maximum allowed pages to be visited on current depth level: '%d'",
                workflow.getMetadata().getMaxPagesPerDepthLevel());

        boolean visitedPagesExceedsNumberOfPagesForCurrentDepthLevel
                = visitedPages.size() > workflow.getMetadata().getMaxPagesPerDepthLevel();
        boolean noMorePendingTasksForCurrentLevel
                = tasksByStatus.get(Status.Pending).size() < 1;
        LOG.info("visitedPagesExceedsNumberOfPagesForCurrentDepthLevel='%s', noMorePendingTasksForCurrentLevel='%s'",
                visitedPagesExceedsNumberOfPagesForCurrentDepthLevel,
                noMorePendingTasksForCurrentLevel);

        long nextDepthLevel = currentDepthLevel;
        if (visitedPagesExceedsNumberOfPagesForCurrentDepthLevel || noMorePendingTasksForCurrentLevel) {
            nextDepthLevel++;

            for (final WorkflowTask workflowTask : tasksByStatus.get(Status.Pending)) {
                LOG.warn("Task='%s' did not execute because max tasks per depth level was exceeded!", workflowTask);
                workflowTask.setStatus(Status.Cancelled);
                workflowTask.setEndDate(System.currentTimeMillis());
                dynamoOperationsHelper.saveWorkflowTask(workflowTask);

            }

            LOG.info("Increasing crawling depth level to '%s'", nextDepthLevel);
        }

        final InitializeCrawlSessionHandler.Input input = new InitializeCrawlSessionHandler.Input();
        input.setCurrentDepthLevel((int) nextDepthLevel);
        input.setWorkflowExecutionId(workflowExecution.getId());
        snsOperationsHelper.publishMessage(config.getInitializeCrawlSessionSNSTopicArn(), input);
    }

    public static class Input {
        private List<CrawlWebPageStateOutput> crawlWebPageStateOutputs;

        public List<CrawlWebPageStateOutput> getCrawlWebPageStateOutputs() {
            return crawlWebPageStateOutputs;
        }

        public void setCrawlWebPageStateOutputs(List<CrawlWebPageStateOutput> crawlWebPageStateOutputs) {
            this.crawlWebPageStateOutputs = crawlWebPageStateOutputs;
        }

        @Override
        public String toString() {
            return "Input{" +
                    "crawlWebPageStateOutputs=" + crawlWebPageStateOutputs +
                    '}';
        }
    }

    public static class Output {

    }
}
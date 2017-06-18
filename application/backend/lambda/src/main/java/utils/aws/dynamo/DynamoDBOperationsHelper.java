package utils.aws.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import models.workflow.Status;
import models.workflow.Workflow;
import models.workflow.WorkflowExecution;
import models.workflow.WorkflowTask;
import utils.Logger;
import utils.SurfObjectMother;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamoDBOperationsHelper {
    private final AmazonDynamoDB dynamoClient;
    private final LambdaLogger lambdaLogger;

    public DynamoDBOperationsHelper(@Nonnull final AmazonDynamoDB dynamoClient, @Nonnull final LambdaLogger lambdaLogger) {
        Preconditions.checkNotNull(dynamoClient, "The DynamoDB client was null!");
        this.dynamoClient = dynamoClient;
        this.lambdaLogger = lambdaLogger;
    }


    public Workflow getWorkflow(@Nonnull final String workflowId) {
        Preconditions.checkArgument(
                !Strings.isNullOrEmpty(workflowId), "Cannot get workflow for which 'workflowId' is null or empty!");

        final Workflow workflowModel = new Workflow();
        workflowModel.setId(workflowId);

        final DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoClient);
        return dynamoDBMapper.load(workflowModel);
    }

    public QueryResultPage<Workflow> getWorkflowsByOwner(
            @Nonnull final String userArn,
            @Nullable final String startingWorkflowId,
            @Nullable Long createdBefore,
            @Nonnull final Integer resultsPerPage) {

        Preconditions.checkNotNull(userArn, "The 'userArn' cannot be null when trying to get workflows by owner!");
        Preconditions.checkNotNull(resultsPerPage, "The 'resultsPerPage' cannot be null when trying to get workflows by owner!");
        if (startingWorkflowId != null) {
            Preconditions.checkNotNull(createdBefore,
                    "'startingWorkflowId' and 'createdBefore' must either be both not null or both null!");
        }
        if (createdBefore != null) {
            Preconditions.checkNotNull(startingWorkflowId,
                    "'startingWorkflowId' and 'createdBefore' must either be both not null or both null!");
        }

        Map<String, AttributeValue> startKey = null;
        if (createdBefore != null && startingWorkflowId != null) {
            startKey = new HashMap<>(4);

            startKey.put(Workflow.DDB_ID, new AttributeValue().withS(startingWorkflowId));
            startKey.put(Workflow.DDB_OWNER_ID, new AttributeValue().withS(SurfObjectMother.getOwnerId(userArn)));
            startKey.put(Workflow.DDB_CREATION_DATE_MILLIS, new AttributeValue().withN(String.valueOf(createdBefore)));
        } else {
            createdBefore = System.currentTimeMillis();
        }

        final Condition rangeKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.LE)
                .withAttributeValueList(new AttributeValue().withN(String.valueOf(createdBefore)));

        final Workflow workflowModel = new Workflow();
        workflowModel.setId(startingWorkflowId);
        workflowModel.setOwnerId(SurfObjectMother.getOwnerId(userArn));

        final DynamoDBQueryExpression<Workflow> query = new DynamoDBQueryExpression<Workflow>()
                .withHashKeyValues(workflowModel)
                .withConsistentRead(false)
                .withScanIndexForward(false)
                .withExclusiveStartKey(startKey)
                .withRangeKeyCondition(Workflow.DDB_CREATION_DATE_MILLIS, rangeKeyCondition)
                .withLimit(resultsPerPage);

        final DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoClient);
        return dynamoDBMapper.queryPage(Workflow.class, query);
    }

    public Workflow saveWorkflow(@Nonnull final Workflow workflow) {
        Preconditions.checkNotNull(workflow, "The 'workflow' must be non-null when trying to save it to database!");

        final DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoClient);
        dynamoDBMapper.save(workflow);

        return dynamoDBMapper.load(workflow);
    }

    public WorkflowExecution saveWorkflowExecution(@Nonnull final WorkflowExecution workflowExecution) {
        Preconditions.checkNotNull(workflowExecution, "The 'workflowExecution' must be non-null when trying to save it to database!");

        final DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoClient);
        dynamoDBMapper.save(workflowExecution);

        return dynamoDBMapper.load(workflowExecution);
    }

    public WorkflowExecution getWorkflowExecution(@Nonnull final String workflowExecutionId) {
        Preconditions.checkArgument(
                !Strings.isNullOrEmpty(workflowExecutionId),
                "Cannot get workflow execution for which 'workflowExecutionId' is null or empty!");

        final WorkflowExecution workflowExecutionModel = new WorkflowExecution();
        workflowExecutionModel.setId(workflowExecutionId);

        final DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoClient);
        return dynamoDBMapper.load(workflowExecutionModel);
    }

    public WorkflowTask putWorkflowTask(@Nonnull final WorkflowTask workflowTask) {
        Preconditions.checkNotNull(workflowTask, "The 'workflowTask' must be non-null when trying to save it to database!");

        final DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoClient);
        dynamoDBMapper.save(workflowTask);

        return dynamoDBMapper.load(workflowTask);
    }

    public List<WorkflowTask> getTasks(
            @Nonnull final String workflowExecutionId,
            @Nonnull final Status taskStatus,
            final int taskDepthLevel) {
        return getTasks(workflowExecutionId, taskStatus, taskDepthLevel, Integer.MAX_VALUE);
    }

    public List<WorkflowTask> getTasks(
            @Nonnull final String workflowExecutionId,
            @Nonnull final Status taskStatus,
            final int taskDepthLevel,
            final int maxTasksToRetrieve) {
        Preconditions.checkArgument(
                !Strings.isNullOrEmpty(workflowExecutionId),
                "Cannot get tasks for which 'workflowExecutionId' is null or empty!");
        Preconditions.checkNotNull(
                taskStatus,
                "Cannot get tasks for which 'taskStatus' is null!"
        );
        Preconditions.checkArgument(
                taskDepthLevel >= 0,
                "A task's depth level must be >= 0!"
        );

        final WorkflowTask taskModel = new WorkflowTask();
        taskModel.setWorkflowExecutionId(workflowExecutionId);
        taskModel.setStatus(taskStatus);
        taskModel.setDepth(taskDepthLevel);

        final Condition rangeKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ)
                .withAttributeValueList(new AttributeValue().withS(taskModel.getStatusAndDepth()));

        Logger.log(lambdaLogger, "Getting workflow tasks based on taskModel='%s'", taskModel);
        Logger.log(lambdaLogger, "Getting workflow tasks based on condition='%s'", rangeKeyCondition);
        Logger.log(lambdaLogger, "Will use queryLimit='%s'", maxTasksToRetrieve);

        final DynamoDBQueryExpression<WorkflowTask> query = new DynamoDBQueryExpression<WorkflowTask>()
                .withHashKeyValues(taskModel)
                .withConsistentRead(false)
                .withRangeKeyCondition(WorkflowTask.DDB_STATUS_AND_DEPTH, rangeKeyCondition)
                .withLimit(maxTasksToRetrieve);

        final DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoClient);
        final PaginatedQueryList<WorkflowTask> workflowTasks = dynamoDBMapper.query(WorkflowTask.class, query);

        for (final WorkflowTask workflowTask : workflowTasks) {
            Logger.log(lambdaLogger, "Found '%s' task='%s'", taskStatus.getName(), workflowTasks);
        }

        return workflowTasks;
    }
}

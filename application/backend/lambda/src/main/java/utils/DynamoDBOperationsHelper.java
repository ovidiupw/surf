package utils;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import models.Workflow;
import models.WorkflowExecution;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class DynamoDBOperationsHelper {
    private final AmazonDynamoDB dynamoClient;

    public DynamoDBOperationsHelper(@Nonnull final AmazonDynamoDB dynamoClient) {
        Preconditions.checkNotNull(dynamoClient, "The DynamoDB client was null!");
        this.dynamoClient = dynamoClient;
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
            @Nullable  Long createdBefore,
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
}

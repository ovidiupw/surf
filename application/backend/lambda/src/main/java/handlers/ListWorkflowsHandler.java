package handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import models.Validateable;
import models.Workflow;
import models.config.LambdaConfigurationConstants;
import models.validators.ListWorkflowsInputValidator;
import utils.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListWorkflowsHandler implements
        RequestHandler<ListWorkflowsHandler.Input, ListWorkflowsHandler.Output>,
        WrappableRequestHandler<ListWorkflowsHandler.Input, ListWorkflowsHandler.Output> {

    private LambdaConfigurationConstants config;
    private AmazonDynamoDB dynamoClient;
    private ListWorkflowsInputValidator inputValidator;

    public ListWorkflowsHandler.Output handleRequest(final ListWorkflowsHandler.Input input, final Context context) {
        ExceptionWrapper<Input, Output> exceptionWrapper = new ExceptionWrapper<>(input, context);
        return exceptionWrapper.doHandleRequest(this);
    }

    @Override
    public Output doHandleRequest(final Input input, final Context context) {
        Logger.log(context.getLogger(), "input=%s", input.toString());
        initializeInstance(context, input.getUserArn());

        inputValidator.validate(input);

        final Workflow workflowModel = new Workflow();
        workflowModel.setId(input.getStartingWorkflowId());
        workflowModel.setOwnerId(
                String.join(
                        "@",
                        ArnHelper.getOwnerIdFromUserArn(input.getUserArn()),
                        ArnHelper.getAuthProviderFromUserArn(input.getUserArn())));

        Logger.log(context.getLogger(), "Will query for workflows with ownerId='%s'", workflowModel.getOwnerId());

        Long createdBefore = input.getCreatedBefore();

        Map<String, AttributeValue> startKey = null;
        if (createdBefore != null && input.getStartingWorkflowId() != null) {
            startKey = new HashMap<>(4);

            startKey.put(Workflow.DDB_ID, new AttributeValue().withS(input.getStartingWorkflowId()));
            startKey.put(Workflow.DDB_OWNER_ID, new AttributeValue().withS(workflowModel.getOwnerId()));
            startKey.put(Workflow.DDB_CREATION_DATE_MILLIS, new AttributeValue().withN(String.valueOf(createdBefore)));
        } else {
            createdBefore = System.currentTimeMillis();
        }

        Logger.log(context.getLogger(), "Will get workflows with '%s' <= '%s' and get the first '%d' results",
                Workflow.DDB_CREATION_DATE_MILLIS,
                createdBefore,
                input.getResultsPerPage());

        final Condition rangeKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.LE)
                .withAttributeValueList(new AttributeValue().withN(String.valueOf(createdBefore)));

        final DynamoDBQueryExpression<Workflow> query = new DynamoDBQueryExpression<Workflow>()
                .withHashKeyValues(workflowModel)
                .withConsistentRead(false)
                .withScanIndexForward(false)
                .withExclusiveStartKey(startKey)
                .withRangeKeyCondition(Workflow.DDB_CREATION_DATE_MILLIS, rangeKeyCondition)
                .withLimit(input.getResultsPerPage());

        final DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoClient);
        QueryResultPage<Workflow> workflowQueryResultPage = dynamoDBMapper.queryPage(Workflow.class, query);

        for (final Workflow wf : workflowQueryResultPage.getResults()) {
            Logger.log(context.getLogger(), "Found workflow='%s' that is owned by '%s'", wf, workflowModel.getOwnerId());
        }

        final ListWorkflowsHandler.Output output = new ListWorkflowsHandler.Output();
        output.setWorkflows(workflowQueryResultPage.getResults());

        return output;
    }

    private void initializeInstance(final Context context, final String userArn) {
        config = FileReader.readObjectFromFile(Constants.CONFIG_FILE_PATH, LambdaConfigurationConstants.class);
        Logger.log(context.getLogger(), "Using lambda config '%s'", config);

        dynamoClient = new DynamoDBHelper(context.getLogger()).getDynamoDBClient(config);
        inputValidator = new ListWorkflowsInputValidator(context);
    }

    public static class Input implements Validateable {
        private String userArn;
        private Long createdBefore;
        private String startingWorkflowId;
        private Integer resultsPerPage;

        public String getUserArn() {
            return userArn;
        }

        public void setUserArn(String userArn) {
            this.userArn = userArn;
        }

        public Long getCreatedBefore() {
            return createdBefore;
        }

        public void setCreatedBefore(Long createdBefore) {
            this.createdBefore = createdBefore;
        }

        public Integer getResultsPerPage() {
            return resultsPerPage;
        }

        public void setResultsPerPage(Integer resultsPerPage) {
            this.resultsPerPage = resultsPerPage;
        }

        public String getStartingWorkflowId() {
            return startingWorkflowId;
        }

        public void setStartingWorkflowId(String startingWorkflowId) {
            this.startingWorkflowId = startingWorkflowId;
        }

        @Override
        public String toString() {
            return "Input{" +
                    "userArn='" + userArn + '\'' +
                    ", createdBefore=" + createdBefore +
                    ", resultsPerPage=" + resultsPerPage +
                    ", startingWorkflowId='" + startingWorkflowId + '\'' +
                    '}';
        }

        @Override
        public void validate() {
            Preconditions.checkArgument(
                    !Strings.isNullOrEmpty(userArn),
                    "The input 'userArn' must not be null or empty");
            Preconditions.checkNotNull(
                    resultsPerPage,
                    "The 'resultsPerPage' field must be set!"
            );
            Preconditions.checkArgument(
                    resultsPerPage > 0,
                    "The 'resultsPerPage' must be > 0"
            );

            if (createdBefore != null || startingWorkflowId != null) {
                Preconditions.checkNotNull(
                        createdBefore,
                        "If either of 'createdBefore' or 'startingWorkflowId' is set, " +
                                "then all the others ones must be set! 'createdBefore' was not set!"
                );
                Preconditions.checkNotNull(
                        startingWorkflowId,
                        "If either of 'createdBefore' or 'startingWorkflowId' is set, " +
                                "then all the others ones must be set! 'startingWorkflowId' was not set!"
                );
            }
        }
    }

    public static class Output {
        private List<Workflow> workflows;

        public List<Workflow> getWorkflows() {
            return workflows;
        }

        public void setWorkflows(List<Workflow> workflows) {
            this.workflows = workflows;
        }

        @Override
        public String toString() {
            return "Output{" +
                    "workflows=" + workflows +
                    '}';
        }
    }
}

package handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import interpolators.ListWorkflowsInputInterpolator;
import models.Validateable;
import models.config.LambdaConfigurationConstants;
import models.workflow.Workflow;
import utils.*;
import utils.aws.dynamo.DynamoDBClientHelper;
import utils.aws.dynamo.DynamoDBOperationsHelper;
import validators.ListWorkflowsInputValidator;

import java.util.List;

public class ListWorkflowsHandler implements
        RequestHandler<ListWorkflowsHandler.Input, ListWorkflowsHandler.Output>,
        WrappableRequestHandler<ListWorkflowsHandler.Input, ListWorkflowsHandler.Output> {

    private DynamoDBOperationsHelper dynamoOperationsHelper;
    private ListWorkflowsInputValidator inputValidator;
    private ListWorkflowsInputInterpolator inputInterpolator;
    private Logger LOG;

    public ListWorkflowsHandler.Output handleRequest(final ListWorkflowsHandler.Input input, final Context context) {
        ExceptionWrapper<Input, Output> exceptionWrapper = new ExceptionWrapper<>(input, context);
        return exceptionWrapper.doHandleRequest(this);
    }

    @Override
    public Output doHandleRequest(Input input, final Context context) {
        initializeInstance(context);
        LOG.info("input=%s", input.toString());

        input = inputInterpolator.interpolate(input);
        inputValidator.validate(input);

        final QueryResultPage<Workflow> workflowsByOwnerPage = dynamoOperationsHelper.getWorkflowsByOwner(
                input.getUserArn(),
                input.getStartingWorkflowId(),
                input.getCreatedBefore(),
                input.getResultsPerPage());

        for (final Workflow wf : workflowsByOwnerPage.getResults()) {
            LOG.info("Found workflow='%s' that is owned by '%s'", wf, wf.getOwnerId());
        }

        final ListWorkflowsHandler.Output output = new ListWorkflowsHandler.Output();
        output.setWorkflows(workflowsByOwnerPage.getResults());

        return output;
    }

    private void initializeInstance(final Context context) {
        LOG = new Logger(context.getLogger());

        final LambdaConfigurationConstants config
                = FileReader.readObjectFromFile(Constants.CONFIG_FILE_PATH, LambdaConfigurationConstants.class);
        LOG.info("Using lambda config '%s'", config);

        AmazonDynamoDB dynamoClient = new DynamoDBClientHelper(LOG).getDynamoDBClient(config);
        dynamoOperationsHelper = new DynamoDBOperationsHelper(dynamoClient, LOG);
        inputValidator = new ListWorkflowsInputValidator(context);
        inputInterpolator = new ListWorkflowsInputInterpolator(context);
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
                        "If either of 'startingWorkflowId' is set, then 'createdBefore' must be set as well!"
                );
                Preconditions.checkNotNull(
                        startingWorkflowId,
                        "If either of 'createdBefore' is set, then 'startingWorkflowId' must be set as well!"
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

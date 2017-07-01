package handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import models.config.LambdaConfigurationConstants;
import models.workflow.WorkflowExecution;
import utils.*;
import utils.aws.dynamo.DynamoDBClientHelper;
import utils.aws.dynamo.DynamoDBOperationsHelper;
import validators.GetWorkflowInputValidator;
import validators.ListWorkflowExecutionsInputValidator;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ListWorkflowExecutionsHandler implements
        RequestHandler<ListWorkflowExecutionsHandler.Input, ListWorkflowExecutionsHandler.Output>,
        WrappableRequestHandler<ListWorkflowExecutionsHandler.Input, ListWorkflowExecutionsHandler.Output> {

    private Logger LOG;
    private DynamoDBOperationsHelper dynamoOperationsHelper;
    private ListWorkflowExecutionsInputValidator inputValidator;

    public ListWorkflowExecutionsHandler.Output handleRequest(
            final ListWorkflowExecutionsHandler.Input input,
            final Context context) {
        final ExceptionWrapper<Input, Output> exceptionWrapper = new ExceptionWrapper<>(input, context);
        return exceptionWrapper.doHandleRequest(this);
    }

    @Override
    public Output doHandleRequest(final Input input, final Context context) {
        initializeInstance(context);
        LOG.info("Input=%s", input.toString());

        inputValidator.validate(input);
        final List<WorkflowExecution> executions = dynamoOperationsHelper.listWorkflowExecutions(input.getWorkflowId());

        final Output output = new Output();
        if (executions == null) {
            output.setWorkflowExecutions(Collections.emptyList());
        } else {
            output.setWorkflowExecutions(executions);
        }

        return output;
    }

    private void initializeInstance(final Context context) {
        LOG = new Logger(context.getLogger());

        final LambdaConfigurationConstants config
                = FileReader.readObjectFromFile(Constants.CONFIG_FILE_PATH, LambdaConfigurationConstants.class);
        LOG.info("Using lambda config '%s'", config);

        final AmazonDynamoDB dynamoClient = new DynamoDBClientHelper(LOG).getDynamoDBClient(config);
        dynamoOperationsHelper = new DynamoDBOperationsHelper(dynamoClient, LOG);
        inputValidator = new ListWorkflowExecutionsInputValidator(context);
    }

    public static class Input {
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

        public void validate() {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(userArn), "The userArn cannot be null or empty!");
            Preconditions.checkArgument(!Strings.isNullOrEmpty(workflowId), "The workflowId cannot be null or empty!");
        }

        @Override
        public String toString() {
            return "Input{" +
                    "userArn='" + userArn + '\'' +
                    ", workflowId=" + workflowId +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Input input = (Input) o;
            return Objects.equals(userArn, input.userArn) &&
                    Objects.equals(workflowId, input.workflowId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userArn, workflowId);
        }
    }

    public static class Output {
        private List<WorkflowExecution> workflowExecutions;

        public List<WorkflowExecution> getWorkflowExecutions() {
            return workflowExecutions;
        }

        public void setWorkflowExecutions(List<WorkflowExecution> workflowExecutions) {
            this.workflowExecutions = workflowExecutions;
        }

        @Override
        public String toString() {
            return "Output{" +
                    "workflowExecutions=" + workflowExecutions +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Output output = (Output) o;
            return Objects.equals(workflowExecutions, output.workflowExecutions);
        }

        @Override
        public int hashCode() {
            return Objects.hash(workflowExecutions);
        }
    }
}

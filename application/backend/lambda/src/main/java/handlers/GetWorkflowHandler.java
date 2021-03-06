package handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import models.config.LambdaConfigurationConstants;
import models.exceptions.BadRequestException;
import models.workflow.Workflow;
import utils.*;
import utils.aws.dynamo.DynamoDBClientHelper;
import utils.aws.dynamo.DynamoDBOperationsHelper;
import validators.CreateWorkflowInputValidator;
import validators.GetWorkflowInputValidator;

import java.util.Objects;

public class GetWorkflowHandler implements
        RequestHandler<GetWorkflowHandler.Input, GetWorkflowHandler.Output>,
        WrappableRequestHandler<GetWorkflowHandler.Input, GetWorkflowHandler.Output>  {

    private Logger LOG;
    private DynamoDBOperationsHelper dynamoOperationsHelper;
    private GetWorkflowInputValidator inputValidator;

    public GetWorkflowHandler.Output handleRequest(final GetWorkflowHandler.Input input, final Context context) {
        final ExceptionWrapper<Input, Output> exceptionWrapper = new ExceptionWrapper<>(input, context);
        return exceptionWrapper.doHandleRequest(this);
    }

    @Override
    public Output doHandleRequest(final Input input, final Context context) {
        initializeInstance(context);
        LOG.info("Input=%s", input.toString());

        inputValidator.validate(input);
        final Workflow workflow = dynamoOperationsHelper.getWorkflow(input.getWorkflowId());

        if (workflow == null) {
            throw new BadRequestException(String.format(
                    "There is no workflow with id='%s' in the database!", input.getWorkflowId()));
        }

        final Output output = new Output();
        output.setWorkflow(workflow);
        return output;
    }

    private void initializeInstance(final Context context) {
        LOG = new Logger(context.getLogger());

        final LambdaConfigurationConstants config
                = FileReader.readObjectFromFile(Constants.CONFIG_FILE_PATH, LambdaConfigurationConstants.class);
        LOG.info("Using lambda config '%s'", config);

        final AmazonDynamoDB dynamoClient = new DynamoDBClientHelper(LOG).getDynamoDBClient(config);
        dynamoOperationsHelper = new DynamoDBOperationsHelper(dynamoClient, LOG);
        inputValidator = new GetWorkflowInputValidator(context);
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

        @Override
        public String toString() {
            return "Input{" +
                    "userArn='" + userArn + '\'' +
                    ", workflowId='" + workflowId + '\'' +
                    '}';
        }

        public void validate() {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(userArn), "The userArn cannot be null or empty!");
            Preconditions.checkArgument(!Strings.isNullOrEmpty(workflowId), "The workflowId cannot be null or empty!");
        }
    }

    public static class Output {
        private Workflow workflow;

        public Workflow getWorkflow() {
            return workflow;
        }

        public void setWorkflow(Workflow workflow) {
            this.workflow = workflow;
        }

        @Override
        public String toString() {
            return "Output{" +
                    "workflow=" + workflow +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Output output = (Output) o;
            return Objects.equals(workflow, output.workflow);
        }

        @Override
        public int hashCode() {
            return Objects.hash(workflow);
        }
    }
}

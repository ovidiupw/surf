package handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import models.Validateable;
import models.Workflow;
import models.config.LambdaConfigurationConstants;
import interpolators.Interpolator;
import interpolators.WorkflowInterpolator;
import validators.CreateWorkflowInputValidator;
import validators.Validator;
import validators.WorkflowValidator;
import utils.*;

public class CreateWorkflowHandler implements
        RequestHandler<CreateWorkflowHandler.Input, CreateWorkflowHandler.Output>,
        WrappableRequestHandler<CreateWorkflowHandler.Input, CreateWorkflowHandler.Output> {

    private Interpolator<Workflow> interpolator;
    private Validator<Workflow> workflowValidator;
    private AmazonDynamoDB dynamoClient;
    private DynamoDBOperationsHelper dynamoOperationsHelper;
    private Validator<CreateWorkflowHandler.Input> inputValidator;
    private LambdaConfigurationConstants config;

    public CreateWorkflowHandler.Output handleRequest(final CreateWorkflowHandler.Input input, final Context context) {
        final ExceptionWrapper<Input, Output> exceptionWrapper = new ExceptionWrapper<>(input, context);
        return exceptionWrapper.doHandleRequest(this);
    }

    public CreateWorkflowHandler.Output doHandleRequest(final Input input, final Context context) {
        Logger.log(context.getLogger(), "input=%s", input.toString());
        initializeInstance(context, input.getUserArn());

        inputValidator.validate(input);
        final Workflow workflow = input.getWorkflow();
        final Workflow interpolatedWorkflow = interpolator.interpolate(workflow);
        workflowValidator.validate(interpolatedWorkflow);

        final Workflow savedWorkflow = dynamoOperationsHelper.saveWorkflow(interpolatedWorkflow);

        final CreateWorkflowHandler.Output output = new CreateWorkflowHandler.Output();
        output.setWorkflow(savedWorkflow);
        return output;
    }

    private void initializeInstance(final Context context, final String userArn) {
        config = FileReader.readObjectFromFile(Constants.CONFIG_FILE_PATH, LambdaConfigurationConstants.class);
        Logger.log(context.getLogger(), "Using lambda config '%s'", config);

        dynamoClient = new DynamoDBClientHelper(context.getLogger()).getDynamoDBClient(config);
        dynamoOperationsHelper = new DynamoDBOperationsHelper(dynamoClient);
        inputValidator = new CreateWorkflowInputValidator(context);
        interpolator = new WorkflowInterpolator(context, userArn);
        workflowValidator = new WorkflowValidator(context, dynamoClient);
    }

    public static class Input implements Validateable {
        private String userArn;
        private Workflow workflow;

        public String getUserArn() {
            return userArn;
        }

        public void setUserArn(String userArn) {
            this.userArn = userArn;
        }

        public Workflow getWorkflow() {
            return workflow;
        }

        public void setWorkflow(Workflow workflow) {
            this.workflow = workflow;
        }

        @Override
        public String toString() {
            return "Input{" +
                    "userArn='" + userArn + '\'' +
                    ", workflow=" + workflow +
                    '}';
        }

        @Override
        public void validate() {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(userArn), "The userArn cannot be null or empty!");
            Preconditions.checkNotNull((workflow), "The workflow cannot be null or empty!");
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
                    "workflow='" + workflow + '\'' +
                    '}';
        }
    }
}

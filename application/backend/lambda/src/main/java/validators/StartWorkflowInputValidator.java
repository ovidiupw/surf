package validators;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import handlers.StartWorkflowHandler;
import models.exceptions.BadRequestException;
import models.workflow.Workflow;
import utils.Logger;
import utils.SurfObjectMother;
import utils.aws.dynamo.DynamoDBOperationsHelper;

import javax.annotation.Nonnull;

public class StartWorkflowInputValidator implements Validator<StartWorkflowHandler.Input> {

    private final Logger LOG;
    private final AmazonDynamoDB dynamoClient;

    public StartWorkflowInputValidator(@Nonnull final Context context, @Nonnull final AmazonDynamoDB dynamoClient) {
        this.LOG = new Logger(context.getLogger());
        this.dynamoClient = dynamoClient;
    }

    @Override
    public void validate(@Nonnull final StartWorkflowHandler.Input input) {
        LOG.info("Validating StartWorkflow.Input '%s'...", input);
        DynamoDBOperationsHelper dynamoOperationsHelper = new DynamoDBOperationsHelper(dynamoClient, LOG);

        input.validate();

        LOG.info("Verifying that a workflow with the supplied 'workflowId' exists...");
        final Workflow workflow = dynamoOperationsHelper.getWorkflow(input.getWorkflowId());

        if (workflow == null) {
            String log = LOG.info("A workflow with the supplied 'workflowId' was not found in the database!");
            throw new BadRequestException(log);
        }
        LOG.info("Successfully found workflow with the supplied 'workflowId' in the database.");

        if (!workflow.getOwnerId().equals(SurfObjectMother.getOwnerId(input.getUserArn()))) {
            LOG.info("The user making the request is not authorized to start a workflow created by another user!");
        }

        LOG.info("StartWorkflow.Input successfully passed the validation tests!");
    }
}

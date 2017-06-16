package validators;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import handlers.StartWorkflowHandler;
import models.Workflow;
import models.exceptions.BadRequestException;
import utils.DynamoDBOperationsHelper;
import utils.Logger;
import utils.SurfObjectMother;

import javax.annotation.Nonnull;

public class StartWorkflowInputValidator implements Validator<StartWorkflowHandler.Input> {

    private final Context context;
    private final AmazonDynamoDB dynamoClient;

    public StartWorkflowInputValidator(@Nonnull final Context context, @Nonnull final AmazonDynamoDB dynamoClient) {
        this.context = context;
        this.dynamoClient = dynamoClient;

    }


    @Override
    public void validate(@Nonnull final StartWorkflowHandler.Input input) {
        Logger.log(context.getLogger(), "Validating StartWorkflow.Input '%s'...", input);
        DynamoDBOperationsHelper dynamoOperationsHelper = new DynamoDBOperationsHelper(dynamoClient);

        input.validate();

        Logger.log(context.getLogger(), "Verifying that a workflow with the supplied 'workflowId' exists...");
        final Workflow workflow = dynamoOperationsHelper.getWorkflow(input.getWorkflowId());

        if (workflow == null) {
            String log = Logger.log(context.getLogger(), "A workflow with the supplied 'workflowId' was not found in the database!");
            throw new BadRequestException(log);
        }
        Logger.log(context.getLogger(), "Successfully found workflow with the supplied 'workflowId' in the database.");

        if (!workflow.getOwnerId().equals(SurfObjectMother.getOwnerId(input.getUserArn()))) {
            Logger.log(
                    context.getLogger(),
                    "The user making the request is not authorized to start a workflow created by another user!");
        }

        Logger.log(context.getLogger(), "StartWorkflow.Input successfully passed the validation tests!");
    }
}

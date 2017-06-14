package models.validators;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.lambda.runtime.Context;
import models.Workflow;
import models.exceptions.BadRequestException;
import models.exceptions.InternalServerException;
import utils.Logger;

import javax.annotation.Nonnull;

public class WorkflowValidator implements Validator<Workflow> {

    private final Context context;
    private final AmazonDynamoDB dynamoClient;

    public WorkflowValidator(@Nonnull final Context context, @Nonnull final AmazonDynamoDB dynamoClient) {
        this.context = context;
        this.dynamoClient = dynamoClient;
    }

    @Override
    public void validate(@Nonnull final Workflow workflow) {
        Logger.log(context.getLogger(), "Validating workflow '%s'...", workflow);
        workflow.validate();

        Logger.log(context.getLogger(), "Verifying that no workflow with the same id already exists in the database");

        final DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoClient);
        final Workflow dbWorkflow = dynamoDBMapper.load(workflow);

        if (dbWorkflow != null) {
            String log = Logger.log(context.getLogger(), "Found workflow with same key in database: '%s'", dbWorkflow);
            throw new InternalServerException(log);
        }

        Logger.log(context.getLogger(), "Workflow successfully passed the validation tests!");
    }
}

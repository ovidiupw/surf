package validators;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import models.exceptions.InternalServerException;
import models.workflow.Workflow;
import utils.Logger;

import javax.annotation.Nonnull;

public class WorkflowValidator implements Validator<Workflow> {

    private final Logger LOG;
    private final AmazonDynamoDB dynamoClient;

    public WorkflowValidator(@Nonnull final Context context, @Nonnull final AmazonDynamoDB dynamoClient) {
        this.LOG = new Logger(context.getLogger());
        this.dynamoClient = dynamoClient;
    }

    @Override
    public void validate(@Nonnull final Workflow workflow) {
        LOG.info("Validating workflow '%s'...", workflow);
        workflow.validate();

        LOG.info("Verifying that no workflow with the same id already exists in the database");

        final DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoClient);
        final Workflow dbWorkflow = dynamoDBMapper.load(workflow);

        if (dbWorkflow != null) {
            String log = LOG.error("Found workflow with same key in database: '%s'", dbWorkflow);
            throw new InternalServerException(log);
        }

        LOG.info("Workflow successfully passed the validation tests!");
    }
}

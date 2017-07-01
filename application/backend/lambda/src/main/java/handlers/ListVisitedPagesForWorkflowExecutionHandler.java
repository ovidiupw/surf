package handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import models.config.LambdaConfigurationConstants;
import models.workflow.VisitedPage;
import utils.*;
import utils.aws.dynamo.DynamoDBClientHelper;
import utils.aws.dynamo.DynamoDBOperationsHelper;
import validators.ListVisitedPagesForWorkflowExecutionInputValidator;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ListVisitedPagesForWorkflowExecutionHandler implements
        RequestHandler<ListVisitedPagesForWorkflowExecutionHandler.Input, ListVisitedPagesForWorkflowExecutionHandler.Output>,
        WrappableRequestHandler<ListVisitedPagesForWorkflowExecutionHandler.Input, ListVisitedPagesForWorkflowExecutionHandler.Output>  {

    private Logger LOG;
    private DynamoDBOperationsHelper dynamoOperationsHelper;
    private ListVisitedPagesForWorkflowExecutionInputValidator inputValidator;

    public ListVisitedPagesForWorkflowExecutionHandler.Output handleRequest(final ListVisitedPagesForWorkflowExecutionHandler.Input input, final Context context) {
        final ExceptionWrapper<Input, Output> exceptionWrapper = new ExceptionWrapper<>(input, context);
        return exceptionWrapper.doHandleRequest(this);
    }

    @Override
    public Output doHandleRequest(final Input input, final Context context) {
        initializeInstance(context);
        LOG.info("Input=%s", input.toString());

        inputValidator.validate(input);
        final List<VisitedPage> visitedPages = dynamoOperationsHelper.listVisitedPages(input.getWorkflowExecutionId());

        final ListVisitedPagesForWorkflowExecutionHandler.Output output = new ListVisitedPagesForWorkflowExecutionHandler.Output();
        if (visitedPages == null) {
            output.setVisitedPages(Collections.emptyList());
        } else {
            output.setVisitedPages(visitedPages);
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
        inputValidator = new ListVisitedPagesForWorkflowExecutionInputValidator(context);
    }

    public static class Input {
        private String userArn;
        private String workflowExecutionId;

        public String getUserArn() {
            return userArn;
        }

        public void setUserArn(String userArn) {
            this.userArn = userArn;
        }

        public String getWorkflowExecutionId() {
            return workflowExecutionId;
        }

        public void setWorkflowExecutionId(String workflowExecutionId) {
            this.workflowExecutionId = workflowExecutionId;
        }

        @Override
        public String toString() {
            return "Input{" +
                    "userArn='" + userArn + '\'' +
                    ", workflowExecutionId='" + workflowExecutionId + '\'' +
                    '}';
        }

        public void validate() {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(userArn), "The userArn cannot be null or empty!");
            Preconditions.checkArgument(!Strings.isNullOrEmpty(workflowExecutionId), "The workflowExecutionId cannot be null or empty!");
        }
    }

    public static class Output {
        private List<VisitedPage> visitedPages;

        public List<VisitedPage> getVisitedPages() {
            return visitedPages;
        }

        public void setVisitedPages(List<VisitedPage> visitedPages) {
            this.visitedPages = visitedPages;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Output output = (Output) o;
            return Objects.equals(visitedPages, output.visitedPages);
        }

        @Override
        public int hashCode() {
            return Objects.hash(visitedPages);
        }

        @Override
        public String toString() {
            return "Output{" +
                    "visitedPages=" + visitedPages +
                    '}';
        }
    }
}

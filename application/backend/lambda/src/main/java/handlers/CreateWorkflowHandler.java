package handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import models.Workflow;
import models.interpolators.Interpolator;
import models.interpolators.WorkflowInterpolator;
import models.parsers.JsonParser;
import models.validators.Validator;
import models.validators.WorkflowValidator;

import javax.annotation.Nonnull;

public class CreateWorkflowHandler implements RequestHandler<CreateWorkflowHandler.Input, CreateWorkflowHandler.Output> {

    private LambdaLogger LOG;
    private JsonParser jsonParser;
    private Interpolator<Workflow> interpolator;
    private Validator<Workflow> validator;

    public CreateWorkflowHandler.Output handleRequest(final CreateWorkflowHandler.Input input, final Context context) {
        LOG.log(String.format("input=%s", input.toString()));
        initializeInstance(context, input.getUserArn());

        final Workflow workflow = jsonParser.parse(input.getRawWorkflow(), Workflow.class);
        interpolator.interpolate(workflow);
        validator.validate(workflow);

        return null;
    }

    private void initializeInstance(@Nonnull final Context context, @Nonnull final String userArn) {
        LOG = context.getLogger();
        jsonParser = new JsonParser(context);
        validator = new WorkflowValidator(context);
        interpolator = new WorkflowInterpolator(context, userArn);
    }

    public static class Input {
        private String userArn;
        private String rawWorkflow;

        public String getUserArn() {
            return userArn;
        }

        public void setUserArn(String userArn) {
            this.userArn = userArn;
        }

        public String getRawWorkflow() {
            return rawWorkflow;
        }

        public void setRawWorkflow(String rawWorkflow) {
            this.rawWorkflow = rawWorkflow;
        }

        @Override
        public String toString() {
            return "Input{" +
                    "userArn='" + userArn + '\'' +
                    ", rawWorkflow=" + rawWorkflow +
                    '}';
        }
    }

    public static class Output {
    }
}

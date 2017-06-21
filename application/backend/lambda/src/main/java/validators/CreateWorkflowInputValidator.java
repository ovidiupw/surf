package validators;

import com.amazonaws.services.lambda.runtime.Context;
import handlers.CreateWorkflowHandler;
import utils.Logger;

import javax.annotation.Nonnull;

public class CreateWorkflowInputValidator implements Validator<CreateWorkflowHandler.Input> {

    private final Logger LOG;

    public CreateWorkflowInputValidator(@Nonnull final Context context) {
        this.LOG = new Logger(context.getLogger());
    }

    @Override
    public void validate(@Nonnull final CreateWorkflowHandler.Input input) {
        LOG.info("Validating CreateWorkflow input '%s'...", input);
        input.validate();
        LOG.info("CreateWorkflow input successfully passed the validation tests!");
    }
}

package validators;

import com.amazonaws.services.lambda.runtime.Context;
import handlers.ListWorkflowsHandler;
import utils.Logger;

import javax.annotation.Nonnull;

public class ListWorkflowsInputValidator implements Validator<ListWorkflowsHandler.Input> {
    private final Logger LOG;

    public ListWorkflowsInputValidator(@Nonnull final Context context) {
        this.LOG = new Logger(context.getLogger());
    }

    @Override
    public void validate(@Nonnull final ListWorkflowsHandler.Input input) {
        LOG.info("Validating ListWorkflowsHandler.Input '%s'...", input);
        input.validate();
        LOG.info("ListWorkflowsHandler.Input successfully passed the validation tests!");
    }
}

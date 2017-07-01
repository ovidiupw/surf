package validators;

import com.amazonaws.services.lambda.runtime.Context;
import handlers.ListWorkflowExecutionsHandler;
import utils.Logger;

import javax.annotation.Nonnull;

public class ListWorkflowExecutionsInputValidator implements Validator<ListWorkflowExecutionsHandler.Input> {
    private final Logger LOG;

    public ListWorkflowExecutionsInputValidator(final Context context) {
        this.LOG = new Logger(context.getLogger());
    }

    @Override
    public void validate(@Nonnull ListWorkflowExecutionsHandler.Input input) throws RuntimeException {
        LOG.info("Validating ListWorkflowExecutions input '%s'...", input);
        input.validate();
        LOG.info("ListWorkflowExecutions input successfully passed the validation tests!");
    }
}

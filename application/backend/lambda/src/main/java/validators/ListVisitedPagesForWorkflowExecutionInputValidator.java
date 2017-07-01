package validators;

import com.amazonaws.services.lambda.runtime.Context;
import handlers.ListVisitedPagesForWorkflowExecutionHandler;
import utils.Logger;

import javax.annotation.Nonnull;

public class ListVisitedPagesForWorkflowExecutionInputValidator implements Validator<ListVisitedPagesForWorkflowExecutionHandler.Input> {
    private final Logger LOG;

    public ListVisitedPagesForWorkflowExecutionInputValidator(final Context context) {
        this.LOG = new Logger(context.getLogger());
    }

    @Override
    public void validate(@Nonnull ListVisitedPagesForWorkflowExecutionHandler.Input input) throws RuntimeException {
        LOG.info("Validating ListVisitedPagesForWorkflow input '%s'...", input);
        input.validate();
        LOG.info("ListVisitedPagesForWorkflow input successfully passed the validation tests!");
    }
}

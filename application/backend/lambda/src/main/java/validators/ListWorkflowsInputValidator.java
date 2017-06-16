package validators;

import com.amazonaws.services.lambda.runtime.Context;
import handlers.ListWorkflowsHandler;
import utils.Logger;

import javax.annotation.Nonnull;

public class ListWorkflowsInputValidator implements Validator<ListWorkflowsHandler.Input> {
    private final Context context;

    public ListWorkflowsInputValidator(@Nonnull final Context context) {
        this.context = context;
    }

    @Override
    public void validate(@Nonnull final ListWorkflowsHandler.Input input) {
        Logger.log(context.getLogger(), "Validating ListWorkflowsHandler.Input '%s'...", input);
        input.validate();
        Logger.log(context.getLogger(), "ListWorkflowsHandler.Input successfully passed the validation tests!");
    }
}

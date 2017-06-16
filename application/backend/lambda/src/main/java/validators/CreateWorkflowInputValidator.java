package validators;

import com.amazonaws.services.lambda.runtime.Context;
import handlers.CreateWorkflowHandler;
import utils.Logger;

import javax.annotation.Nonnull;

public class CreateWorkflowInputValidator implements Validator<CreateWorkflowHandler.Input> {

    private final Context context;

    public CreateWorkflowInputValidator(@Nonnull final Context context) {
        this.context = context;
    }

    @Override
    public void validate(@Nonnull final CreateWorkflowHandler.Input input) {
        Logger.log(context.getLogger(), "Validating CreateWorkflow input '%s'...", input);
        input.validate();
        Logger.log(context.getLogger(), "CreateWorkflow input successfully passed the validation tests!");
    }
}

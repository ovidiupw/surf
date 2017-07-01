package validators;

import com.amazonaws.services.lambda.runtime.Context;
import handlers.GetWorkflowHandler;
import utils.Logger;

import javax.annotation.Nonnull;

public class GetWorkflowInputValidator implements Validator<GetWorkflowHandler.Input> {
    private final Logger LOG;

    public GetWorkflowInputValidator(final Context context) {
        this.LOG = new Logger(context.getLogger());
    }

    @Override
    public void validate(@Nonnull GetWorkflowHandler.Input input) throws RuntimeException {
        LOG.info("Validating GetWorkflow input '%s'...", input);
        input.validate();
        LOG.info("GetWorkflow input successfully passed the validation tests!");
    }
}

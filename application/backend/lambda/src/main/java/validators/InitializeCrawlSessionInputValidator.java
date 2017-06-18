package validators;

import com.amazonaws.services.lambda.runtime.Context;
import handlers.InitializeCrawlSessionHandler;
import utils.Logger;

import javax.annotation.Nonnull;

public class InitializeCrawlSessionInputValidator implements Validator<InitializeCrawlSessionHandler.Input> {
    private final Context context;

    public InitializeCrawlSessionInputValidator(@Nonnull final Context context) {
        this.context = context;
    }

    @Override
    public void validate(@Nonnull final InitializeCrawlSessionHandler.Input input) {
        Logger.log(context.getLogger(), "Validating ListWorkflowsHandler.Input '%s'...", input);
        input.validate();
        Logger.log(context.getLogger(), "ListWorkflowsHandler.Input successfully passed the validation tests!");
    }
}

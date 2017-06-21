package validators;

import com.amazonaws.services.lambda.runtime.Context;
import handlers.InitializeCrawlSessionHandler;
import utils.Logger;

import javax.annotation.Nonnull;

public class InitializeCrawlSessionInputValidator implements Validator<InitializeCrawlSessionHandler.Input> {
    private final Logger LOG;

    public InitializeCrawlSessionInputValidator(@Nonnull final Context context) {
        this.LOG = new Logger(context.getLogger());
    }

    @Override
    public void validate(@Nonnull final InitializeCrawlSessionHandler.Input input) {
        LOG.info("Validating ListWorkflowsHandler.Input '%s'...", input);
        input.validate();
        LOG.info("ListWorkflowsHandler.Input successfully passed the validation tests!");
    }
}

package validators;

import com.amazonaws.services.lambda.runtime.Context;
import models.workflow.CrawlWebPageStateInput;
import utils.Logger;

import javax.annotation.Nonnull;

public class CrawlWebPageInputValidator implements Validator<CrawlWebPageStateInput> {

    private final Logger LOG;

    public CrawlWebPageInputValidator(@Nonnull final Context context) {
        this.LOG = new Logger(context.getLogger());
    }

    @Override
    public void validate(@Nonnull final CrawlWebPageStateInput input) {
        LOG.info("Validating CrawlWebPage input '%s'...", input);
        input.validate();
        LOG.info("CrawlWebPage input successfully passed the validation tests!");
    }
}

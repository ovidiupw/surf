package validators;

import com.amazonaws.services.lambda.runtime.Context;
import handlers.GetS3CrawledDataHandler;
import utils.Logger;

import javax.annotation.Nonnull;

public class GetS3CrawledDataInputValidator implements Validator<GetS3CrawledDataHandler.Input> {
    private final Logger LOG;

    public GetS3CrawledDataInputValidator(final Context context) {
        this.LOG = new Logger(context.getLogger());
    }

    @Override
    public void validate(@Nonnull GetS3CrawledDataHandler.Input input) throws RuntimeException {
        LOG.info("Validating GetS3CrawledData input '%s'...", input);
        input.validate();
        LOG.info("GetS3CrawledData input successfully passed the validation tests!");
    }
}
